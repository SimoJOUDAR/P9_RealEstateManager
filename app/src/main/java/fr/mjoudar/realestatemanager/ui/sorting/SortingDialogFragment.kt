package fr.mjoudar.realestatemanager.ui.sorting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import fr.mjoudar.realestatemanager.databinding.FragmentSortingDialogBinding
import fr.mjoudar.realestatemanager.ui.homepage.HomepageViewModel

@AndroidEntryPoint
class SortingDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentSortingDialogBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SortingDialogViewModel by viewModels()
    private val homepageViewModel: HomepageViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSortingDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        setFilterObserver()
    }

    /***********************************************************************************************
     ** Observers
     ***********************************************************************************************/
    private fun setFilterObserver() {
        viewModel.offersFilter.observe(viewLifecycleOwner) {
            if (it != null) {
                homepageViewModel.getFilteredOfferList(it)
                findNavController().navigate(SortingDialogFragmentDirections.actionSortingDialogFragment2ToMainViewpagerFragment())
            }
        }
    }
}