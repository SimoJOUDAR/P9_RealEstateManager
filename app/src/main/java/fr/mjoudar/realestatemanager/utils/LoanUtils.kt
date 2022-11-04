package fr.mjoudar.realestatemanager.utils

import kotlin.math.pow

object LoanUtils {

    fun calculateLoan(propertyValue: Double, downPayment: Double, interestRate: Double, loanTerm: Double) : Double {
        val loan = propertyValue - downPayment
        return if (loan == 0.0 ) 0.0 else (((loan*interestRate)/12) / (1-(1+(interestRate/12)).pow(-(loanTerm*12))))
    }
}