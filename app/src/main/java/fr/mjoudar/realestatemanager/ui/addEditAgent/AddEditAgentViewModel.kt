package fr.mjoudar.realestatemanager.ui.addEditAgent

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.mjoudar.realestatemanager.domain.models.Agent
import fr.mjoudar.realestatemanager.repositories.AgentRepository
import fr.mjoudar.realestatemanager.utils.DataState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class AddEditAgentViewModel @Inject constructor(
    private val agentRepository: AgentRepository
) : ViewModel() {
//    private val _state = MutableStateFlow<DataState<Agent>>(DataState.loading(null))
//    val state: StateFlow<DataState<Agent>>
//        get() = _state

    var isNewAgent = MutableLiveData(true)
    //var agent: Agent? = null
    var agentList = MutableLiveData<List<Agent>>(mutableListOf())
    val agent = MutableLiveData<Agent>(null)
    var errorMessage = MutableLiveData<String> ("")

    var inputIncomplete = MutableLiveData<Boolean>(false)
    var isAgentSaved = MutableLiveData<Boolean>(false)

    val avatar = MutableLiveData<String>("")
    val name = MutableLiveData<String>("")
    val email = MutableLiveData<String>("")
    val phone = MutableLiveData<String>("")

    /***********************************************************************************************
     ** Initialization
     ***********************************************************************************************/
    init {
        fetchAgents()
    }
    private fun fetchAgents() {
        viewModelScope.launch {
            agentRepository.getAllAgents()
                .catch { e -> errorMessage.value = e.toString() }
                .collectLatest { agentList.value = it }
        }
    }

    /***********************************************************************************************
     ** Data loading
     ***********************************************************************************************/
    fun loadAgent() {
        isNewAgent.value = false
        avatar.value = agent.value!!.avatar
        name.value = agent.value!!.name
        email.value = agent.value!!.email
        phone.value = agent.value!!.phone
    }

    /***********************************************************************************************
     ** Save agent
     ***********************************************************************************************/
    fun saveAgent() {
        when (checkInput()) {
            true -> {
                var id = ""
                var defaultAvatar = "file:///android_asset/Agents/agent_avatar_3.png"
                when (isNewAgent.value!!) {
                    true -> id = UUID.randomUUID().toString()
                    false -> {
                        id = agent.value!!.id
                        defaultAvatar = agent.value!!.avatar!!
                    }
                }
                val newAgent = Agent(id, name.value, defaultAvatar, email.value, phone.value)
                createAgent(newAgent)
            }
            false -> inputIncomplete.value = true
        }
    }

    private fun checkInput(): Boolean {
        return (name.value!!.isNotEmpty() && checkEmailValidity(email.value!!) && email.value!!.isNotEmpty())
    }

    private fun checkEmailValidity(input: String): Boolean {
        return (input.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(input).matches())
    }

    private fun createAgent(agent : Agent?) {
        agent?.let {
            viewModelScope.launch {
                agentRepository.saveAgent(it)
                isAgentSaved.value = true
            }
        }
    }

    fun deleteAgent() {
        // Not required
    }

}