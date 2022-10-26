package fr.mjoudar.realestatemanager.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import fr.mjoudar.realestatemanager.R
import fr.mjoudar.realestatemanager.databinding.FragmentListViewBinding
import fr.mjoudar.realestatemanager.domain.models.Offer
import fr.mjoudar.realestatemanager.ui.adapters.OffersAdapter
import fr.mjoudar.realestatemanager.ui.details.OfferDetailsFragment
import fr.mjoudar.realestatemanager.ui.homepage.HomepageViewModel
import fr.mjoudar.realestatemanager.utils.DataState
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@AndroidEntryPoint
class ListViewFragment : Fragment() {

    private var offersList: List<Offer> = emptyList()
    private lateinit var adapter: OffersAdapter
    private val homepageViewModel: HomepageViewModel by activityViewModels()
    private var _binding: FragmentListViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentListViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.swipeRefreshLayout.setOnRefreshListener { setObserver() }
        setRecyclerView()
        setObserver()
    }

    private fun setRecyclerView() {
        binding.swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.orange_shadow)
        val onClickListener = View.OnClickListener { itemView ->
            val item = itemView.tag as Offer
            val bundle = Bundle()
            bundle.putParcelable(OfferDetailsFragment.OFFER_ARG, item)
            bundle.putBoolean(OfferDetailsFragment.iS_EURO_CURRENCY_ARG, homepageViewModel.isCurrencyEuro.value!!)
            itemView.findNavController().navigate(R.id.offerDetailsFragment, bundle)
        }
        val onContextClickListener = View.OnContextClickListener { true }
        adapter = OffersAdapter(onClickListener, onContextClickListener, homepageViewModel.isCurrencyEuro.value?: false)
        binding.recyclerview.adapter = adapter
    }


    private fun setObserver() {
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
            homepageViewModel.isCurrencyEuro.observe(requireActivity()) {
                if (::adapter.isInitialized)
                    adapter.setCurrency(it)
            }
        }
    }

    private fun submitOffersList(data : List<Offer>) {
        binding.swipeRefreshLayout.isRefreshing = false
        offersList = data
        adapter.setData(data)
    }

    private fun displayLoading(isLoading: Boolean) {
        if (isLoading) binding.progressBar.visibility = View.VISIBLE
        else binding.progressBar.visibility = View.GONE
    }

    private fun displayError(message: String?) {
        if (message != null) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(requireContext(), "Unknown error", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        _binding = null
//        binding.recyclerview.adapter = null
    }
}