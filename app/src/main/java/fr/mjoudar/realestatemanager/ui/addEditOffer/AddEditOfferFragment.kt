package fr.mjoudar.realestatemanager.ui.addEditOffer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import fr.mjoudar.realestatemanager.databinding.FragmentAddEditOfferBinding

@AndroidEntryPoint
class AddEditOfferFragment : Fragment() {

    private var _binding: FragmentAddEditOfferBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAddEditOfferBinding.inflate(inflater, container, false)
        return binding.root
    }

}