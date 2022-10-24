package fr.mjoudar.realestatemanager.ui.filter

import androidx.lifecycle.MutableLiveData
import fr.mjoudar.realestatemanager.domain.models.*

class FilterDialogViewModel {

    //Type
    val boolPropertyTypes = MutableLiveData<MutableList<Boolean>>(mutableListOf(false, false, false, false))
    val namedPropertyTypes = mutableListOf(PropertyType.HOUSE, PropertyType.APARTMENT, PropertyType.DUPLEX, PropertyType.MANSION)
    val propertyTypes = MutableLiveData<MutableList<PropertyType>>(mutableListOf())
    val boolOfferTypes = MutableLiveData<MutableList<Boolean>>(mutableListOf(false, false))
    val namedOfferTypes = mutableListOf(OfferType.SALE, OfferType.RENT)
    val offerTypes = MutableLiveData<MutableList<OfferType>>(mutableListOf())
    val available = MutableLiveData<Boolean?>(true)
    val closed = MutableLiveData<Boolean?>(false)
    //Characteristics
    var minPrice= MutableLiveData<String>("")
    var maxPrice= MutableLiveData<String>("")
    var minSurface= MutableLiveData<String>("")
    var maxSurface= MutableLiveData<String>("")
    var minRooms= MutableLiveData<String>("")
    var maxRooms= MutableLiveData<String>("")
    var minBathrooms= MutableLiveData<String>("")
    var maxBathrooms= MutableLiveData<String>("")
    //Particularities
    val boolParticularities = MutableLiveData<MutableList<Boolean>>(mutableListOf(false, false, false, false, false, false, false, false, false, false))
    val namedParticularities = mutableListOf(Particularities.GARAGE, Particularities.PARKING_LOT, Particularities.BASEMENT, Particularities.BALCONY, Particularities.BACKYARD, Particularities.SWIMMING_POOL, Particularities.GYM_ROOM, Particularities.GARDEN, Particularities.JACUZZI, Particularities.SAUNA)
    val particularities = MutableLiveData<MutableList<Particularities>>(mutableListOf())
    var withPictures = MutableLiveData<Boolean>(false)
    //Location
    val city = MutableLiveData<String?>("")
    val state = MutableLiveData<String?>("")
    val country = MutableLiveData<String?>("")
    //Poi
    val boolPoi = MutableLiveData<MutableList<Boolean>>(mutableListOf(false, false, false, false, false, false, false, false, false))
    val namedPoi = mutableListOf(POI.BUS_STATION, POI.MARKET_MALL, POI.MEDICAL_CENTER, POI.SPORT_CENTER, POI.CULTURAL_CENTER, POI.SCHOOL, POI.PARK, POI.BAR_COFFEESHOP, POI.RESTAURANT)
    val poi = MutableLiveData<MutableList<POI>>(mutableListOf())
    //Agent
    val agent = MutableLiveData<Agent>()
    val agentList: MutableList<Agent> = mutableListOf()
    var agentId = MutableLiveData<String?>("")
    //Dates
    var stringPublicationDateFrom = MutableLiveData<String>("")
    var stringPublicationDateTo = MutableLiveData<String>("")
    var stringClosureDateFrom = MutableLiveData<String>("")
    var stringClosureDateTo = MutableLiveData<String>("")
    var publicationDateFrom: Long? = null
    var publicationDateTo: Long? = null
    var closureDateFrom: Long? = null
    var closureDateTo: Long? = null

    //Radio group
//    var _sorting: Sorting? = Sorting.PUBLICATION_DATE_DESC
    val boolSorting = MutableLiveData<MutableList<Boolean>>(mutableListOf(false, false, false, false, false, false, false, false))

}