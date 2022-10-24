package fr.mjoudar.realestatemanager.domain.models

import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import fr.mjoudar.realestatemanager.db.entities.PhotoEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Photo (
    var id: String,
    @get:Bindable
    var uri: String,
    @get:Bindable
    var description: String
    ) : Parcelable, BaseObservable() {

    fun toEntity(offer_id: String?): PhotoEntity {
        return  PhotoEntity(
            photo_id = id,
            offer_id = offer_id,
            uri = uri,
            description = description
        )
    }

    }