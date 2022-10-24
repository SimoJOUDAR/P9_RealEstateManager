package fr.mjoudar.realestatemanager.ui.loan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import fr.mjoudar.realestatemanager.databinding.FragmentLoanSimulatorBinding

@AndroidEntryPoint
class LoadSimulatorFragment : Fragment() {

    private var _binding: FragmentLoanSimulatorBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentLoanSimulatorBinding.inflate(inflater, container, false)
        return binding.root
    }

}