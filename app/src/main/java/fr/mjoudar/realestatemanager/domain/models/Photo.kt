package fr.mjoudar.realestatemanager.domain.models

import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Photo (
    var id: String,
    @get:Bindable
    var uri: String,
    @get:Bindable
    var description: String,
    ) : Parcelable, BaseObservable()