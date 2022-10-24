package fr.mjoudar.realestatemanager.ui.addEditOffer

import androidx.lifecycle.MutableLiveData
import fr.mjoudar.realestatemanager.domain.models.*
import java.util.*

class AddEditOfferViewModel {

    //Type
    val propertyType = MutableLiveData<String>("")
    val propertyTypeValues = MutableLiveData<Array<PropertyType>>(PropertyType.values())
    val offerType = MutableLiveData<String>("")
    val offerTypeValues = MutableLiveData<Array<OfferType>>(OfferType.values())
    //Price
    val price = MutableLiveData<String>("")
    //Characteristics
    val surface = MutableLiveData<String>("")
    val rooms = MutableLiveData<String>("")
    val bathrooms = MutableLiveData<String>("")
    //Particularities
    val boolParticularities = MutableLiveData<MutableList<Boolean>>(mutableListOf(false, false, false, false, false, false, false, false, false, false))
    val namedParticularities = mutableListOf(Particularities.GARAGE, Particularities.PARKING_LOT, Particularities.BASEMENT, Particularities.BALCONY, Particularities.BACKYARD, Particularities.SWIMMING_POOL, Particularities.GYM_ROOM, Particularities.GARDEN, Particularities.JACUZZI, Particularities.SAUNA)
    val particularities = MutableLiveData<MutableList<Particularities>>(mutableListOf())
    //Description
    val description = MutableLiveData<String>("")
    //Address
    val address = MutableLiveData<String?>("")
    val complement = MutableLiveData<String?>("")
    val city = MutableLiveData<String?>("")
    val zipCode = MutableLiveData<String?>("")
    val state = MutableLiveData<String?>("")
    val country = MutableLiveData<String?>("")
    //Poi
    val boolPoi = MutableLiveData<MutableList<Boolean>>(mutableListOf(false, false, false, false, false, false, false, false, false))
    val namedPoi = mutableListOf(POI.BUS_STATION, POI.MARKET_MALL, POI.MEDICAL_CENTER, POI.SPORT_CENTER, POI.CULTURAL_CENTER, POI.SCHOOL, POI.PARK, POI.BAR_COFFEESHOP, POI.RESTAURANT)
    val poi = MutableLiveData<MutableList<POI>>(mutableListOf())
    //Agent
    val agent = MutableLiveData<Agent>()
    //Dates
    var publicationDate = MutableLiveData<Long>(0)
    var closureDate = MutableLiveData<Long>(0)
    val isOfferClosed = MutableLiveData<Boolean>(false)

    //Photos
    val pictures = MutableLiveData<MutableList<Photo>>(arrayListOf())

    //At initializing object
    fun particularitiesEnumToBoolConverter() {
        val capVal = particularities.value
        val newVal = mutableListOf(false, false, false, false, false, false, false, false, false, false)
        if (capVal!!.isNotEmpty()) {
            for (i in capVal) {
                val j = namedParticularities.indexOf(i)
                newVal[j] = true
            }
        }
        boolParticularities.value = newVal
    }

    // at saving object
    fun particularitiesBoolToEnumConverter() {
        val capVal = boolParticularities.value
        val newVal: MutableList<Particularities> = mutableListOf()
        for (i in 0..capVal!!.lastIndex) {
            if (capVal[i]) newVal.add(namedParticularities[i])
        }
        particularities.value = newVal
    }

    //At initializing object
    fun poiEnumToBoolConverter() {
        val capVal = poi.value
        val newVal = mutableListOf(false, false, false, false, false, false, false, false, false)
        if (capVal!!.isNotEmpty()) {
            for (i in capVal) {
                val j = namedPoi.indexOf(i)
                newVal[j] = true
            }
        }
        boolPoi.value = newVal
    }

    // at saving object
    fun poiBoolToEnumConverter() {
        val capVal = boolPoi.value
        val newVal: MutableList<POI> = mutableListOf()
        for (i in 0..capVal!!.lastIndex) {
            if (capVal[i]) newVal.add(namedPoi[i])
        }
        poi.value = newVal
    }
}