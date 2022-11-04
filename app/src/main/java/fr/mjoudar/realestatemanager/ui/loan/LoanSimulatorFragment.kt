package fr.mjoudar.realestatemanager.ui.loan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import fr.mjoudar.realestatemanager.R
import fr.mjoudar.realestatemanager.databinding.FragmentLoanSimulatorBinding

@AndroidEntryPoint
class LoanSimulatorFragment : Fragment() {

    private var _binding: FragmentLoanSimulatorBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoanSimulatorViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentLoanSimulatorBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        addListeners()
    }

    private fun setObservers() {
        viewModel.invalidInput.observe(viewLifecycleOwner) {
            if (it) Toast.makeText(requireContext(), R.string.invalid_input, Toast.LENGTH_LONG).show()
        }

        viewModel.monthlyPaymentFormatted.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                val text1 = resources.getString(R.string.loan_simulator_results_body, viewModel.interestRateString.value!!, viewModel.loanTermSlideString)
                binding.loanSimulatorResultsBody.text = text1
                binding.loanSimulatorResultsMonthlyPayments.text = viewModel.monthlyPaymentFormatted.value!!
                binding.loanSimulatorResultsBody.visibility = View.VISIBLE
                binding.loanSimulatorResultsMonthlyPayments.visibility = View.VISIBLE
            }
        }

        viewModel.isEuroCurrency.observe(viewLifecycleOwner) {
            val icon = if (it) R.drawable.ic_euro_24 else R.drawable.ic_money_24
            binding.loanSimulatorPropertyValue.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0)
            binding.loanSimulatorDownPayment.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0)
        }

    }

    private fun addListeners() {
        binding.loanSimulatorLoanTermSlider.addOnChangeListener { _, value, _ ->
            viewModel.loanTermSlide.value = value
        }
    }

}