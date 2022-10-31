package fr.mjoudar.realestatemanager.db.relationships

import androidx.room.Embedded
import androidx.room.Relation
import fr.mjoudar.realestatemanager.db.entities.AddressEntity
import fr.mjoudar.realestatemanager.db.entities.AgentEntity
import fr.mjoudar.realestatemanager.db.entities.OfferEntity
import fr.mjoudar.realestatemanager.db.entities.PhotoEntity
import fr.mjoudar.realestatemanager.domain.models.Offer

data class OfferEntityAggregate(
    @Embedded
    val offerEntity: OfferEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "offer_id"
    )
    val photoEntities: List<PhotoEntity> = arrayListOf(),
    @Relation(
        parentColumn = "id",
        entityColumn = "offer_id"
    )
    val addressEntity: AddressEntity
) {
    fun toModel() : Offer {
        return Offer(
            _id= offerEntity.id,
            _propertyType = offerEntity.property_type,
            _offerType = offerEntity.offer_type,
            _availability = offerEntity.availability,
            _price = offerEntity.price,
            _surface = offerEntity.surface,
            _rooms = offerEntity.rooms,
            _bathrooms = offerEntity.bathrooms,
            _particularities = offerEntity.particularities.toMutableList(),
            _description = offerEntity.description,
            _photos = photoEntities.map { it.toModel() }.toMutableList(),
            _address = addressEntity.toModel(),
            _poi = offerEntity.poi.toMutableList(),
            _agentId = offerEntity.agent_id,
            _publicationDate = offerEntity.publication_date,
            _closureDate = offerEntity.closure_date,
        )
    }
}