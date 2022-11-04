package fr.mjoudar.realestatemanager.provider

import android.content.ContentValues
import android.net.Uri
import fr.mjoudar.realestatemanager.db.entities.AddressEntity
import fr.mjoudar.realestatemanager.db.entities.AgentEntity
import fr.mjoudar.realestatemanager.db.entities.OfferEntity
import fr.mjoudar.realestatemanager.db.entities.PhotoEntity
import fr.mjoudar.realestatemanager.domain.models.OfferType
import fr.mjoudar.realestatemanager.domain.models.POI
import fr.mjoudar.realestatemanager.domain.models.Particularities
import fr.mjoudar.realestatemanager.domain.models.PropertyType
import fr.mjoudar.realestatemanager.provider.ContentValues.AddressTable.Columns.KEY_ADDRESS_CITY
import fr.mjoudar.realestatemanager.provider.ContentValues.AddressTable.Columns.KEY_ADDRESS_COMPLEMENT
import fr.mjoudar.realestatemanager.provider.ContentValues.AddressTable.Columns.KEY_ADDRESS_COUNTRY
import fr.mjoudar.realestatemanager.provider.ContentValues.AddressTable.Columns.KEY_ADDRESS_ID
import fr.mjoudar.realestatemanager.provider.ContentValues.AddressTable.Columns.KEY_ADDRESS_LAT
import fr.mjoudar.realestatemanager.provider.ContentValues.AddressTable.Columns.KEY_ADDRESS_LNG
import fr.mjoudar.realestatemanager.provider.ContentValues.AddressTable.Columns.KEY_ADDRESS_STATE
import fr.mjoudar.realestatemanager.provider.ContentValues.AddressTable.Columns.KEY_ADDRESS_VICINITY
import fr.mjoudar.realestatemanager.provider.ContentValues.AddressTable.Columns.KEY_ADDRESS_ZIPCODE
import fr.mjoudar.realestatemanager.provider.ContentValues.AgentsTable.Columns.KEY_AGENT_AVATAR
import fr.mjoudar.realestatemanager.provider.ContentValues.AgentsTable.Columns.KEY_AGENT_EMAIL
import fr.mjoudar.realestatemanager.provider.ContentValues.AgentsTable.Columns.KEY_AGENT_ID
import fr.mjoudar.realestatemanager.provider.ContentValues.AgentsTable.Columns.KEY_AGENT_NAME
import fr.mjoudar.realestatemanager.provider.ContentValues.AgentsTable.Columns.KEY_AGENT_PHONE
import fr.mjoudar.realestatemanager.provider.ContentValues.OffersTable.Columns.KEY_OFFER_AGENT_ID
import fr.mjoudar.realestatemanager.provider.ContentValues.OffersTable.Columns.KEY_OFFER_AVAILABILITY
import fr.mjoudar.realestatemanager.provider.ContentValues.OffersTable.Columns.KEY_OFFER_BATHROOMS
import fr.mjoudar.realestatemanager.provider.ContentValues.OffersTable.Columns.KEY_OFFER_CLOSURE_DATE
import fr.mjoudar.realestatemanager.provider.ContentValues.OffersTable.Columns.KEY_OFFER_DESCRIPTION
import fr.mjoudar.realestatemanager.provider.ContentValues.OffersTable.Columns.KEY_OFFER_ID
import fr.mjoudar.realestatemanager.provider.ContentValues.OffersTable.Columns.KEY_OFFER_OFFER_TYPE
import fr.mjoudar.realestatemanager.provider.ContentValues.OffersTable.Columns.KEY_OFFER_PARTICULARITIES
import fr.mjoudar.realestatemanager.provider.ContentValues.OffersTable.Columns.KEY_OFFER_POI
import fr.mjoudar.realestatemanager.provider.ContentValues.OffersTable.Columns.KEY_OFFER_PRICE
import fr.mjoudar.realestatemanager.provider.ContentValues.OffersTable.Columns.KEY_OFFER_PROPERTY_TYPE
import fr.mjoudar.realestatemanager.provider.ContentValues.OffersTable.Columns.KEY_OFFER_PUBLICATION_DATE
import fr.mjoudar.realestatemanager.provider.ContentValues.OffersTable.Columns.KEY_OFFER_ROOMS
import fr.mjoudar.realestatemanager.provider.ContentValues.OffersTable.Columns.KEY_OFFER_SURFACE
import fr.mjoudar.realestatemanager.provider.ContentValues.PhotosTable.Columns.KEY_PHOTO_DESCRIPTION
import fr.mjoudar.realestatemanager.provider.ContentValues.PhotosTable.Columns.KEY_PHOTO_ID
import fr.mjoudar.realestatemanager.provider.ContentValues.PhotosTable.Columns.KEY_PHOTO_OFFER_ID
import fr.mjoudar.realestatemanager.provider.ContentValues.PhotosTable.Columns.KEY_PHOTO_URI

