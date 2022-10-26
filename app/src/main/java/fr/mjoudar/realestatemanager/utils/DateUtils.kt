package fr.mjoudar.realestatemanager.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    private val dmyFormat = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)

    private fun longToDate(long: Long?): Date? {
        return if (long != null) Date(long)
        else null
    }

    private fun dateToString(date: Date): String {
        return try {
            dmyFormat.format(date)
        } catch (e: NumberFormatException) {
            return ""
        }
    }

    fun longDateToString(long: Long?): String? {
        return longToDate(long)?.let { dateToString(it) }
    }
}