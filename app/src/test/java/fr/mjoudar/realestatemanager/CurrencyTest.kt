package fr.mjoudar.realestatemanager

import fr.mjoudar.realestatemanager.utils.Utils.convertDollarToEuro
import fr.mjoudar.realestatemanager.utils.Utils.convertEuroToDollar
import junit.framework.TestCase
import org.junit.Test

class CurrencyTest {

    @Test
    @Throws(Exception::class)
    fun should_convert_dollar_to_euro() {
        val propertyCostInDollars : Long = 100_000
        val propertyCostInEuros : Long = 81200
        TestCase.assertEquals(propertyCostInEuros, convertDollarToEuro(propertyCostInDollars))
    }

    @Test
    @Throws(Exception::class)
    fun should_convert_euro_to_dollar() {
        val propertyCostInDollars : Long = 100_000
        val propertyCostInEuros : Long = 81200
        TestCase.assertEquals(propertyCostInDollars, convertEuroToDollar(propertyCostInEuros))
    }

    @Test
    @Throws(Exception::class)
    fun should_convert_euro_to_dollar_to_euro() {
        val dollar = convertEuroToDollar(1)
        val euro = convertDollarToEuro(dollar)
        TestCase.assertEquals(1, euro)
    }


}