package fr.mjoudar.realestatemanager.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fr.mjoudar.realestatemanager.domain.models.POI
import fr.mjoudar.realestatemanager.domain.models.Particularities
import java.lang.reflect.Type

class Converters {
    private var gson = Gson()

    @TypeConverter
    fun fromParticularitiesToString(enum: List<Particularities>): String {
        if (enum.isEmpty()) return ""
        return gson.toJson(enum)
    }

    @TypeConverter
    fun fromStringToParticularities(string: String): List<Particularities> {
        if (string.isEmpty()) return arrayListOf()
        val listType: Type = object : TypeToken<List<Particularities>>() {}.type
        return gson.fromJson<List<Particularities>>(string, listType)
    }



    @TypeConverter
    fun fromPOIToString(enum: List<POI>): String? {
        return gson.toJson(enum)
    }

    @TypeConverter
    fun fromStringToPOI(string: String?): List<POI> {
        if (string == null) {
            return arrayListOf()
        }
        val listType: Type = object : TypeToken<List<POI>>() {}.type
        return gson.fromJson<List<POI>>(string, listType)
    }

}