package fr.mjoudar.realestatemanager.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import dagger.hilt.android.AndroidEntryPoint
import fr.mjoudar.realestatemanager.R
import fr.mjoudar.realestatemanager.databinding.FragmentOfferDetailsBinding
import fr.mjoudar.realestatemanager.domain.models.Agent
import fr.mjoudar.realestatemanager.domain.models.Offer
import fr.mjoudar.realestatemanager.ui.adapters.OfferPicturesPagerAdapter
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
//    private var liteMap: GoogleMap? = null
//    private lateinit var mapView: MapView
//    private val backPressedCallback = object : OnBackPressedCallback(true) {
//        override fun handleOnBackPressed() {
//            requireActivity().findNavController(R.id.nav_host_fragment_activity_main).navigate(R.id.mainViewpagerFragment)
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { it ->
            it.getBoolean(iS_EURO_CURRENCY_ARG).let { it1 -> isEuroCurrency = it1 }
            it.getParcelable<Offer>(OFFER_ARG)?.let { it2 ->
                offer = it2
                offer?.agentId?.let {it3 ->
                    viewModel.getAgent(it3)
                    setObservers()
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentOfferDetailsBinding.inflate(inflater, container, false)
        // binding.lifecycleOwner is used for observing LiveData with Data Binding
        binding.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }

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
            binding.offer = it
            adapter.submitList(it.photos)  //TODO : try to add it as DataBinding
            binding.viewpager.adapter = adapter
            binding.agent = agent
            binding.isEuroCurrency
        }
    }

    /***********************************************************************************************
     ** Memory control
     **********************************************************************************************/

    override fun onLowMemory() {
        super.onLowMemory()
//        mapView.onLowMemory()
    }

    override fun onStart() {
        super.onStart()
//        mapView.onStart()
//        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    override fun onResume() {
        super.onResume()
//        mapView.onResume()
    }

    override fun onStop() {
        super.onStop()
//        mapView.onStop()
//        callback.remove()
    }

    override fun onDestroy() {
        super.onDestroy()
//        mapView.onDestroy()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
//        viewPager!!.adapter = null
//        viewPager = null
//        dotsIndicator = null
//        mapView.onDestroy()
    }

    companion object {
        const val OFFER_ARG = "offer"
        const val iS_EURO_CURRENCY_ARG = "isEuroCurrency"
    }
}