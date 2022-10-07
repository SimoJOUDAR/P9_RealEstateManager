package fr.mjoudar.realestatemanager.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import fr.mjoudar.realestatemanager.domain.models.*
import java.time.LocalDate

@Entity(
    tableName = "offers",
    foreignKeys = [ForeignKey(
        entity = AgentEntity::class,
        parentColumns = ["id"],
        childColumns = ["agent_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class OfferEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String?,
    var propertyType: PropertyType?,
    var offerType: OfferType?,
    var availability: Boolean?,
    var price: Long?,
    var surface: Int?,
    var rooms: Int?,
    var bathrooms: Int?,
    var particularities: MutableList<Particularities>?,
    var description: String?,
    var mainPhoto_id: Long?,
    var poi: MutableList<POI>?,
    var agent_id: String?,
    var publication_date: LocalDate?,
    var closure_date: LocalDate?,
    var static_map: String?
) {

    fun toModel(photosEntity: MutableList<PhotoEntity>, mainPhotoEntity: PhotoEntity, addressEntity: AddressEntity, agentEntity: AgentEntity) : Offer {
        return Offer(
            _id= id,
            _propertyType = propertyType,
            _offerType = offerType,
            _availability = availability,
            _price = price,
            _surface = surface,
            _rooms = rooms,
            _bathrooms = bathrooms,
            _particularities = particularities,
            _description = description,
            _photos = photosEntity.map { it.toModel() }.toMutableList(),
            _mainPhoto = mainPhotoEntity.toModel(),
            _address = addressEntity.toModel(),
            _poi = poi,
            _agent = agentEntity.toModel(),
            _publicationDate = publication_date,
            _closureDate = closure_date,
            _staticMap = static_map
        )
    }

}