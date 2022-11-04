package fr.mjoudar.realestatemanager

import fr.mjoudar.realestatemanager.utils.DateUtils.longToDate
import fr.mjoudar.realestatemanager.utils.DateUtils.longDateToString
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DateUtilTest {

    @Test
    @Throws(Exception::class)
    fun should_get_the_correct_date() {
        val currentTime = System.currentTimeMillis()
        val date = Date(currentTime)
        assertEquals(date, longToDate(currentTime))
    }

    @Test
    @Throws(Exception::class)
    fun should_get_date_in_the_correct_format() {
        val currentTime = System.currentTimeMillis()
        val date = Date(currentTime)
        val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        val today = dateFormat.format(date)
        val formattedDate = longDateToString(currentTime)
        assertEquals(today, formattedDate)
    }
}
