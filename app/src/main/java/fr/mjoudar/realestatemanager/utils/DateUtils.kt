package fr.mjoudar.realestatemanager.utils

import fr.mjoudar.realestatemanager.utils.Utils.getTodayDate
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    private fun longToDate(long: Long?): Date? {
        return if (long != null) Date(long)
        else null
    }

    fun longDateToString(long: Long?): String? {
        return longToDate(long)?.let { getTodayDate(it) }
    }
}