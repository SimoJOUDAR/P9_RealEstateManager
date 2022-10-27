package fr.mjoudar.realestatemanager.ui.addEditAgent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
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
    private var agent: Agent? = null
    private var isNewAgent = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { it ->
            it.getBoolean(iS_NEW_AGENT_ARG).let { it1 -> isNewAgent = it1 }
            it.getParcelable<Agent>(AGENT_ARG).let { it2 -> agent = it2 }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAddEditAgentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.viewModel = viewModel
        binding.agent = null
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
    }

    private fun setObservers() {
        viewModel.inputIncomplete.observe(viewLifecycleOwner) {
            if (it) {
                Snackbar.make(requireActivity().findViewById(R.id.root_layout), R.string.agent_input_incomplete, Snackbar.LENGTH_SHORT).show()
            }
        }
        viewModel.isAgentSaved.observe(viewLifecycleOwner) {
            if (it) {
                NotificationHandler.createNotification(
                    requireContext(),
                    "Agent created",
                    "The new agent was successfully create.",
                    "",
                    autoCancel = false
                )
                findNavController().navigate(R.id.mainViewpagerFragment)
            }
        }
    }


    companion object {
        const val AGENT_ARG = "agent"
        const val iS_NEW_AGENT_ARG = "isNewAgent"
    }

}