package fr.mjoudar.realestatemanager.db.dao

import android.database.Cursor
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import fr.mjoudar.realestatemanager.db.entities.AddressEntity
import fr.mjoudar.realestatemanager.db.entities.OfferEntity
import fr.mjoudar.realestatemanager.db.entities.PhotoEntity
import fr.mjoudar.realestatemanager.db.relationships.OfferEntityAggregate
import fr.mjoudar.realestatemanager.domain.models.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface OfferDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOfferEntityAggregate(
        offer: OfferEntity,
        photos: List<PhotoEntity>,
        address: AddressEntity
    )

    suspend fun insertOffer(offerEntityAggregate: OfferEntityAggregate) {
        insertOfferEntityAggregate(
            offerEntityAggregate.offerEntity,
            offerEntityAggregate.photoEntities,
            offerEntityAggregate.addressEntity
        )
    }

    @Transaction
    @Query("SELECT * FROM offer WHERE id = :id")
    fun getOfferById(id: String): Flow<OfferEntityAggregate>

    @Transaction
    @Query("SELECT * from offer ORDER BY publication_date DESC")
    fun getAllOffers(): Flow<List<OfferEntityAggregate>>

    @Transaction
    @Query("SELECT COUNT(*) from offer")
    suspend fun getOffersCount(): Int

    @Transaction
    @Query(
        """
        SELECT *
        FROM offer
        INNER JOIN address ON address.offer_id = offer.id
        WHERE property_type LIKE '%' || :searchQuery || '%'  
        OR vicinity  LIKE '%' || :searchQuery || '%'  
        OR state  LIKE '%' || :searchQuery || '%'  
        OR city  LIKE '%' || :searchQuery || '%'
        ORDER BY publication_date DESC
   """
    )
    fun getOffersByKeyword(searchQuery: String): Flow<List<OfferEntityAggregate>>


    @RawQuery(observedEntities = [OfferEntityAggregate::class])
    fun getFilteredOffers(query: SupportSQLiteQuery) : Flow<List<OfferEntityAggregate>>


    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateOfferEntityAggregate(
        offer: OfferEntity,
        photos: List<PhotoEntity>,
        address: AddressEntity
    )

    suspend fun updateOffer(offerEntityAggregate: OfferEntityAggregate) {
        updateOfferEntityAggregate(
            offerEntityAggregate.offerEntity,
            offerEntityAggregate.photoEntities,
            offerEntityAggregate.addressEntity
        )
    }

    @Transaction
    @Query(
        """
            SELECT * FROM offer
            INNER JOIN address ON address.offer_id = offer.id
            INNER JOIN photo ON photo.offer_id = offer.id
            """
    )
    fun getAllOffersWithCursor(): Cursor

    @Transaction
    @Query(
        """
            SELECT COUNT(*) FROM offer
            INNER JOIN address ON address.offer_id = offer.id
            INNER JOIN photo ON photo.offer_id = offer.id
            """
    )
    fun getOffersCountWithCursor(): Cursor


    @Transaction
    @Query(
        """
        SELECT * FROM offer
        INNER JOIN address ON address.offer_id = offer.id
        INNER JOIN photo ON photo.offer_id = offer.id
        WHERE id = :id
    """
    )
    fun getOfferByIdWithCursor(id: String): Cursor
}