object ContentValues {

    fun getOfferFromContentValues(values: ContentValues): OfferEntity {
        return OfferEntity(
            values.getAsString(KEY_OFFER_ID),
            values.get(KEY_OFFER_PROPERTY_TYPE) as PropertyType,
            values.get(KEY_OFFER_OFFER_TYPE) as OfferType,
            values.getAsBoolean(KEY_OFFER_AVAILABILITY),
            values.getAsLong(KEY_OFFER_PRICE),
            values.getAsInteger(KEY_OFFER_SURFACE),
            values.getAsInteger(KEY_OFFER_ROOMS),
            values.getAsInteger(KEY_OFFER_BATHROOMS),
            values.get(KEY_OFFER_PARTICULARITIES) as List<Particularities>,
            values.getAsString(KEY_OFFER_DESCRIPTION),
            values.get(KEY_OFFER_POI) as List<POI>,
            values.getAsString(KEY_OFFER_AGENT_ID),
            values.getAsLong(KEY_OFFER_PUBLICATION_DATE),
            values.getAsLong(KEY_OFFER_CLOSURE_DATE)
        )
    }

    fun getAddressFromContentValues(values: ContentValues): AddressEntity {
        return AddressEntity(
            values.getAsLong(KEY_ADDRESS_ID),
            values.getAsString(KEY_OFFER_ID),
            values.getAsString(KEY_ADDRESS_VICINITY),
            values.getAsString(KEY_ADDRESS_COMPLEMENT),
            values.getAsString(KEY_ADDRESS_ZIPCODE),
            values.getAsString(KEY_ADDRESS_CITY),
            values.getAsString(KEY_ADDRESS_STATE),
            values.getAsString(KEY_ADDRESS_COUNTRY),
            values.getAsDouble(KEY_ADDRESS_LAT),
            values.getAsDouble(KEY_ADDRESS_LNG)
        )
    }

    fun getPhotoFromContentValues(values: ContentValues): PhotoEntity {

        return PhotoEntity(
            values.getAsString(KEY_PHOTO_ID),
            values.getAsString(KEY_PHOTO_OFFER_ID),
            values.getAsString(KEY_PHOTO_URI),
            values.getAsString(KEY_PHOTO_DESCRIPTION)
        )
    }

    fun getAgentFromContentValues(values: ContentValues): AgentEntity {
        return AgentEntity(
            values.getAsString(KEY_AGENT_ID),
            values.getAsString(KEY_AGENT_NAME),
            values.getAsString(KEY_AGENT_AVATAR),
            values.getAsString(KEY_AGENT_EMAIL),
            values.getAsString(KEY_AGENT_PHONE)
        )
    }



    //The URI suffix for counting records
    const val COUNT = "count"

    //URI Authority
    const val AUTHORITY = "fr.mjoudar.realestatemanager.provider"

    //  public tables.
    const val CONTENT_PATH_AGENT = "agent"
    const val CONTENT_PATH_OFFERS = "offers"

