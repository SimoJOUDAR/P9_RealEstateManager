package fr.mjoudar.realestatemanager

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import fr.mjoudar.realestatemanager.db.BusinessDatabase
import fr.mjoudar.realestatemanager.db.dao.AgentDao
import fr.mjoudar.realestatemanager.db.entities.AgentEntity
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.produceIn
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
@SmallTest
class AgentDaoTest {

    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var database: BusinessDatabase
    private lateinit var agentDao: AgentDao
    private val agent1 = AgentEntity(
        "1",
        "agent 1",
        "file:///android_asset/Agents/agent_avatar_1.png",
        "&gent1@rem.com",
        "+1 354-607-2343"
    )
    private val agent2 = AgentEntity(
        "2",
        "agent 2",
        "file:///android_asset/Agents/agent_avatar_2.png",
        "agent2@rem.com",
        "+1 354-607-3245"
    )

    @Before
    fun initDatabase() {
        database = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            BusinessDatabase::class.java
        ).build()
        agentDao = database.agentDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDatabase() {
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun should_get_empty_agent_list() = runBlocking {
        // The list is empty

        //Get first agent in the list
        val firstAgent = agentDao.getAllAgents().first()
        //First agent size should be 0
        assertEquals(0, firstAgent.size)
        closeDatabase()
    }

    @FlowPreview
    @Test
    @Throws(Exception::class)
    fun should_insert_and_retrieve_agents_correctly() = runBlocking {

        agentDao.insertAgent(agent1)
        agentDao.insertAgent(agent2)

        val agentList = mutableListOf<AgentEntity>(agent2, agent1)

        val channelFlow = agentDao.getAllAgents().produceIn(this)
        assertEquals(channelFlow.receive(), agentList)
        assertTrue(channelFlow.isEmpty)
        channelFlow.cancel()
        closeDatabase()
    }

    @Test
    @Throws(Exception::class)
    fun should_get_agent_by_id_correctly() = runBlocking {

        agentDao.insertAgent(agent1)
        agentDao.insertAgent(agent2)

        val agent1Dao = agentDao.getAgentById(agent1.id).first()
        assertEquals(agent1, agent1Dao)

        val agent2Dao = agentDao.getAgentById(agent2.id).first()
        assertEquals(agent2, agent2Dao)

        assertNotEquals(agent2, agent1Dao)

        closeDatabase()
    }
}