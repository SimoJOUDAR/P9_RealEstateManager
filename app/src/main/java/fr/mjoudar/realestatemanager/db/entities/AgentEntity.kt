package fr.mjoudar.realestatemanager.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import fr.mjoudar.realestatemanager.domain.models.Agent

@Entity(
    tableName = "agent"
)
data class AgentEntity(
    @PrimaryKey(autoGenerate = false)
    var id: String,
    var name: String?,
    var avatar: String?,
    var email: String?,
    var phone: String?
) {
    fun toModel(): Agent {
        return Agent(
            id,
            name,
            avatar,
            email,
            phone
        )
    }
}