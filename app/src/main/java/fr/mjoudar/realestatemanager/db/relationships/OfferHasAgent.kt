package fr.mjoudar.realestatemanager.db.relationships

import androidx.room.Embedded
import androidx.room.Relation
import fr.mjoudar.realestatemanager.db.entities.AgentEntity
import fr.mjoudar.realestatemanager.db.entities.OfferEntity

data class OfferHasAgent(
    @Embedded
    val agentEntity: AgentEntity,
    @Relation (
        parentColumn = "id",
        entityColumn = "agent_id"
            )
    val offerEntity: OfferEntity
        )