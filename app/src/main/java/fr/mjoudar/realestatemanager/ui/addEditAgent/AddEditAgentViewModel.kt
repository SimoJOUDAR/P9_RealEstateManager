package fr.mjoudar.realestatemanager.ui.addEditAgent

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.mjoudar.realestatemanager.domain.models.Agent
import fr.mjoudar.realestatemanager.repositories.AgentRepository
import fr.mjoudar.realestatemanager.utils.DataState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddEditAgentViewModel @Inject constructor(
    private val agentRepository: AgentRepository
) : ViewModel() {
    private val _state = MutableStateFlow<DataState<Agent>>(DataState.loading(null))
    val state: StateFlow<DataState<Agent>>
        get() = _state

    var agent: Agent? = null
    var isNewAgent = true
    var inputIncomplete = MutableLiveData<Boolean>(false) // TODO: implement observer in fragment for incorrect input handling
    var isAgentSaved = MutableLiveData<Boolean>(false) // TODO: implement observer in fragment for success notification

    val avatar = MutableLiveData<String>()
    val name = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val phone = MutableLiveData<String>()


    private fun checkInput(): Boolean {
        //return (avatar.value != null && name.value != null && email.value != null && phone.value != null)
        return (name.value != null && email.value != null && phone.value != null)
    }
    fun saveAgent() {
        Timber.tag("saveAgent").d("clicked")
        when (checkInput()) {
            true -> {
                Timber.tag("saveAgent").d("input not null")
                when (isNewAgent) {
                    true -> {
                        Timber.tag("saveAgent").d("new agent")
                        //agent = Agent(UUID.randomUUID().toString(), name.value, avatar.value, email.value, phone.value)
                        agent = Agent(UUID.randomUUID().toString(), name.value, "file:///android_asset/Agents/agent_avatar_3.png", email.value, phone.value)
                        createAgent(agent)
                    }
                    else -> {
                        Timber.tag("saveAgent").d("update agent")
                        agent!!.name = name.value
                        agent!!.avatar = avatar.value
                        agent!!.email = email.value
                        agent!!.phone = phone.value
                        updateAgent(agent)
                    }
                }
            }
            false -> {
                Timber.tag("saveAgent").d("input null")
                inputIncomplete.value = true
            }
        }
    }

    private fun createAgent(agent : Agent?) {
        agent?.let {
            viewModelScope.launch {
                agentRepository.saveAgent(it)
                isAgentSaved.value = true
            }
        }
    }

    private fun updateAgent(agent : Agent?) {
        agent?.let {
            viewModelScope.launch {
                agentRepository.updateAgent(it)
                isAgentSaved.value = true
            }
        }
    }

}