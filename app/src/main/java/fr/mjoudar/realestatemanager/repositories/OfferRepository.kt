package fr.mjoudar.realestatemanager.repositories

import androidx.sqlite.db.SimpleSQLiteQuery
import fr.mjoudar.realestatemanager.db.dao.OfferDao
import fr.mjoudar.realestatemanager.domain.models.Offer
import fr.mjoudar.realestatemanager.domain.models.OffersFilter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OfferRepository @Inject constructor(private val offerDao: OfferDao) {

    suspend fun saveOffer(offer: Offer) {
        offerDao.insertOffer(offer.toEntityAggregate())
    }

    fun getOfferById(id: String): Flow<Offer> {
        return offerDao.getOfferById(id)
            .distinctUntilChanged()
            .map { it ->
                it.toModel()
            }
    }

    fun getAllOffers(): Flow<List<Offer>> {
        return offerDao.getAllOffers()
            .distinctUntilChanged()
            .map { offersList ->
                offersList.map {
                    it.toModel()
                }
            }
    }

    fun getOffersByKeyword(searchQuery: String): Flow<List<Offer>> {
        return offerDao.getOffersByKeyword(searchQuery)
            .distinctUntilChanged()
            .map { offer ->
                offer.map {
                    it.toModel()
                }
            }
    }

    suspend fun updateOffer(offer: Offer) {
        offerDao.updateOffer(offer.toEntityAggregate())
    }

    fun getFilteredOffers(filter: OffersFilter) : Flow<List<Offer>> {
        return offerDao.getFilteredOffers(SimpleSQLiteQuery(filter.toSimpleSQLiteQueryString()))
            .distinctUntilChanged()
            .map { offerEntityAggregateList ->
                offerEntityAggregateList.map {
                    it.toModel()
                }
            }
    }
}