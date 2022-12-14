package fr.mjoudar.realestatemanager.ui.addEditOffer

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.mjoudar.realestatemanager.domain.models.*
import fr.mjoudar.realestatemanager.repositories.AgentRepository
import fr.mjoudar.realestatemanager.repositories.OfferRepository
import fr.mjoudar.realestatemanager.utils.GeocodeUtils
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddEditOfferViewModel @Inject constructor(
    private val agentRepository: AgentRepository,
    private val offerRepository: OfferRepository,
    application: Application,
) : AndroidViewModel(application)  {

    // Bundle args
    var offer: Offer? = null
    // Indicators observed by the fragment
    var isNewOffer = true
    var isOfferSaved = MutableLiveData(false)
    var isAgentListEmpty = MutableLiveData(false)
    var errorMessage: String? = null

    // Offer's data
    //Type
    val propertyType = MutableLiveData<String>("")
    val offerType = MutableLiveData<String>("")
    //Price
    val price = MutableLiveData<String>("")
    //Characteristics
    val surface = MutableLiveData<String>("")
    val rooms = MutableLiveData<String>("")
    val bathrooms = MutableLiveData<String>("")
    //Particularities
    val boolParticularities = MutableLiveData<MutableList<Boolean>>(mutableListOf(false, false, false, false, false, false, false, false, false, false))
    val namedParticularities = mutableListOf(Particularities.GARAGE, Particularities.PARKING_LOT, Particularities.BASEMENT, Particularities.BALCONY, Particularities.BACKYARD, Particularities.SWIMMING_POOL, Particularities.GYM_ROOM, Particularities.GARDEN, Particularities.JACUZZI, Particularities.SAUNA)
    var particularities: MutableList<Particularities> = mutableListOf()
    //Description
    val description = MutableLiveData<String>("")
    //Address
    val address = MutableLiveData<String?>("")
    val complement = MutableLiveData<String?>("")
    val city = MutableLiveData<String?>("")
    val zipCode = MutableLiveData<String?>("")
    val state = MutableLiveData<String?>("")
    val country = MutableLiveData<String?>("")
    private var addressLat: Double? = 0.0
    private var addressLng: Double? = 0.0
    //Poi
    val boolPoi = MutableLiveData<MutableList<Boolean>>(mutableListOf(false, false, false, false, false, false, false, false, false))
    val namedPoi = mutableListOf(POI.BUS_STATION, POI.MARKET_MALL, POI.MEDICAL_CENTER, POI.SPORT_CENTER, POI.CULTURAL_CENTER, POI.SCHOOL, POI.PARK, POI.BAR_COFFEESHOP, POI.RESTAURANT)
    var poi: MutableList<POI> = mutableListOf()
    //agent
    var agentList = MutableLiveData<List<Agent>>(mutableListOf())
    val agent = MutableLiveData<Agent>()
    //Dates
    val isOfferClosed = MutableLiveData<Boolean>(false)
    var publicationDate = MutableLiveData<Calendar>(Calendar.getInstance())
    var closureDate = MutableLiveData<Calendar>(Calendar.getInstance())
    //Photos
    var photos = MutableLiveData<MutableList<Photo>>(mutableListOf())

    init {
        fetchAgents()
    }

    /***********************************************************************************************
     ** Data retrieval
     ***********************************************************************************************/

    private fun fetchAgents() {
        viewModelScope.launch {
            agentRepository.getAllAgents()
                .catch { e ->
                    isAgentListEmpty.value = true
                    errorMessage = e.toString()
                }
                .collectLatest {
                    if (it.isEmpty()) isAgentListEmpty.value = true
                    else agentList.value = it
                }
        }
    }

    private fun fetchAgentById(id: String?) {
        id?.let { it1 ->
            viewModelScope.launch {
                agentRepository.getAgentById(it1)
                    .catch { e ->
                        errorMessage = e.toString()
                    }
                    .collectLatest { it2 ->
                        agent.value = it2
                    }
            }
        }
    }

    /***********************************************************************************************
     ** Data loading
     ***********************************************************************************************/
    fun loadData(data: Offer?) {
        data?.let {
            isNewOffer = false
            offer = data
            propertyType.value = it.propertyType.toString()
            offerType.value = it.offerType.toString()
            price.value = it.price.toString()
            surface.value = it.surface.toString()
            rooms.value = it.rooms.toString()
            bathrooms.value = it.bathrooms.toString()
            particularities = it.particularities
            description.value = it.description
            address.value = it.address?.vicinity
            complement.value = it.address?.complement
            city.value = it.address?.city
            zipCode.value = it.address?.zipcode
            state.value = it.address?.state
            country.value = it.address?.country
            addressLat = it.address!!.lat
            addressLng = it.address!!.lng
            poi = it.poi
            isOfferClosed.value = !(it.availability!!)
            publicationDate.value?.timeInMillis ?:  it.publicationDate
            closureDate.value?.timeInMillis ?:  it.closureDate
            photos.value = it.photos

            boolParticularities.value = fromEnumListToBoolList(particularities, namedParticularities, boolParticularities.value!!)
            boolPoi.value = fromEnumListToBoolList(poi, namedPoi, boolPoi.value!!)
            fetchAgentById(it.agentId)
        }
    }

    private fun fromEnumListToBoolList(list: MutableList<*>, namesList: MutableList<*>, boolList: MutableList<Boolean> ) : MutableList<Boolean> {
        val bools = boolList
        for (i in list) {
            for (j in namesList) {
                if (j == i) {
                    bools[namesList.indexOf(j)] = true
                }
            }
        }
        return boolList
    }

    /***********************************************************************************************
     ** Save offer
     ***********************************************************************************************/

    fun handleOfferCreation(data: LatLng) {
        viewModelScope.launch {
            setLatLng(data)
            particularitiesConverter()
            poiConverter()
            createOfferOnDatabase(buildOffer())
        }
    }

    private fun setLatLng(data: LatLng) {
        addressLat = data.latitude//.toString().toDoubleOrNull()
        addressLng = data.longitude//.toString().toDoubleOrNull()
    }

    private fun particularitiesConverter() {
        val list: MutableList<Particularities> = mutableListOf()
        val boolList = boolParticularities.value!!
        for (i in 0 until boolList.size) {
            if (boolList[i]) list.add(namedParticularities[i])
        }
        particularities = list
    }

    private fun poiConverter() {
        val list: MutableList<POI> = mutableListOf()
        val boolList = boolPoi.value!!
        for (i in 0 until boolList.size) {
            if (boolList[i]) list.add(namedPoi[i])
        }
        poi = list
    }

    private fun createAddress(): Address {
        return Address(address.value, complement.value, zipCode.value, city.value, state.value, country.value, addressLat, addressLng)
    }

    private fun buildOffer(): Offer {
        val addressObject = createAddress()
        val offerId: String = if (isNewOffer) UUID.randomUUID().toString() else offer!!.id
        val newOffer = Offer(
            offerId,
            PropertyType.valueOf(propertyType.value!!),
            OfferType.valueOf(offerType.value!!),
            !isOfferClosed.value!!,
            price.value!!.toLong(),
            surface.value!!.toInt(),
            rooms.value!!.toInt(),
            bathrooms.value!!.toInt(),
            particularities,
            description.value,
            photos.value!!,
            addressObject,
            poi,
            agent.value?.id,
            publicationDate.value?.timeInMillis,
            if (isOfferClosed.value!!) closureDate.value?.timeInMillis else null
        )
        offer = newOffer
        return newOffer
    }

    private fun createOfferOnDatabase(offer : Offer?) {
        offer?.let {
            viewModelScope.launch {
                offerRepository.saveOffer(it)
                isOfferSaved.value = true
            }
        }
    }

    private fun updateOfferOnDatabase(offer : Offer?) {
        offer?.let {
            viewModelScope.launch {
                offerRepository.updateOffer(it)
                isOfferSaved.value = true
            }
        }
    }

    fun deleteOffer() {
        // Not required
    }

}