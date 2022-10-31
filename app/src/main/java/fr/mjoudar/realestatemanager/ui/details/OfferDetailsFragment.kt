package fr.mjoudar.realestatemanager.ui.details

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ktx.awaitMap
import com.google.maps.android.ktx.awaitMapLoad
import dagger.hilt.android.AndroidEntryPoint
import fr.mjoudar.realestatemanager.R
import fr.mjoudar.realestatemanager.databinding.FragmentOfferDetailsBinding
import fr.mjoudar.realestatemanager.domain.models.Agent
import fr.mjoudar.realestatemanager.domain.models.Offer
import fr.mjoudar.realestatemanager.ui.adapters.OfferPicturesPagerAdapter
import fr.mjoudar.realestatemanager.ui.addEditOffer.AddEditOfferFragment
import fr.mjoudar.realestatemanager.utils.DataState
import timber.log.Timber

@AndroidEntryPoint
class OfferDetailsFragment : Fragment() {

    private var _binding: FragmentOfferDetailsBinding? = null
    private val binding get() = _binding!!
    private var offer: Offer? = null
    private var agent: Agent? = null
    private var adapter = OfferPicturesPagerAdapter()
    private var isEuroCurrency = false
    private val viewModel: OfferDetailsViewModel by viewModels()
    lateinit var navController: NavController
    private var liteMap: GoogleMap? = null
    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { it -> retrievedArguments(it) }
        lifecycleScope.launchWhenResumed {navController = findNavController()}
        onBackPressedHandler()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentOfferDetailsBinding.inflate(inflater, container, false)
        // binding.lifecycleOwner is used for observing LiveData with Data Binding
        binding.lifecycleOwner = this.viewLifecycleOwner
        initGoogleMap(savedInstanceState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retrievedArgumentsFromStack()
        setListeners()
    }

    /***********************************************************************************************
     ** Data retrieval
     ***********************************************************************************************/
    private fun retrievedArguments(data: Bundle) {
        data.getParcelable<Offer>(OFFER_ARG)?.let { it ->
            offer = it
            offer?.agentId?.let {it2 ->
                viewModel.getAgent(it2)
                setObservers()
            }
        }
    }

    private fun retrievedArgumentsFromStack() {
        lifecycleScope.launchWhenResumed {
            navController.currentBackStackEntry?.savedStateHandle?.getLiveData<Offer>(OFFER_ARG)
                ?.observe(viewLifecycleOwner) { it ->
                    offer = it
                    offer?.agentId?.let {it1 ->
                        viewModel.getAgent(it1)
                        setObservers()
                    }
                }
        }
    }

    private fun setupData() {
        val sharedPreference =  requireActivity().getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE)
        isEuroCurrency = sharedPreference.getBoolean("isCurrencyEuro", false)
    }

    private fun initGoogleMap(savedInstanceState: Bundle?) {
        mapView = binding.liteMapView
        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle("MapViewBundleKey")
        }
        mapView.onCreate(mapViewBundle)
        lifecycleScope.launchWhenCreated {
            // Get map
            liteMap = mapView.awaitMap()
            // Get map
            liteMap!!.awaitMapLoad()
            liteMap!!.uiSettings.isZoomControlsEnabled = true
            if (offer?.address?.lat != null && offer?.address?.lng != null) {
                val location = LatLng(offer!!.address?.lat!!, offer!!.address?.lng!!)
                liteMap!!.moveCamera(CameraUpdateFactory.newLatLng(location))
                addMarker(liteMap, location)
            }
        }
    }

    private fun addMarker(googleMap: GoogleMap?, location: LatLng) {
        googleMap?.addMarker(
            MarkerOptions()
                .position(location)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
                .title(offer?.address?.vicinity)
        )
    }

    /***********************************************************************************************
     ** Observers
     ***********************************************************************************************/
    private fun setObservers() {
        lifecycleScope.launchWhenStarted {
            viewModel.agentState
                .collect {
                    when (it.status) {
                    DataState.Status.SUCCESS -> {
                        it.data?.let { data ->
                            agent = data
                            updateContent()
                        }
                    }
                    DataState.Status.LOADING -> {

                    }
                    DataState.Status.ERROR -> {
                        Timber.d("LIST_OBSERVER: ${it.message}")
                    }
                }
            }
        }
    }

    private fun updateContent() {
        offer?.let {
            setupData()
            binding.offer = it
            adapter.submitList(it.photos)
            binding.viewpager.adapter = adapter
            binding.agent = agent
            binding.isEuroCurrency = isEuroCurrency
        }
    }

    /***********************************************************************************************
     ** Listeners
     ***********************************************************************************************/
    private fun setListeners() {
        binding.customUpButton.setOnClickListener { clearBackStack() }
        binding.editButton.setOnClickListener {
            lifecycleScope.launchWhenResumed {
                val bundle = Bundle()
                bundle.putParcelable(AddEditOfferFragment.OFFER_ARG, offer)
                navController.navigate(R.id.addEditOfferFragment, bundle)
            }
        }
    }

    /***********************************************************************************************
     ** popBackStack handlers
     ***********************************************************************************************/

    private fun onBackPressedHandler() {
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                clearBackStack()
            }
        })
    }

    private fun clearBackStack() {
        navController.navigate(OfferDetailsFragmentDirections.actionOfferDetailsFragmentToMainViewpagerFragment())
    }

    /***********************************************************************************************
     ** Memory control
     **********************************************************************************************/

//    override fun onLowMemory() {
//        super.onLowMemory()
//        mapView.onLowMemory()
//    }
//
//    override fun onStart() {
//        super.onStart()
//        mapView.onStart()
//    }
//
//    override fun onResume() {
//        super.onResume()
//        mapView.onResume()
//    }
//
//    override fun onStop() {
//        super.onStop()
//        mapView.onStop()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        mapView.onDestroy()
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        mapView.onDestroy()
//    }

    companion object {
        const val OFFER_ARG = "offer"
    }
}