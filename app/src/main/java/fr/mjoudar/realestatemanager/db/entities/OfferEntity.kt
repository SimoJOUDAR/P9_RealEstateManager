package fr.mjoudar.realestatemanager.db.entities

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import fr.mjoudar.realestatemanager.domain.models.*
import java.time.LocalDate

@Entity(
    tableName = "offer",
    foreignKeys = [ForeignKey(
        entity = AgentEntity::class,
        parentColumns = ["id"],
        childColumns = ["agent_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class OfferEntity(
    @PrimaryKey(autoGenerate = false)
    @NonNull val id: String,
    var property_type: PropertyType?,
    var offer_type: OfferType?,
    var availability: Boolean?,
    var price: Long?,
    var surface: Int?,
    var rooms: Int?,
    var bathrooms: Int?,
    var particularities: List<Particularities> = arrayListOf(),
    var description: String?,
    var main_photo_id: String?,
    var poi: List<POI> = arrayListOf(),
    var agent_id: String?,
    var publication_date: Long?,
    var closure_date: Long?,
    var static_map: String?
)