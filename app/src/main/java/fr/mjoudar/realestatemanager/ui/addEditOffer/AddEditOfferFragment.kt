package fr.mjoudar.realestatemanager.ui.addEditOffer

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import fr.mjoudar.realestatemanager.R
import fr.mjoudar.realestatemanager.databinding.FragmentAddEditOfferBinding
import fr.mjoudar.realestatemanager.domain.models.Agent
import fr.mjoudar.realestatemanager.domain.models.Offer
import fr.mjoudar.realestatemanager.domain.models.Photo
import fr.mjoudar.realestatemanager.notification.NotificationHandler
import fr.mjoudar.realestatemanager.ui.adapters.AddEditOfferPicturesAdapter
import timber.log.Timber
import java.util.*


@AndroidEntryPoint
class AddEditOfferFragment : Fragment() {

    private var _binding: FragmentAddEditOfferBinding? = null
    private val binding get() = _binding!!
    private var argOffer = Offer("")
    lateinit var navController: NavController

    private val viewModel: AddEditOfferViewModel by viewModels()
    private var photos: List<Photo> = emptyList()
    private lateinit var adapter: AddEditOfferPicturesAdapter

    // Date picker listeners
    private val publicationDateListener by lazy {
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            viewModel.publicationDate.value = calendar
        }
    }
    private val closureDateListener by lazy {
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            viewModel.closureDate.value = calendar
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retrievedArguments()
        lifecycleScope.launchWhenResumed {navController = findNavController()}
        onBackPressedHandler()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAddEditOfferBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapters()
        setObservers()
        setDatePickersListeners()
        setSoftKeyboardOff()
    }

    //TODO: Test----------------------------------------------------------------------------------------------------------------------------------------------------

    /***********************************************************************************************
     ** Data retrieval
     ***********************************************************************************************/
    private fun retrievedArguments() {
        arguments?.let { it ->
            it.getParcelable<Offer>(OFFER_ARG)?.let { it2 ->
                argOffer = it2
                viewModel.loadData(it2)
            }
        }
    }

    /***********************************************************************************************
     ** Adapters
     ***********************************************************************************************/
    private fun setAdapters() {
        setTypeAdapters()
        setPhotosAdapter()
    }

    private fun setTypeAdapters() {
        binding.sectionAddEditType.addEditOfferPropertyType.setAdapter(ArrayAdapter(requireContext(), android.R.layout.select_dialog_item, viewModel.propertyTypeValues))
        binding.sectionAddEditType.addEditOfferOfferType.setAdapter(ArrayAdapter(requireContext(), android.R.layout.select_dialog_item, viewModel.offerTypeValues))
    }

    private fun setPhotosAdapter() {
        // TODO: Set up delete listener
        val onClickListener = View.OnClickListener { itemView ->
            val item = itemView.tag as Photo
            // TODO: Setup onDeletePicture() method
        }
        val onContextClickListener = View.OnContextClickListener { true }
        adapter = AddEditOfferPicturesAdapter(onClickListener, onContextClickListener)
        binding.sectionAddEditPictures.picturesRecyclerView.adapter = adapter
    }

    /***********************************************************************************************
     ** Observers
     ***********************************************************************************************/
    private fun setObservers() {
        setAgentListObserver()
        setPhotosObserver()
        setInputIncompleteObserver()
        setIsOfferSavedObserver()
        setIsAgentListEmptyObserver()

        viewModel.isOfferClosed.observe(requireActivity()) {
            Timber.tag("isOfferClosed_Test").d("viewModel.isOfferClosed = ${viewModel.isOfferClosed.value}")
            Timber.tag("isOfferClosed_Test").d("viewModel.offer.isOfferClosed = ${!(viewModel.offer?.availability)!!}")
        }
    }

    // Observes agentList to setup the agents' autocomplete adapter
    private fun setAgentListObserver() {
        viewModel.agentList.observe(viewLifecycleOwner) { it1 ->
            it1?.let {
                binding.sectionAddEditAgent.addEditOfferAgent.setAdapter(ArrayAdapter(requireContext(), android.R.layout.select_dialog_item, it1))
                binding.sectionAddEditAgent.addEditOfferAgent.setOnItemClickListener { parent, _, position, _ ->
                    viewModel.agent.value = parent.getItemAtPosition(position) as Agent
                }
            }
        }
    }

    // Observes photos to setup the photos RecyclerView adapter
    private fun setPhotosObserver() {
        viewModel.photos.observe(viewLifecycleOwner) { it1 ->
            it1?.let { it2 ->
                photos = it2 // TODO : To remove ?
                adapter.setData(it2)
                binding.sectionAddEditPictures.picturesRecyclerView.adapter = adapter
                //TODO : update the recyclerView adapter
            }
        }
    }

    private fun setInputIncompleteObserver() {
        viewModel.inputIncomplete.observe(viewLifecycleOwner) {
            if (it) {
//                Snackbar.make(requireActivity().findViewById(R.id.root_layout), R.string.agent_input_incomplete, Snackbar.LENGTH_SHORT).show()
                Toast.makeText(requireContext(), R.string.agent_input_incomplete, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setIsOfferSavedObserver() {
        viewModel.isOfferSaved.observe(viewLifecycleOwner) {
            if (it) {
                launchNotification()
                when (viewModel.isNewOffer) {
                    true -> clearBackStack()
                    false -> popBackStackWithArgument(viewModel.offer!!)
                }
            }
        }
    }

    private fun setIsAgentListEmptyObserver() {
        viewModel.isAgentListEmpty.observe(viewLifecycleOwner) { it1 ->
            if (it1) {
                viewModel.errorMessage?.let { it2 ->
                    emptyAgentListHandler(it2)
                }?: run {
                    emptyAgentListHandler()
                }
            }
        }
    }

    /***********************************************************************************************
     ** popBackStack handlers
     ***********************************************************************************************/
    private fun onBackPressedHandler() {
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                popBackStackWithArgument(argOffer)
            }
        })
    }

    private fun launchNotification() {
        NotificationHandler.createNotification(
            requireContext(),
            "Offer saved",
            "The offer has been successfully saved.",
            "",
            autoCancel = false
        )
    }

    private fun clearBackStack() {
        Timber.tag("popBack").d("clearBackStack")
        val entry = parentFragmentManager.getBackStackEntryAt(0)
        parentFragmentManager.popBackStack(entry.id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    private fun popBackStackWithArgument(data : Offer) {
        Timber.tag("popBack").d("popBackStackWithArgument")
        lifecycleScope.launchWhenResumed {
            navController.previousBackStackEntry?.savedStateHandle?.set(OFFER_ARG, data)
            navController.popBackStack()
            //val id = navController.previousBackStackEntry?.destination?.id
            //navController.popBackStack(id!!, true)
        }
    }

    /***********************************************************************************************
     ** Listeners
     ***********************************************************************************************/
    private fun setDatePickersListeners() {
        binding.sectionAddEditDates.addEditOfferPublicationDate.setOnClickListener {
            datePickerLauncher(publicationDateListener, viewModel.publicationDate.value!!)
        }
        binding.sectionAddEditDates.addEditOfferClosureDate.setOnClickListener {
            datePickerLauncher(closureDateListener, viewModel.closureDate.value!!, viewModel.publicationDate.value!!.timeInMillis)
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
     ** Utils
     ***********************************************************************************************/
    private fun setSoftKeyboardOff() {
        binding.sectionAddEditType.addEditOfferPropertyType.inputType = InputType.TYPE_NULL
        binding.sectionAddEditType.addEditOfferOfferType.inputType = InputType.TYPE_NULL
        binding.sectionAddEditAgent.addEditOfferAgent.inputType = InputType.TYPE_NULL
        binding.sectionAddEditDates.addEditOfferPublicationDate.inputType = InputType.TYPE_NULL
        binding.sectionAddEditDates.addEditOfferClosureDate.inputType = InputType.TYPE_NULL
    }

    private fun emptyAgentListHandler(error: String? = null) {
        error?.let {
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }?: run {
            Toast.makeText(requireContext(), getString(R.string.empty_agent_list), Toast.LENGTH_SHORT).show()
        }
        findNavController().navigate(R.id.mainViewpagerFragment)
    }

    companion object {
        const val OFFER_ARG = "offer"
    }
}