package fr.mjoudar.realestatemanager.ui.homepage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.mjoudar.realestatemanager.domain.models.Agent
import fr.mjoudar.realestatemanager.domain.models.Offer
import fr.mjoudar.realestatemanager.domain.models.OffersFilter
import fr.mjoudar.realestatemanager.repositories.AgentRepository
import fr.mjoudar.realestatemanager.repositories.OfferRepository
import fr.mjoudar.realestatemanager.utils.DataState
import fr.mjoudar.realestatemanager.utils.DatabaseDemoDataGenerator
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomepageViewModel @Inject constructor(
    private val agentRepository: AgentRepository,
    private val offerRepository: OfferRepository,
) : ViewModel() {

    private val _agentsState = MutableStateFlow<DataState<List<Agent>>>(DataState.loading(null))
    val agentsState: StateFlow<DataState<List<Agent>>>
        get() = _agentsState

    private val _offersState = MutableStateFlow<DataState<List<Offer>>>(DataState.loading(null))
    val offersState: StateFlow<DataState<List<Offer>>>
        get() = _offersState

    val dataActualized = MutableLiveData<Boolean>(false)
    var dataFiltered = false

    //var isCurrencyEuro = false
    var isCurrencyEuro = MutableLiveData<Boolean>(false)

    init {
        isCurrencyEuro.value = false
        fetchAgents()
    }

    /**********************************************************************************************
     ** Sample data initialization
     **********************************************************************************************/

    fun generateDemoData() {
        viewModelScope.launch {
            agentRepository.saveAgent(DatabaseDemoDataGenerator.agent1)
            agentRepository.saveAgent(DatabaseDemoDataGenerator.agent2)
            agentRepository.saveAgent(DatabaseDemoDataGenerator.agent3)
            offerRepository.saveOffer(DatabaseDemoDataGenerator.offer1)
            offerRepository.saveOffer(DatabaseDemoDataGenerator.offer2)
            offerRepository.saveOffer(DatabaseDemoDataGenerator.offer3)
            offerRepository.saveOffer(DatabaseDemoDataGenerator.offer4)
            offerRepository.saveOffer(DatabaseDemoDataGenerator.offer5)
            offerRepository.saveOffer(DatabaseDemoDataGenerator.offer6)
            offerRepository.saveOffer(DatabaseDemoDataGenerator.offer7)
            offerRepository.saveOffer(DatabaseDemoDataGenerator.offer8)
            offerRepository.saveOffer(DatabaseDemoDataGenerator.offer9)
            offerRepository.saveOffer(DatabaseDemoDataGenerator.offer10)
            offerRepository.saveOffer(DatabaseDemoDataGenerator.offer11)
            offerRepository.saveOffer(DatabaseDemoDataGenerator.offer12)
        }
    }

    /**********************************************************************************************
     ** Currency
     **********************************************************************************************/

    fun toggleCurrency() {
        isCurrencyEuro.value = !isCurrencyEuro.value!!
    }

    /**********************************************************************************************
     ** Data fetching
     **********************************************************************************************/
    private fun fetchAgents() {
        viewModelScope.launch {
            agentRepository.getAllAgents()
                .catch { e ->
                    _agentsState.value = (DataState.error(e.toString(), null))
                }
                .collectLatest {
                    _agentsState.value = DataState.success(it)
                    if (it.isNotEmpty()) fetchOffers()
                }
        }
    }

    fun fetchOffers() {
        viewModelScope.launch {
            offerRepository.getAllOffers()
                .catch { e ->
                    _offersState.value = (DataState.error(e.toString(), null))
                }
                .collectLatest {
                    _offersState.value = DataState.success(it)
                    dataFiltered = false
                    dataActualized.value = true
                }
        }
    }

    /**********************************************************************************************
     ** Filtering
     **********************************************************************************************/
    fun getFilteredOfferList(offersFilter: OffersFilter) {
        viewModelScope.launch {
            offerRepository.getFilteredOffers(offersFilter)
                .catch { e ->
                    _offersState.value = (DataState.error(e.toString(), null))
                }
                .collectLatest {
                    val list = it.toSet().toList() // Remove duplicates
                    _offersState.value = DataState.success(list)
                    Timber.tag("Filter").d("Filter results = ${it.size} Vr. Cleaned results = ${list.size}")
                    dataFiltered = true
                    dataActualized.value = true
                }
        }
    }
}