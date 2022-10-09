package fr.mjoudar.realestatemanager.repositories

import fr.mjoudar.realestatemanager.db.dao.AgentDao
import fr.mjoudar.realestatemanager.domain.models.Agent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AgentRepository @Inject constructor(
    private val agentDao: AgentDao
    ) {

    suspend fun saveAgent(agent: Agent) {
        return agentDao.insertAgent(agent.toEntity())
    }

    fun getAllAgents(): Flow<List<Agent>> {
        return agentDao.getAllAgents()
            .distinctUntilChanged()
            .map { agentEntityList ->
                agentEntityList.map {
                    it.toModel()
                }
            }
    }

    fun getAgentById(agentId: String): Flow<Agent> {
        return agentDao.getAgentById(agentId)
            .distinctUntilChanged()
            .map {
                it.toModel()
            }
    }

    suspend fun updateAgent(agent: Agent) {
        return agentDao.updateAgent(agent.toEntity())
    }
}