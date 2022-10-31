package fr.mjoudar.realestatemanager.ui.filter

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import fr.mjoudar.realestatemanager.databinding.FragmentFilterDialogBinding
import fr.mjoudar.realestatemanager.domain.models.Agent
import fr.mjoudar.realestatemanager.ui.homepage.HomepageViewModel
import fr.mjoudar.realestatemanager.utils.DataState
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber
import java.util.*

@AndroidEntryPoint
class FilterDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentFilterDialogBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FilterDialogViewModel by viewModels()
    private val homepageViewModel: HomepageViewModel by activityViewModels()

    // Date picker listeners
    private val publicationDateFromListener by lazy {
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            viewModel.publicationDateFrom.value = calendar
        }
    }
    private val publicationDateToListener by lazy {
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            viewModel.publicationDateTo.value = calendar
        }
    }
    private val closureDateFromListener by lazy {
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            viewModel.closureDateFrom.value = calendar
        }
    }
    private val closureDateToListener by lazy {
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            viewModel.closureDateTo.value = calendar
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFilterDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        actualizeData()
        binding.viewModel = viewModel
        setSoftKeyboardOff()
        setDatePickersListeners()
        setFilterObserver()
    }

    /***********************************************************************************************
     ** Data retrieval
     ***********************************************************************************************/
    private fun actualizeData() {
        lifecycleScope.launchWhenStarted {
            homepageViewModel.agentsState.collectLatest {
                when (it.status) {
                    DataState.Status.SUCCESS -> {
                        it.data?.let { data -> if (data.isNotEmpty()) setAgentsMenu(data) }
                    }
                    DataState.Status.LOADING -> {}
                    DataState.Status.ERROR -> {
                        displayError(it.message)
                        Timber.d("LIST_OBSERVER: ${it.message}")
                    }
                }
            }
        }
    }

    /***********************************************************************************************
     ** Adapters and listeners
     ***********************************************************************************************/

    private fun setAgentsMenu(data : List<Agent>) {
        binding.sectionFilterAgent.filterAgent.setAdapter(ArrayAdapter(requireContext(), android.R.layout.select_dialog_item, data))
        binding.sectionFilterAgent.filterAgent.setOnItemClickListener { parent, _, position, _ ->
            viewModel.agent.value = parent.getItemAtPosition(position) as Agent
        }
    }

    private fun setSoftKeyboardOff() {
        binding.sectionFilterAgent.filterAgent.inputType = InputType.TYPE_NULL
        binding.sectionFilterDates.filterPublicationDateFrom.inputType = InputType.TYPE_NULL
        binding.sectionFilterDates.filterPublicationDateTo.inputType = InputType.TYPE_NULL
        binding.sectionFilterDates.filterClosureDateFrom.inputType = InputType.TYPE_NULL
        binding.sectionFilterDates.filterClosureDateTo.inputType = InputType.TYPE_NULL
    }

    private fun setDatePickersListeners() {
        binding.sectionFilterDates.filterPublicationDateFrom.setOnClickListener {
            viewModel.publicationDateFrom.value = Calendar.getInstance()
            datePickerLauncher(publicationDateFromListener, viewModel.publicationDateFrom.value!!)
        }
        binding.sectionFilterDates.filterPublicationDateTo.setOnClickListener {
            viewModel.publicationDateTo.value = Calendar.getInstance()
            datePickerLauncher(publicationDateToListener, viewModel.publicationDateTo.value!!, viewModel.publicationDateFrom.value!!.timeInMillis)
        }
        binding.sectionFilterDates.filterClosureDateFrom.setOnClickListener {
            viewModel.closureDateFrom.value = Calendar.getInstance()
            datePickerLauncher(closureDateFromListener, viewModel.closureDateFrom.value!!, viewModel.publicationDateFrom.value!!.timeInMillis)
        }
        binding.sectionFilterDates.filterClosureDateTo.setOnClickListener {
            viewModel.closureDateTo.value = Calendar.getInstance()
            datePickerLauncher(closureDateToListener, viewModel.closureDateTo.value!!, viewModel.closureDateFrom.value!!.timeInMillis)
        }
    }

    private fun datePickerLauncher(listener:  DatePickerDialog.OnDateSetListener, calendar: Calendar, minDate: Long? = null) {
        val picker = DatePickerDialog(
            requireContext(),
            listener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        picker.datePicker.maxDate = Calendar.getInstance().timeInMillis
        minDate?.let { picker.datePicker.minDate = minDate }
        picker.show()
    }

    /***********************************************************************************************
     ** Observers
     ***********************************************************************************************/
    private fun setFilterObserver() {
        viewModel.offersFilter.observe(viewLifecycleOwner) {
            if (it != null) {
                homepageViewModel.getFilteredOfferList(it)
                findNavController().popBackStack()
            }
        }
    }

    /***********************************************************************************************
     ** Utils
     ***********************************************************************************************/
    private fun displayError(message: String?) {
        if (message != null) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(requireContext(), "Unknown error", Toast.LENGTH_LONG).show()
        }
    }

}