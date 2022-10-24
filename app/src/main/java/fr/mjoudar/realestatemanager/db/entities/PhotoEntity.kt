package fr.mjoudar.realestatemanager.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import fr.mjoudar.realestatemanager.domain.models.Photo

@Entity(
    tableName = "photo",
    foreignKeys = [ForeignKey(
        entity = OfferEntity::class,
        parentColumns = ["id"],
        childColumns = ["offer_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class PhotoEntity(
    @PrimaryKey(autoGenerate = false)
    var photo_id: String,
    @ColumnInfo(name = "offer_id", index = true)
    var offer_id: String?,
    var uri: String,
    var description: String
) {
    fun toModel(): Photo {
        return Photo(
            photo_id,
            uri,
            description
        )
    }
}