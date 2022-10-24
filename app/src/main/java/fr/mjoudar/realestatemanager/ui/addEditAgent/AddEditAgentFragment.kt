package fr.mjoudar.realestatemanager.ui.addEditAgent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import fr.mjoudar.realestatemanager.databinding.FragmentAddEditAgentBinding

@AndroidEntryPoint
class AddEditAgentFragment : Fragment() {

    private var _binding: FragmentAddEditAgentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAddEditAgentBinding.inflate(inflater, container, false)
        return binding.root
    }


}