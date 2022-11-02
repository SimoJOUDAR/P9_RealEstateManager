package fr.mjoudar.realestatemanager.ui.map

import fr.mjoudar.realestatemanager.databinding.FragmentMapViewBinding
import fr.mjoudar.realestatemanager.domain.models.Offer
import fr.mjoudar.realestatemanager.ui.homepage.HomepageViewModel

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ktx.awaitMap
import com.google.maps.android.ktx.awaitMapLoad
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import dagger.hilt.android.AndroidEntryPoint
import fr.mjoudar.realestatemanager.R
import fr.mjoudar.realestatemanager.ui.details.OfferDetailsFragment
import fr.mjoudar.realestatemanager.utils.DataState
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@AndroidEntryPoint
class MapViewFragment : Fragment() {

    private var _binding: FragmentMapViewBinding? = null
    private val binding get() = _binding!!
    private val homepageViewModel: HomepageViewModel by activityViewModels()
    private lateinit var googleMap: GoogleMap
    private var isPermissionsAllowed = false
    private var currentLocation: LatLng = LatLng(49.339430, -123.166417)
    private var lastLocation: Location? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMapViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        permissionHandler()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMap()
    }

    /***********************************************************************************************
     ** Permission
     ***********************************************************************************************/
    private fun permissionHandler() {
        Dexter.withContext(context)
            .withPermissions(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        isPermissionsAllowed = report.areAllPermissionsGranted()
                        setUpLocationListener()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            })
            .withErrorListener {
                Timber.d(it.name)
            }
            .check()
    }
    /***********************************************************************************************
     ** Map initialization
     ***********************************************************************************************/
    @SuppressLint("MissingPermission")
    private fun setUpLocationListener() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 2000)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(2000)
            .setMaxUpdateDelayMillis(1000)
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                for (location in locationResult.locations) {
                    currentLocation = LatLng(location.latitude, location.longitude)
                    lastLocation = location
                }
            }
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    @SuppressLint("PotentialBehaviorOverride", "MissingPermission")
    private fun setupMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        lifecycleScope.launchWhenCreated {
            if (mapFragment != null) {
                googleMap = mapFragment.awaitMap()
                googleMap.awaitMapLoad()
                when (resources.configuration.orientation) {
                    Configuration.ORIENTATION_PORTRAIT -> googleMap.setPadding(0, 0, 0, 300);
                    else -> googleMap.setPadding(0, 0, 100, 300);
                }
                googleMap.setOnInfoWindowClickListener { marker -> viewPropertyDetail(marker.tag as Offer) }
                googleMap.uiSettings.isZoomControlsEnabled = true
                googleMap.uiSettings.isCompassEnabled = true
                googleMap.isMyLocationEnabled = true
                googleMap.uiSettings.isMyLocationButtonEnabled = true
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12f))
                actualizedDataObserver()
            }
        }
    }

    private fun addMarkers(location: LatLng, offer: Offer) {
        val marker: Marker? = googleMap.addMarker(
            MarkerOptions()
                .position(location)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
                .title(offer.address?.vicinity)
        )
        marker?.tag = offer
    }

    private fun viewPropertyDetail(offer: Offer) {
        val bundle = Bundle()
        bundle.putParcelable(OfferDetailsFragment.OFFER_ARG, offer)
        findNavController().navigate(R.id.offerDetailsFragment, bundle)
    }

    /***********************************************************************************************
     ** Data fetching
     ***********************************************************************************************/
    private fun actualizeData() {
        lifecycleScope.launchWhenStarted {
            homepageViewModel.offersState.collectLatest {
                when (it.status) {
                    DataState.Status.SUCCESS -> {
                        displayLoading(false)
                        it.data?.let { data -> submitOffersList(data) }
                    }
                    DataState.Status.LOADING -> {
                        displayLoading(true)
                    }
                    DataState.Status.ERROR -> {
                        displayLoading(false)
                        displayError(it.message)
                        Timber.d("LIST_OBSERVER: ${it.message}")
                    }
                }
            }
        }
    }

    private fun submitOffersList(data : List<Offer>) {
        googleMap.clear()
        data.forEach { offer ->
            if (offer.address?.lat != null && offer.address?.lng != null) {
                val location = LatLng(offer.address?.lat!!, offer.address?.lng!!)
                addMarkers(location, offer)
            }
        }
    }

    /***********************************************************************************************
     ** Observers
     ***********************************************************************************************/
    private fun actualizedDataObserver() {
        homepageViewModel.dataActualized.observe(viewLifecycleOwner) {
            if (it) actualizeData()
        }
    }

    /***********************************************************************************************
     ** Utils
     ***********************************************************************************************/
    private fun displayLoading(isLoading: Boolean) {
        if (isLoading) binding.mapProgressBar.visibility = View.VISIBLE
        else binding.mapProgressBar.visibility = View.GONE
    }

    private fun displayError(message: String?) {
        if (message != null) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(requireContext(), "Unknown error", Toast.LENGTH_LONG).show()
        }
    }
}