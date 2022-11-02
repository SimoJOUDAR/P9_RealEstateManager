package fr.mjoudar.realestatemanager.ui.addEditAgent

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import fr.mjoudar.realestatemanager.R
import fr.mjoudar.realestatemanager.databinding.FragmentAddEditAgentBinding
import fr.mjoudar.realestatemanager.domain.models.Agent
import fr.mjoudar.realestatemanager.notification.NotificationHandler

@AndroidEntryPoint
class AddEditAgentFragment : Fragment() {

    private var _binding: FragmentAddEditAgentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddEditAgentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAddEditAgentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        setSoftKeyboardOff()
        deleteButtonListener()
    }

    /***********************************************************************************************
     ** Adapters
     ***********************************************************************************************/
    private fun setAgentsAdapter(data: List<Agent>) {
        binding.addEditOfferAgent.setAdapter(ArrayAdapter(requireContext(), android.R.layout.select_dialog_item, data))
        binding.addEditOfferAgent.setOnItemClickListener { parent, _, position, _ ->
            viewModel.agent.value = parent.getItemAtPosition(position) as Agent
        }
    }

    /***********************************************************************************************
     ** Observers
     ***********************************************************************************************/
    private fun setObservers() {
        setAgentListObserver()
        setAgentObserver()
        setInputIncompleteObserver()
        setIsAgentSavedObserver()
        setErrorMessageObserver()
    }

    private fun setAgentListObserver() {
        viewModel.agentList.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) setAgentsAdapter(it)
        }
    }

    private fun setAgentObserver() {
        viewModel.agent.observe(viewLifecycleOwner) {
            it?.let { viewModel.loadAgent() }
        }
    }

    private fun setInputIncompleteObserver() {
        viewModel.inputIncomplete.observe(viewLifecycleOwner) {
            if (it) {
//                Snackbar.make(requireActivity().findViewById(R.id.root_layout), R.string.invalid_input, Snackbar.LENGTH_SHORT).show()
                Toast.makeText(requireContext(), R.string.invalid_input, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setIsAgentSavedObserver() {
        viewModel.isAgentSaved.observe(viewLifecycleOwner) {
            if (it) {
                createNotification()
                navigateBack()
            }
        }
    }

    private fun setErrorMessageObserver() {
        viewModel.errorMessage.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        }
    }

    /***********************************************************************************************
     ** Listeners
     ***********************************************************************************************/
    private fun deleteButtonListener() {
        binding.btnDeleteAgent.setOnClickListener {
            Toast.makeText(requireContext(), R.string.delete_agent_not_allowed, Toast.LENGTH_LONG).show()
        }
    }

    /***********************************************************************************************
     ** Utils
     ***********************************************************************************************/
    companion object {
        const val AGENT_ARG = "agent"
    }

    private fun setSoftKeyboardOff() {
        binding.addEditOfferAgent.inputType = InputType.TYPE_NULL
    }

    private fun createNotification() {
        NotificationHandler.createAgentNotification(
            requireContext(),
            "Agent created",
            "The agent ${viewModel.name.value} has been successfully saved.",
            "",
             false
        )
    }

    private fun navigateBack() {
        findNavController().navigate(AddEditAgentFragmentDirections.actionAddEditAgentFragmentToMainViewpagerFragment())
    }

}