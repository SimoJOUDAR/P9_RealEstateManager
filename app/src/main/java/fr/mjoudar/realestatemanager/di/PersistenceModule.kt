package fr.mjoudar.realestatemanager.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fr.mjoudar.realestatemanager.db.BusinessDatabase
import fr.mjoudar.realestatemanager.db.dao.AgentDao
import fr.mjoudar.realestatemanager.db.dao.OfferDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PersistenceModule {
    companion object {

        @Singleton
        @Provides
        fun provideBusinessDatabase(@ApplicationContext context: Context): BusinessDatabase {
            return Room.databaseBuilder(
                context,
                BusinessDatabase::class.java,
                BusinessDatabase.DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
        }

        @Singleton
        @Provides
        fun provideOfferDao(db: BusinessDatabase): OfferDao {
            return db.offerDao()
        }


        @Singleton
        @Provides
        fun provideAgentDao(db: BusinessDatabase): AgentDao {
            return db.agentDao()
        }


    }
}