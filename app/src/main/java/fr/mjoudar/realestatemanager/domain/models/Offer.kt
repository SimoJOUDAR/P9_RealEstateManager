package fr.mjoudar.realestatemanager.domain.models

import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import java.time.LocalDate
import fr.mjoudar.realestatemanager.BR
import fr.mjoudar.realestatemanager.db.entities.OfferEntity
import fr.mjoudar.realestatemanager.db.relationships.OfferEntityAggregate
import kotlinx.parcelize.Parcelize

@Parcelize
data class Offer (
    val _id: String,
    var _propertyType: PropertyType? = PropertyType.HOUSE,
    var _offerType: OfferType? = OfferType.SALE,
    var _availability: Boolean? = false,
    var _price: Long? = 0,
    var _surface: Int? = 0,
    var _rooms: Int? = 0,
    var _bathrooms: Int? = 0,
    var _particularities: MutableList<Particularities> = mutableListOf(),
    var _description: String? = "",
    var _photos: MutableList<Photo> = mutableListOf(),
    var _mainPhotoId: String? = null,
    var _address: Address? = null,
    var _poi: MutableList<POI> = mutableListOf(),
    var _agentId: String? = null,
    var _publicationDate: Long? = null,
    var _closureDate: Long? = null,
    var _staticMap: String? = null
        ) : Parcelable, BaseObservable() {

    val id
        get() = _id

    @get:Bindable
    var propertyType
        get() = _propertyType
        set(value) {
            _propertyType = value
            notifyPropertyChanged(BR.propertyType)
        }

        @get:Bindable
    var offerType
        get() = _offerType
        set(value) {
            _offerType = value
            notifyPropertyChanged(BR.offerType)
        }

        @get:Bindable
    var availability
        get() = _availability
        set(value) {
            _availability = value
            notifyPropertyChanged(BR.availability)
        }

        @get:Bindable
    var price
        get() = _price
        set(value) {
            _price = value
            notifyPropertyChanged(BR.price)
        }

        @get:Bindable
    var surface
        get() = _surface
        set(value) {
            _surface = value
            notifyPropertyChanged(BR.surface)
        }

        @get:Bindable
    var rooms
        get() = _rooms
        set(value) {
            _rooms = value
            notifyPropertyChanged(BR.rooms)
        }

        @get:Bindable
    var bathrooms
        get() = _bathrooms
        set(value) {
            _bathrooms = value
            notifyPropertyChanged(BR.bathrooms)
        }

        @get:Bindable
    var particularities
        get() = _particularities
        set(value) {
            _particularities = value
            notifyPropertyChanged(BR.particularities)
        }

        @get:Bindable
    var description
        get() = _description
        set(value) {
            _description = value
            notifyPropertyChanged(BR.description)
        }

        @get:Bindable
    var photos
        get() = _photos
        set(value) {
            _photos = value
            notifyPropertyChanged(BR.photos)
        }

        @get:Bindable
    var mainPhotoId
        get() = _mainPhotoId
        set(value) {
            _mainPhotoId = value
            notifyPropertyChanged(BR.mainPhotoId)
        }

        @get:Bindable
    var address
        get() = _address
        set(value) {
            _address = value
            notifyPropertyChanged(BR.address)
        }

        @get:Bindable
    var poi
        get() = _poi
        set(value) {
            _poi = value
            notifyPropertyChanged(BR.poi)
        }

        @get:Bindable
    var agentId
        get() = _agentId
        set(value) {
            _agentId = value
            notifyPropertyChanged(BR.agentId)
        }

        @get:Bindable
    var publicationDate
        get() = _publicationDate
        set(value) {
            _publicationDate = value
            notifyPropertyChanged(BR.publicationDate)
        }

        @get:Bindable
    var closureDate
        get() = _closureDate
        set(value) {
            _closureDate = value
            notifyPropertyChanged(BR.closureDate)
        }

        @get:Bindable
    var staticMap
        get() = _staticMap
        set(value) {
            _staticMap = value
            notifyPropertyChanged(BR.staticMap)
        }

    val isEmpty
        get() =  propertyType?.equals(null) ?: true || offerType?.equals(null) ?: true
                || price == 0.toLong() || surface != 0 || rooms != 0 || bathrooms != 0
                || description!!.isEmpty() || address!!.isEmpty || agentId != null || publicationDate != null


    private fun toEntity() : OfferEntity {
        return OfferEntity(
            id,
            propertyType,
            offerType,
            availability,
            price,
            surface,
            rooms,
            bathrooms,
            particularities,
            description,
            mainPhotoId,
            poi,
            agentId,
            publicationDate,
            closureDate,
            staticMap
        )
    }

    fun toEntityAggregate() : OfferEntityAggregate {
        return OfferEntityAggregate(
            toEntity(),
            photos.map { it.toEntity(id) },
            address!!.toEntity(id)
        )
    }

}

// TODO : override toString Vs. upgrade enum to key-value
enum class PropertyType {
    HOUSE, APARTMENT, DUPLEX, MANSION
}

enum class OfferType {
    SALE, RENT
}

enum class Particularities {
    GARAGE, PARKING_LOT, BASEMENT, BALCONY, BACKYARD, SWIMMING_POOL, GYM_ROOM, GARDEN, JACUZZI, SAUNA
}

enum class POI {
    BUS_STATION, MARKET_MALL, MEDICAL_CENTER, SPORT_CENTER, CULTURAL_CENTER, SCHOOL, PARK, BAR_COFFEESHOP, RESTAURANT
}