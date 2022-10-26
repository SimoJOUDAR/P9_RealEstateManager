package fr.mjoudar.realestatemanager.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.mjoudar.realestatemanager.domain.models.Agent
import fr.mjoudar.realestatemanager.domain.models.Offer
import fr.mjoudar.realestatemanager.repositories.AgentRepository
import fr.mjoudar.realestatemanager.repositories.OfferRepository
import fr.mjoudar.realestatemanager.utils.DataState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OfferDetailsViewModel @Inject constructor(
    private val agentRepository: AgentRepository
) : ViewModel() {

    private val _agentState = MutableStateFlow<DataState<Agent>>(DataState.loading(null))
    val agentState: StateFlow<DataState<Agent>>
        get() = _agentState
    private val _offerState = MutableStateFlow<DataState<Offer>>(DataState.loading(null))
    val offerState: StateFlow<DataState<Offer>> get() = _offerState

    fun getAgent(agentId: String?) {
        viewModelScope.launch {
            agentId?.let { it ->
                agentRepository.getAgentById(it)
                    .catch { e ->
                        _agentState.value = (DataState.error(e.toString(), null))
                    }
                    .collectLatest {
                        _agentState.value = DataState.success(it)
                    }
            }
        }
    }

}