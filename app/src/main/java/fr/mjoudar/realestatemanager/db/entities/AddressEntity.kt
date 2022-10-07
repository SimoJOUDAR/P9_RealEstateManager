package fr.mjoudar.realestatemanager.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import fr.mjoudar.realestatemanager.domain.models.Address

@Entity(
    tableName = "address"
)
data class AddressEntity(
    @PrimaryKey(autoGenerate = true)
    var address_id: Long? = null,
    @ColumnInfo(name = "property_id", index = true)
    var property_id: String?,
    val vicinity: String?,
    val complement: String?,
    val zipcode: String?,
    val city: String?,
    val state: String?,
    val country: String?,
    var lat: Double?,
    var lng: Double?
) {

    fun toModel() : Address {
        return Address(
            vicinity,
            complement,
            zipcode,
            city,
            state,
            country,
            lat,
            lng
        )
    }

}