package fr.mjoudar.realestatemanager.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import androidx.core.net.toUri
import fr.mjoudar.realestatemanager.db.BusinessDatabase
import fr.mjoudar.realestatemanager.db.dao.AgentDao
import fr.mjoudar.realestatemanager.db.dao.OfferDao
import fr.mjoudar.realestatemanager.provider.ContentValues.AGENT_MULTIPLE_RECORDS_MIME_TYPE
import fr.mjoudar.realestatemanager.provider.ContentValues.AGENT_SINGLE_RECORD_MIME_TYPE
import fr.mjoudar.realestatemanager.provider.ContentValues.AGENT_URI_ALL_ITEMS_CODE
import fr.mjoudar.realestatemanager.provider.ContentValues.AGENT_URI_COUNT_CODE
import fr.mjoudar.realestatemanager.provider.ContentValues.AGENT_URI_ONE_ITEM_CODE
import fr.mjoudar.realestatemanager.provider.ContentValues.AUTHORITY
import fr.mjoudar.realestatemanager.provider.ContentValues.CONTENT_PATH_AGENT
import fr.mjoudar.realestatemanager.provider.ContentValues.CONTENT_PATH_OFFERS
import fr.mjoudar.realestatemanager.provider.ContentValues.COUNT
import fr.mjoudar.realestatemanager.provider.ContentValues.OFFERS_MULTIPLE_RECORD_MIME_TYPE
import fr.mjoudar.realestatemanager.provider.ContentValues.OFFERS_SINGLE_RECORD_MIME_TYPE
import fr.mjoudar.realestatemanager.provider.ContentValues.OFFERS_URI_ALL_ITEMS_CODE
import fr.mjoudar.realestatemanager.provider.ContentValues.OFFERS_URI_COUNT_CODE
import fr.mjoudar.realestatemanager.provider.ContentValues.OFFERS_URI_ONE_ITEM_CODE
import fr.mjoudar.realestatemanager.provider.ContentValues.getAddressFromContentValues
import fr.mjoudar.realestatemanager.provider.ContentValues.getAgentFromContentValues
import fr.mjoudar.realestatemanager.provider.ContentValues.getOfferFromContentValues
import fr.mjoudar.realestatemanager.provider.ContentValues.getPhotoFromContentValues
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext

class OfferProvider : ContentProvider(), CoroutineScope {

    // provide access to the database
    private lateinit var sUriMatcher: UriMatcher
    private lateinit var offerDatabase: BusinessDatabase
    private lateinit var offerDao: OfferDao
    private lateinit var agentDao: AgentDao

    override fun onCreate(): Boolean {
        offerDatabase = BusinessDatabase.getDatabase(context!!)
        offerDao = offerDatabase.offerDao()
        agentDao = offerDatabase.agentDao()
        initUriMatcher()
        return true
    }

    // Adds the provider's matching URI
    private fun initUriMatcher() {
        sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        sUriMatcher.addURI(AUTHORITY, CONTENT_PATH_AGENT, AGENT_URI_ALL_ITEMS_CODE)
        sUriMatcher.addURI(AUTHORITY, "$CONTENT_PATH_AGENT/#", AGENT_URI_ONE_ITEM_CODE)
        sUriMatcher.addURI(
            AUTHORITY, "$CONTENT_PATH_AGENT/$COUNT",
            AGENT_URI_COUNT_CODE
        )
        sUriMatcher.addURI(AUTHORITY, "$CONTENT_PATH_AGENT/*", AGENT_URI_ONE_ITEM_CODE)

        sUriMatcher.addURI(AUTHORITY, CONTENT_PATH_OFFERS, OFFERS_URI_ALL_ITEMS_CODE)
        sUriMatcher.addURI(AUTHORITY, "$CONTENT_PATH_OFFERS/#", OFFERS_URI_ONE_ITEM_CODE)
        sUriMatcher.addURI(
            AUTHORITY, "$CONTENT_PATH_OFFERS/$COUNT",
            OFFERS_URI_COUNT_CODE
        )
        sUriMatcher.addURI(AUTHORITY, "$CONTENT_PATH_OFFERS/*", OFFERS_URI_ONE_ITEM_CODE)
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        var cursor: Cursor? = null
        val id = uri.lastPathSegment
        runBlocking {
            when (sUriMatcher.match(uri)) {
                OFFERS_URI_ALL_ITEMS_CODE -> {
                    cursor = offerDao.getAllOffersWithCursor()
                }

                OFFERS_URI_ONE_ITEM_CODE -> {
                    cursor = id?.let { offerDao.getOfferByIdWithCursor(it) }
                }

                OFFERS_URI_COUNT_CODE -> {
                    cursor = offerDao.getOffersCountWithCursor()
                }

                AGENT_URI_ALL_ITEMS_CODE -> {
                    cursor = agentDao.getAllAgentsWithCursor()
                }

                AGENT_URI_ONE_ITEM_CODE -> {
                    cursor = id?.let { agentDao.getAgentByIdWithCursor(it) }
                }

                AGENT_URI_COUNT_CODE -> {
                    cursor = agentDao.getAgentsCountWithCursor()
                }

                UriMatcher.NO_MATCH -> {
                    throw IllegalArgumentException("Query is not matching: $uri")
                }

                else -> {
                    throw IllegalArgumentException("Query doesn't exist: $uri")
                }
            }
        }
        return cursor
    }

    override fun getType(uri: Uri): String? = when (sUriMatcher.match(uri)) {
        AGENT_URI_ALL_ITEMS_CODE -> AGENT_MULTIPLE_RECORDS_MIME_TYPE
        AGENT_URI_ONE_ITEM_CODE -> AGENT_SINGLE_RECORD_MIME_TYPE
        OFFERS_URI_ALL_ITEMS_CODE -> OFFERS_MULTIPLE_RECORD_MIME_TYPE
        OFFERS_URI_ONE_ITEM_CODE -> OFFERS_SINGLE_RECORD_MIME_TYPE
        AGENT_URI_COUNT_CODE -> OFFERS_SINGLE_RECORD_MIME_TYPE
        else -> throw IllegalArgumentException("Unknown URI: $uri")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        if (context != null && values != null) {
            when (sUriMatcher.match(uri)) {
                AGENT_URI_ONE_ITEM_CODE -> {
                    val agent = getAgentFromContentValues(values)
                    runBlocking {
                        agentDao.insertAgent(agent)
                        context!!.contentResolver.notifyChange(uri, null)
                    }
                    return "$uri/${agent.id}".toUri()
                }
                OFFERS_URI_ONE_ITEM_CODE -> {
                    val offer = getOfferFromContentValues(values)
                    val photos = getPhotoFromContentValues(values)
                    val address = getAddressFromContentValues(values)

                    runBlocking {
                        context!!.contentResolver.notifyChange(uri, null)
                        offerDao.insertOfferEntityAggregate(
                            offer,
                            mutableListOf(photos),
                            address,
                        )
                    }
                    return "$uri/${offer.id}".toUri()
                }
                else -> {
                    throw IllegalArgumentException("Query doesn't exist: $uri")
                }
            }
        }
        return null
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO



}