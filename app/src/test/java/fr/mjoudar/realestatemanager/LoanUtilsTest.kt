package fr.mjoudar.realestatemanager

import fr.mjoudar.realestatemanager.utils.LoanUtils.calculateLoan
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.Test

class LoanUtilsTest {

    @Test
    @Throws(Exception::class)
    fun should_get_zero_for_down_payment_equals_loan_value() {
        val propertyValue = 100.0
        val downPayment = 100.0
        val interestRate = 0.0
        val loanTerm = 15.0
        assertEquals(0, calculateLoan(propertyValue, downPayment, interestRate, loanTerm).toInt())
    }

    @Test
    @Throws(Exception::class)
    fun should_get_precise_monthly_payment() {
        val propertyValue = 100000.0
        val downPayment = 10000.0
        val interestRate = 2.5
        val loanTerm = 15.0
        assertEquals(18750, calculateLoan(propertyValue, downPayment, interestRate, loanTerm).toInt())
    }


}