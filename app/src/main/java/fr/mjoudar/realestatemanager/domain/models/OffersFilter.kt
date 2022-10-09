package fr.mjoudar.realestatemanager.domain.models

import java.time.LocalDate

data class OffersFilter(
    var _propertyTypes: List<PropertyType> = arrayListOf(),
    var _offerTypes: List<OfferType> = arrayListOf(),
    var _availability: Boolean? = null,
    var _minPrice: Long? = null,
    var _maxPrice: Long? = null,
    var _minSurface: Int? = null,
    var _maxSurface: Int? = null,
    var _minRooms: Int? = null,
    var _maxRooms: Int? = null,
    var _minBathrooms: Int? = null,
    var _maxBathrooms: Int? = null,
    var _particularities: List<Particularities> = arrayListOf(),
    var _withPhotos: Boolean? = null,
    var _city: String? = null,
    var _state: String? = null,
    var _country: String? = null,
    var _poi: List<POI> = arrayListOf(),
    var _agentId: String? = null,
    var _publicationDateFrom: Long? = null,
    var _publicationDateTo: Long? = null,
    var _closureDateFrom: Long? = null,
    var _closureDateTo: Long? = null,
    var _sorting: Sorting? = Sorting.PUBLICATION_DATE_DESC
) {
    fun toSimpleSQLiteQueryString() : String {
        val builder = StringBuilder("SELECT * FROM offer " +
                "INNER JOIN address ON address.offer_id = offer.id " +
                "INNER JOIN photo ON photo.offer_id = offer.id WHERE ")
        if (_propertyTypes.isNotEmpty()) builder.append("(property_type IN \"%$_propertyTypes%\") AND ")
        if (_offerTypes.isNotEmpty()) builder.append("(offer_type IN \"%$_offerTypes%\") AND ")
        if (_availability != null) builder.append("(availability IS $_availability) AND ")
        when {
            _minPrice != null && _maxPrice != null -> builder.append("(price BETWEEN $_minPrice AND $_maxPrice) AND ")
            _minPrice != null && _maxPrice == null -> builder.append("(price >= $_minPrice) AND ")
            _minPrice == null && _maxPrice != null -> builder.append("(price <= $_maxPrice) AND ")
        }
        when {
            _minSurface != null && _maxSurface != null -> builder.append("(surface BETWEEN $_minSurface AND $_maxSurface) AND ")
            _minSurface != null && _maxSurface == null -> builder.append("(surface >= $_minSurface) AND ")
            _minSurface == null && _maxSurface != null -> builder.append("(surface <= $_maxSurface) AND ")
        }
        when {
            _minRooms != null && _maxRooms != null -> builder.append("(rooms BETWEEN $_minRooms AND $_maxRooms) AND ")
            _minRooms != null && _maxRooms == null -> builder.append("(rooms >= $_minRooms) AND ")
            _minRooms == null && _maxRooms != null -> builder.append("(rooms <= $_maxRooms) AND ")
        }
        when {
            _minBathrooms != null && _maxBathrooms != null -> builder.append("(bathrooms BETWEEN $_minBathrooms AND $_maxBathrooms) AND ")
            _minBathrooms != null && _maxBathrooms == null -> builder.append("(bathrooms >= $_minBathrooms) AND ")
            _minBathrooms == null && _maxBathrooms != null -> builder.append("(bathrooms <= $_maxBathrooms) AND ")
        }
        if (_particularities.isNotEmpty()) {
            for (i in _particularities) {
                builder.append("($i IN particularities AND ")
            }
            builder.setLength(builder.length-5)
            builder.append(") AND ")
        }
        if (_withPhotos == true) builder.append("(main_photo_id IS NOT NULL) AND ")
        if (_city != null) builder.append("(city IS $_city) AND ")
        if (_state != null) builder.append("(state IS $_state) AND ")
        if (_country != null) builder.append("(country IS $_country) AND ")
        if (_poi.isNotEmpty()) {
            for (i in _poi) {
                builder.append("($i IN poi AND ")
            }
            builder.setLength(builder.length-5)
            builder.append(") AND ")
        }
        if (_agentId != null) builder.append("(agent_id IS $_agentId) AND ")
        when {
            _publicationDateFrom != null && _publicationDateTo != null -> builder.append("(publication_date BETWEEN $_publicationDateFrom AND $_publicationDateTo) AND ")
            _publicationDateFrom != null && _publicationDateTo == null -> builder.append("(publication_date >= $_publicationDateFrom) AND ")
            _publicationDateFrom == null && _publicationDateTo != null -> builder.append("(publication_date <= $_publicationDateTo) AND ")
        }
        when {
            _closureDateFrom != null && _closureDateTo != null -> builder.append("(closure_date BETWEEN $_closureDateFrom AND $_closureDateTo) AND ")
            _closureDateFrom != null && _closureDateTo == null -> builder.append("(closure_date >= $_closureDateFrom) AND ")
            _closureDateFrom == null && _closureDateTo != null -> builder.append("(closure_date <= $_closureDateTo) AND ")
        }
        builder.setLength(builder.length-5)
        if (_sorting != null) {
            when(_sorting) {
                Sorting.PRICE_ASC -> builder.append(" ORDER BY price ASC")
                Sorting.PRICE_DESC -> builder.append(" ORDER BY price DESC")
                Sorting.SURFACE_ASC -> builder.append(" ORDER BY surface ASC")
                Sorting.SURFACE_DESC -> builder.append(" ORDER BY surface DESC")
                Sorting.PUBLICATION_DATE_ASC -> builder.append(" ORDER BY publicationDate ASC")
                Sorting.PUBLICATION_DATE_DESC -> builder.append(" ORDER BY publicationDate DESC")
                Sorting.CLOSURE_DATE_ASC -> builder.append(" ORDER BY closureDate ASC")
                Sorting.CLOSURE_DATE_DESC -> builder.append(" ORDER BY closureDate DESC")
                else -> {}
            }
        }
        return builder.toString()
    }
}

enum class Sorting {
    PRICE_ASC, PRICE_DESC, SURFACE_ASC, SURFACE_DESC, PUBLICATION_DATE_ASC, PUBLICATION_DATE_DESC, CLOSURE_DATE_ASC, CLOSURE_DATE_DESC
}