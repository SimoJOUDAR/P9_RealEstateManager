package fr.mjoudar.realestatemanager.ui.filter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.mjoudar.realestatemanager.domain.models.*
import fr.mjoudar.realestatemanager.utils.DateUtils
import okhttp3.internal.toLongOrDefault
import timber.log.Timber
import java.util.*

class FilterDialogViewModel : ViewModel() {

    // Indicators observed by the fragment
    val offersFilter = MutableLiveData<OffersFilter?>(null)

    //Type
    val boolPropertyTypes = MutableLiveData<MutableList<Boolean>>(mutableListOf(false, false, false, false))
    val namedPropertyTypes = mutableListOf(PropertyType.HOUSE, PropertyType.APARTMENT, PropertyType.DUPLEX, PropertyType.MANSION)
    val boolOfferTypes = MutableLiveData<MutableList<Boolean>>(mutableListOf(false, false))
    val namedOfferTypes = mutableListOf(OfferType.SALE, OfferType.RENT)
    val available = MutableLiveData<Boolean?>(true)
    val closed = MutableLiveData<Boolean?>(false)
    val all = MutableLiveData<Boolean?>(false)

    //Characteristics
    var minPrice= MutableLiveData<String>(null)
    var maxPrice= MutableLiveData<String>(null)
    var minSurface= MutableLiveData<String>(null)
    var maxSurface= MutableLiveData<String>(null)
    var minRooms= MutableLiveData<String>(null)
    var maxRooms= MutableLiveData<String>(null)
    var minBathrooms= MutableLiveData<String>(null)
    var maxBathrooms= MutableLiveData<String>(null)
    //Particularities
    val boolParticularities = MutableLiveData<MutableList<Boolean>>(mutableListOf(false, false, false, false, false, false, false, false, false, false))
    val namedParticularities = mutableListOf(Particularities.GARAGE, Particularities.PARKING_LOT, Particularities.BASEMENT, Particularities.BALCONY, Particularities.BACKYARD, Particularities.SWIMMING_POOL, Particularities.GYM_ROOM, Particularities.GARDEN, Particularities.JACUZZI, Particularities.SAUNA)
    //Location
    val city = MutableLiveData<String?>(null)
    val state = MutableLiveData<String?>(null)
    val country = MutableLiveData<String?>(null)
    //Poi
    val boolPoi = MutableLiveData<MutableList<Boolean>>(mutableListOf(false, false, false, false, false, false, false, false, false))
    val namedPoi = mutableListOf(POI.BUS_STATION, POI.MARKET_MALL, POI.MEDICAL_CENTER, POI.SPORT_CENTER, POI.CULTURAL_CENTER, POI.SCHOOL, POI.PARK, POI.BAR_COFFEESHOP, POI.RESTAURANT)
    //Agent
    val agent = MutableLiveData<Agent>(null)
    //Dates
    var publicationDateFrom = MutableLiveData<Calendar>(null)
    var publicationDateTo = MutableLiveData<Calendar>(null)
    var closureDateFrom = MutableLiveData<Calendar>(null)
    var closureDateTo = MutableLiveData<Calendar>(null)

    //Radio group
//    var _sorting: Sorting? = Sorting.PUBLICATION_DATE_DESC
    val boolSorting = MutableLiveData<MutableList<Boolean>>(mutableListOf(false, false, false, false, false, true, false, false))
    val namedSorting = mutableListOf(Sorting.PRICE_ASC, Sorting.PRICE_ASC, Sorting.PRICE_DESC, Sorting.SURFACE_ASC, Sorting.SURFACE_DESC, Sorting.PUBLICATION_DATE_ASC, Sorting.PUBLICATION_DATE_DESC, Sorting.CLOSURE_DATE_ASC, Sorting.CLOSURE_DATE_DESC)

    fun buildOfferFilter() {
        val filter = OffersFilter(
            propertyTypesConverter(),
            offerTypesConverter(),
            if (all.value!!) null else available.value,
            minPrice.value?.toLongOrNull(),
            maxPrice.value?.toLongOrNull(),
            minSurface.value?.toIntOrNull(),
            maxSurface.value?.toIntOrNull(),
            minRooms.value?.toIntOrNull(),
            maxRooms.value?.toIntOrNull(),
            minBathrooms.value?.toIntOrNull(),
            maxBathrooms.value?.toIntOrNull(),
            particularitiesConverter(),
            city.value,
            state.value,
            country.value,
            poiConverter(),
            agent.value?.id,
            publicationDateFrom.value?.timeInMillis,
            publicationDateTo.value?.timeInMillis,
            closureDateFrom.value?.timeInMillis,
            closureDateTo.value?.timeInMillis,
            sortingConverter()
        )
        offersFilter.value = filter
    }


    /***********************************************************************************************
     ** Converters
     ***********************************************************************************************/
    private fun propertyTypesConverter() : MutableList<PropertyType> {
        val propertyTypes: MutableList<PropertyType> = mutableListOf()
        val bools = boolPropertyTypes.value!!
        for (i in 0 until bools.size) {
            if (bools[i]) propertyTypes.add(namedPropertyTypes[i])
        }
        return propertyTypes
    }
    private fun offerTypesConverter() : MutableList<OfferType> {
        val offerTypes: MutableList<OfferType> = mutableListOf()
        val bools = boolOfferTypes.value!!
        for (i in 0 until bools.size) {
            if (bools[i]) offerTypes.add(namedOfferTypes[i])
        }
        return offerTypes
    }
    private fun particularitiesConverter() : MutableList<Particularities> {
        val particularities: MutableList<Particularities> = mutableListOf()
        val bools = boolParticularities.value!!
        for (i in 0 until bools.size) {
            if (bools[i]) particularities.add(namedParticularities[i])
        }
        return particularities
    }
    private fun poiConverter() : MutableList<POI> {
        val poi: MutableList<POI> = mutableListOf()
        val bools = boolPoi.value!!
        for (i in 0 until bools.size) {
            if (bools[i]) poi.add(namedPoi[i])
        }
        return poi
    }
    private fun sortingConverter() : Sorting? {
        val bools = boolSorting.value!!
        for (i in 0..bools.size) {
            if (bools[i]) return namedSorting[i]
        }
        return null
    }

    fun availability(bool: Boolean?) {
        available.value = bool
    }
}