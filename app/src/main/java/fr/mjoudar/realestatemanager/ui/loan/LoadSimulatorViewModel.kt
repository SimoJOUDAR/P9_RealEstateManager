package fr.mjoudar.realestatemanager.ui.loan

import androidx.lifecycle.MutableLiveData

class LoadSimulatorViewModel {

    val propertyValueString = MutableLiveData<String?>("")
    val downPaymentString = MutableLiveData<String?>("")
    val interestRateString = MutableLiveData<String?>("")
    val dollar = MutableLiveData<Boolean?>(true)
    val euro = MutableLiveData<Boolean?>(false)
    val loanTerm = MutableLiveData<Int>(15)
    val monthlyPayment = MutableLiveData<Float>(0F)

    val propertyValue = MutableLiveData<Int?>(0)
    val downPayment = MutableLiveData<Int?>(0)
    val interestRate = MutableLiveData<Float?>(0F)

    fun calculate() {}

}