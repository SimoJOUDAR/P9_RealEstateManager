package fr.mjoudar.realestatemanager

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import fr.mjoudar.realestatemanager.db.BusinessDatabase
import fr.mjoudar.realestatemanager.db.dao.AgentDao
import fr.mjoudar.realestatemanager.db.dao.OfferDao
import fr.mjoudar.realestatemanager.db.entities.AddressEntity
import fr.mjoudar.realestatemanager.db.entities.AgentEntity
import fr.mjoudar.realestatemanager.db.entities.OfferEntity
import fr.mjoudar.realestatemanager.db.entities.PhotoEntity
import fr.mjoudar.realestatemanager.db.relationships.OfferEntityAggregate
import fr.mjoudar.realestatemanager.domain.models.OfferType
import fr.mjoudar.realestatemanager.domain.models.POI
import fr.mjoudar.realestatemanager.domain.models.Particularities
import fr.mjoudar.realestatemanager.domain.models.PropertyType
import kotlinx.coroutines.FlowPreview

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.produceIn
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
@SmallTest
class OfferDaoTest {

    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var database: BusinessDatabase
    private lateinit var agentDao: AgentDao
    private lateinit var offerDao: OfferDao
    private val agent = AgentEntity(
        "1",
        "agent 1",
        "file:///android_asset/Agents/agent_avatar_1.png",
        "&gent1@rem.com",
        "+1 354-607-2343"
    )

    private val photo1 = PhotoEntity("1", "1", "url1", "this is photo1")
    private val photo2 = PhotoEntity("2", "1", "url2", "this is photo2")
    private val photo3 = PhotoEntity("3", "2", "url3", "this is photo3")
    private val photo4 = PhotoEntity("4", "2", "url4", "this is photo4")
    private val address1 = AddressEntity(1, "1", "21 street", "apt2", "23000", "NewYork", "NY", "US", 0.0, 0.0)
    private val address2 = AddressEntity(2, "2", "45 street", "sec3", "45G45", "Vancouver", "BC", "Canada", 0.0, 0.0)
    private val offerEntity1 = OfferEntity(
        "1",
        PropertyType.HOUSE,
        OfferType.SALE,
        true,
        500000,
        300,
        4,
        3,
        arrayListOf(Particularities.GARAGE),
        "This is offer1",
        arrayListOf(POI.SCHOOL),
        "1",
        123456,
        null
        )
    private val offerEntity2 = OfferEntity(
        "2",
        PropertyType.DUPLEX,
        OfferType.RENT,
        false,
        3000,
        100,
        3,
        2,
        arrayListOf(Particularities.BALCONY),
        "This is offer2",
        arrayListOf(POI.BAR_COFFEESHOP, POI.SCHOOL),
        "1",
        567890,
        567934
    )

    private val offer1 = OfferEntityAggregate(
        offerEntity1,
        arrayListOf(photo1, photo2),
        address1
    )

    private val offer2 = OfferEntityAggregate(
        offerEntity2,
        arrayListOf(photo3, photo4),
        address2
    )

    @Before
    fun initDatabase() {
        database = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            BusinessDatabase::class.java
        ).build()
        agentDao = database.agentDao()
        offerDao = database.offerDao()
        runBlocking {
            agentDao.insertAgent(agent)
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDatabase() {
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun should_get_empty_offer_list() = runBlocking {
        // The list is empty
        //Get first agent in the list
        val firstProperty = offerDao.getAllOffers().first()
        //First agent size should be 0
        Assert.assertEquals(0, firstProperty.size)
        closeDatabase()
    }

    @FlowPreview
    @Test
    @Throws(Exception::class)
    fun should_insert_and_retrieve_offers_correctly() = runBlocking {

        offerDao.insertOffer(offer1)
        offerDao.insertOffer(offer2)

        val offerList = mutableListOf(offer2, offer1)

        val channelFlow = offerDao.getAllOffers().produceIn(this)
        Assert.assertEquals(channelFlow.receive(), offerList)
        Assert.assertTrue(channelFlow.isEmpty)
        channelFlow.cancel()
        closeDatabase()
    }

    @Test
    @Throws(Exception::class)
    fun should_get_offer_by_id_correctly() = runBlocking {

        offerDao.insertOffer(offer1)
        offerDao.insertOffer(offer2)

        val result = offerDao.getOfferById(offer1.offerEntity.id).first()
        Assert.assertEquals(result, offer1)
        Assert.assertNotEquals(result, offer2)

        closeDatabase()
    }

    @Test
    @Throws(Exception::class)
    fun should_update_offer_correctly() = runBlocking {

        offerDao.insertOffer(offer1)

        val offer1Updated = offer1
        offer1Updated.offerEntity.surface = 300
        offerDao.updateOffer(offer1Updated)

        val result = offerDao.getOfferById(offer1.offerEntity.id).first()
        Assert.assertEquals(result, offer1Updated)

        closeDatabase()
    }







}