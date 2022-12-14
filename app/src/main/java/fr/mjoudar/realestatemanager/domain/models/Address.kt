package fr.mjoudar.realestatemanager.domain.models

import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import fr.mjoudar.realestatemanager.db.entities.AddressEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class Address(
    @get:Bindable
    val vicinity: String? = "",
    @get:Bindable
    val complement: String? = "",
    @get:Bindable
    val zipcode: String? = "",
    @get:Bindable
    val city: String? = "",
    @get:Bindable
    val state: String? = "",
    @get:Bindable
    val country: String? = "",
    @get:Bindable
    var lat: Double? = 0.0,
    @get:Bindable
    var lng: Double? = 0.0
        ) : Parcelable, BaseObservable() {

    val isEmpty
        get() = vicinity!!.isEmpty() || city!!.isEmpty() || country!!.isEmpty()

    override fun toString(): String {
        val address = buildString {
            append("$vicinity,\n")
            if (complement!!.isNotEmpty()) append("$complement,\n")
            if (zipcode!!.isNotEmpty()) append("$zipcode, ")
            append("$city, \n")
            if (state!!.isNotEmpty()) append("$state, ")
            append("$country.")
        }
        return address
    }

    fun toEntity(offer_id: String) : AddressEntity {
        return AddressEntity(
            offer_id = offer_id,
            vicinity = vicinity,
            complement = complement,
            zipcode = zipcode,
            city = city,
            state = state,
            country = country,
            lat = lat,
            lng = lng
        )
    }

        }
