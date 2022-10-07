package fr.mjoudar.realestatemanager.domain.models

import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import kotlinx.android.parcel.Parcelize
import fr.mjoudar.realestatemanager.BR
import fr.mjoudar.realestatemanager.db.entities.AgentEntity

@Parcelize
data class Agent (
    var _id: String,
    var _name: String? = "",
    var _avatar: String? = "",
    var _email: String? = "",
    var _phone: String? = ""
    ):  Parcelable, BaseObservable() {

    val id: String
        get() = _id

    @get:Bindable
    var name
        get() = _name
        set(value) {
            _name = value
            notifyPropertyChanged(BR.name)
        }

    @get:Bindable
    var avatar
        get() = _avatar
        set(value) {
            _avatar = value
            notifyPropertyChanged(BR.avatar)
        }

    @get:Bindable
    var email
        get() = _email
        set(value) {
            _email = value
            notifyPropertyChanged(BR.email)
        }

    @get:Bindable
    var phone
        get() = _phone
        set(value) {
            _phone = value
            notifyPropertyChanged(BR.phone)
        }

    val isEmpty
        get() = name!!.isEmpty() || email!!.isEmpty() || phone!!.isEmpty()

    override fun toString(): String {
        return name!!
    }

    fun toEntity(): AgentEntity {
        return AgentEntity(
            id,
            name,
            avatar,
            email,
            phone
        )
    }
}