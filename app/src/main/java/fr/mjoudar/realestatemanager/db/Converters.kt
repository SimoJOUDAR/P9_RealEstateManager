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



//    @TypeConverter
//    fun fromParticularitiesToString(enum: List<Particularities>): String? {
//        if(enum.isEmpty()) return null
//        val builder = StringBuilder("")
//        for (i in enum) {
//            builder.append(i.name)
//            builder.append(',')
//        }
//        builder.setLength(builder.length-1)
//        return builder.toString()
//    }
//
//    @TypeConverter
//    fun fromStringToParticularities(string: String?): List<Particularities> {
//        if (string == null) {
//            return arrayListOf()
//        }
//        var enum: MutableList<Particularities> = mutableListOf()
//        when {
//            string.split(",").size > 1 -> {
//                for (i in string.split(",").indices) {
//                    enum.add(Particularities.valueOf("\"%${string.split(",")[i]}%\""))
//                }
//            }
//            string.split(",").size  == 1 -> {
//                enum.add(Particularities.valueOf("\"%$string%\""))
//            }
//        }
//        return enum.toList()
//    }

}