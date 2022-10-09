package fr.mjoudar.realestatemanager.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import fr.mjoudar.realestatemanager.db.dao.AgentDao
import fr.mjoudar.realestatemanager.db.dao.OfferDao
import fr.mjoudar.realestatemanager.db.entities.AddressEntity
import fr.mjoudar.realestatemanager.db.entities.AgentEntity
import fr.mjoudar.realestatemanager.db.entities.OfferEntity
import fr.mjoudar.realestatemanager.db.entities.PhotoEntity

@Database(
    entities = [
        AgentEntity::class,
        OfferEntity::class,
        PhotoEntity::class,
        AddressEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class BusinessDatabase : RoomDatabase() {

    abstract fun offerDao(): OfferDao
    abstract fun agentDao(): AgentDao

    companion object {
        const val DATABASE_NAME = "business_database"

        @Volatile
        private var INSTANCE: BusinessDatabase? = null
        fun getDatabase(context: Context): BusinessDatabase {
            return INSTANCE
                ?: synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        BusinessDatabase::class.java,
                        DATABASE_NAME
                    ).build()
                    INSTANCE = instance
                    return instance
                }
        }
    }

}