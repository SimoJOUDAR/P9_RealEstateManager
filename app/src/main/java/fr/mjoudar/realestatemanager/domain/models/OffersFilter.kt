package fr.mjoudar.realestatemanager.domain.models

import java.time.LocalDate

data class OffersFilter(
    var _propertyType: MutableList<PropertyType> = mutableListOf(),
    var _offerType: MutableList<OfferType> = mutableListOf(),
    var _availability: Boolean? = null,
    var _maxPrice: Long? = null,
    var _minPrice: Long? = null,
    var _maxSurface: Int? = null,
    var _minSurface: Int? = null,
    var _maxRooms: Int? = null,
    var _minRooms: Int? = null,
    var _maxBathrooms: Int? = null,
    var _minBathrooms: Int? = null,
    var _particularities: MutableList<Particularities> = mutableListOf(),
    var _descriptionTextQuery: String? = null,
    val withPhotos: Boolean? = null,
    var city: String? = null,
    var state: String? = null,
    var country: String? = null,
    var _poi: MutableList<POI>? = mutableListOf(),
    var _agent: Agent? = null,
    var _publicationDateFrom: LocalDate? = null,
    var _publicationDateTo: LocalDate? = null,
    var _closureDateFrom: LocalDate? = null,
    var _closureDateTo: LocalDate? = null,
)