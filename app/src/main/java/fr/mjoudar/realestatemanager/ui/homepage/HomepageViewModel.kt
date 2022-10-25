package fr.mjoudar.realestatemanager.ui.homepage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.mjoudar.realestatemanager.domain.models.Agent
import fr.mjoudar.realestatemanager.repositories.AgentRepository
import fr.mjoudar.realestatemanager.repositories.OfferRepository
import fr.mjoudar.realestatemanager.utils.DataState
import fr.mjoudar.realestatemanager.utils.DatabaseDemoDataGenerator
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomepageViewModel @Inject constructor(
    private val agentRepository: AgentRepository,
    private val offerRepository: OfferRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<DataState<List<Agent>>>(DataState.loading(null))
    val state: StateFlow<DataState<List<Agent>>>
        get() = _state

    private val demoData = DatabaseDemoDataGenerator()
    var isCurrencyEuro = false

    /**********************************************************************************************
     ** Sample data initialization
     **********************************************************************************************/

    fun generateDemoData() {
        viewModelScope.launch {
            agentRepository.saveAgent(demoData.agent1)
            agentRepository.saveAgent(demoData.agent2)
            agentRepository.saveAgent(demoData.agent3)
            offerRepository.saveOffer(demoData.offer1)
            offerRepository.saveOffer(demoData.offer2)
            offerRepository.saveOffer(demoData.offer3)
            offerRepository.saveOffer(demoData.offer4)
            offerRepository.saveOffer(demoData.offer5)
            offerRepository.saveOffer(demoData.offer6)
            offerRepository.saveOffer(demoData.offer7)
            offerRepository.saveOffer(demoData.offer8)
            offerRepository.saveOffer(demoData.offer9)
            offerRepository.saveOffer(demoData.offer10)
            offerRepository.saveOffer(demoData.offer11)
            offerRepository.saveOffer(demoData.offer12)
        }
    }

    /**********************************************************************************************
     ** Currency
     **********************************************************************************************/

    fun toggleCurrency() {
        isCurrencyEuro = !isCurrencyEuro
    }

    /**********************************************************************************************
     ** Fetch agents
     **********************************************************************************************/
    fun fetchAgents() {
        viewModelScope.launch {
            agentRepository.getAllAgents()
                .catch { e ->
                    _state.value = (DataState.error(e.toString(), null))
                }
                .collectLatest {
                    _state.value = DataState.success(it)
                }
        }
    }
}