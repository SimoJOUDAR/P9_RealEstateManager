package fr.mjoudar.realestatemanager.ui.loan

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.mjoudar.realestatemanager.utils.LoanUtils.calculateLoan
import timber.log.Timber
import java.text.NumberFormat
import java.util.*

class LoanSimulatorViewModel : ViewModel() {

    val invalidInput = MutableLiveData(false)
    val propertyValueString = MutableLiveData<String>("")
    val downPaymentString = MutableLiveData<String>("")
    val interestRateString = MutableLiveData<String>("")
    val isEuroCurrency = MutableLiveData<Boolean>(false)
    val loanTermSlide = MutableLiveData<Float>(15F)

    var propertyValue = 0.0
    var downPayment = 0.0
    var interestRate = 0.0
    var loanTerm = 0.0
    var monthlyPayment = 0.0
    var loanTermSlideString = ""

    var monthlyPaymentFormatted = MutableLiveData<String>("")

    fun setCurrency(isEuro: Boolean) {
        isEuroCurrency.value = isEuro
        Timber.tag("LoadSimulatorViewModel").d("isEuroCurrency = $isEuroCurrency")
    }

    fun submit() {
        if (isValidInput()) {
            convertValues()
        }
        else invalidInput.value = true
    }

    private fun isValidInput(): Boolean {
        return (propertyValueString.value!!.isNotEmpty() && downPaymentString.value!!.isNotEmpty() && interestRateString.value!!.isNotEmpty())
    }

    private fun convertValues() {
        propertyValue = propertyValueString.value!!.toDoubleOrNull() ?: 0.0
        downPayment = downPaymentString.value!!.toDoubleOrNull() ?: 0.0
        interestRate = interestRateString.value!!.toDoubleOrNull()?.div(100) ?: 0.0
        loanTerm = loanTermSlide.value!!.toDouble()
        loanTermSlideString = loanTermSlide.value!!.toInt().toString()
        calculate()
    }

    private fun calculate() {
        monthlyPayment = calculateLoan(propertyValue, downPayment, interestRate, loanTerm)
        formatMonthlyPayment()
    }

    private fun formatMonthlyPayment() {
        val local = if (isEuroCurrency.value!!) Locale.FRANCE else Locale.US
        val priceFormat = NumberFormat.getCurrencyInstance(local)
        priceFormat.maximumFractionDigits = 0
        monthlyPaymentFormatted.value = priceFormat.format(monthlyPayment.toLong())
    }
}