    // Content URI for  tables. Returns all items.
    val CONTENT_URI_OFFERS = Uri.parse("content://$AUTHORITY/$CONTENT_PATH_OFFERS")
    val CONTENT_URI_AGENT = Uri.parse("content://$AUTHORITY/$CONTENT_PATH_AGENT")

    // Single record mime type
    const val AGENT_SINGLE_RECORD_MIME_TYPE = "vnd.android.cursor.item/vnd.fr.mjoudar.realestatemanager.provider.agent"
    const val OFFERS_SINGLE_RECORD_MIME_TYPE = "vnd.android.cursor.item/vnd.fr.mjoudar.realestatemanager.provider.offers"


    // Multiple Record MIME type
    const val AGENT_MULTIPLE_RECORDS_MIME_TYPE = "vnd.android.cursor.dir/vnd.fr.mjoudar.realestatemanager.provider.agent"
    const val OFFERS_MULTIPLE_RECORD_MIME_TYPE = "vnd.android.cursor.dir/vnd.fr.mjoudar.realestatemanager.provider.offers"

    // URI Codes
    const val AGENT_URI_ALL_ITEMS_CODE = 10
    const val AGENT_URI_ONE_ITEM_CODE = 20
    const val AGENT_URI_COUNT_CODE = 30
    const val OFFERS_URI_ALL_ITEMS_CODE = 11
    const val OFFERS_URI_ONE_ITEM_CODE = 21
    const val OFFERS_URI_COUNT_CODE = 31

    object OffersTable {

        const val TABLE_NAME: String = "offers"

        object Columns {
            const val KEY_OFFER_ID: String = "id"
            const val KEY_OFFER_PROPERTY_TYPE: String = "propertyType"
            const val KEY_OFFER_OFFER_TYPE: String = "offerType"
            const val KEY_OFFER_AVAILABILITY: String = "availability"
            const val KEY_OFFER_PRICE: String = "price"
            const val KEY_OFFER_SURFACE: String = "surface"
            const val KEY_OFFER_ROOMS: String = "rooms"
            const val KEY_OFFER_BATHROOMS: String = "bathrooms"
            const val KEY_OFFER_PARTICULARITIES: String = "particularities"
            const val KEY_OFFER_DESCRIPTION: String = "description"
            const val KEY_OFFER_POI: String = "poi"
            const val KEY_OFFER_AGENT_ID: String = "agentId"
            const val KEY_OFFER_PUBLICATION_DATE: String = "sell_date"
            const val KEY_OFFER_CLOSURE_DATE: String = "sold_date"
        }
    }


    object AddressTable {

        const val TABLE_NAME: String = "offer_addresses"

        object Columns {
            const val KEY_ADDRESS_ID: String = "address_id"
            const val KEY_ADDRESS_OFFER_ID: String = "property_id"
            const val KEY_ADDRESS_VICINITY: String = "address1"
            const val KEY_ADDRESS_COMPLEMENT: String = "address_2"
            const val KEY_ADDRESS_ZIPCODE: String = "zipCode"
            const val KEY_ADDRESS_CITY: String = "city"
            const val KEY_ADDRESS_STATE: String = "state"
            const val KEY_ADDRESS_COUNTRY: String = "country"
            const val KEY_ADDRESS_LAT: String = "lat"
            const val KEY_ADDRESS_LNG: String = "lng"
        }
    }

    object PhotosTable {

        const val TABLE_NAME: String = "offer_photos"

        object Columns {
            const val KEY_PHOTO_ID: String = "photo_id"
            const val KEY_PHOTO_OFFER_ID: String = "offer_id"
            const val KEY_PHOTO_URI: String = "uri"
            const val KEY_PHOTO_DESCRIPTION: String = "name"
        }
    }

    object AgentsTable {

        const val TABLE_NAME: String = "agents"

        object Columns {
            const val KEY_AGENT_ID: String = "id"
            const val KEY_AGENT_NAME: String = "name"
            const val KEY_AGENT_AVATAR: String = "avatar"
            const val KEY_AGENT_EMAIL: String = "email"
            const val KEY_AGENT_PHONE: String = "email"
        }
    }
}