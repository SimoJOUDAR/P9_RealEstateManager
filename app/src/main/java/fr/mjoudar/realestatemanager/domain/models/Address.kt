package fr.mjoudar.realestatemanager.domain.models

import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Address(
    val id: String,
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
        }
