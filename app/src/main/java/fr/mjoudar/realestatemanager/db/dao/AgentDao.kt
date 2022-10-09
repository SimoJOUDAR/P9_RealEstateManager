package fr.mjoudar.realestatemanager.db.dao

import android.database.Cursor
import androidx.room.*
import fr.mjoudar.realestatemanager.db.entities.AgentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AgentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAgent(agent: AgentEntity)

    @Query("SELECT * FROM agent ORDER BY name DESC")
    fun getAllAgents(): Flow<List<AgentEntity>>

    @Query("SELECT * FROM agent ORDER BY name DESC")
    fun getAllAgentsWithCursor(): Cursor

    @Query("SELECT * FROM agent WHERE id = :id")
    fun getAgentById(id: String): Flow<AgentEntity>

    @Query("SELECT * FROM agent WHERE id = :id")
    fun getAgentByIdWithCursor(id: String): Cursor

    @Transaction
    @Query("SELECT COUNT(*)  from agent")
    fun getAgentsCount(): Int

    @Transaction
    @Query("SELECT COUNT(*)  from agent")
    fun getAgentsCountWithCursor(): Cursor

    @Update
    suspend fun updateAgent(agent: AgentEntity)
}