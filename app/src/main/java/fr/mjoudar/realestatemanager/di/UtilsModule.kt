package fr.mjoudar.realestatemanager.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fr.mjoudar.realestatemanager.BaseApplication
import fr.mjoudar.realestatemanager.domain.models.Offer
import fr.mjoudar.realestatemanager.utils.preferences.DataStorePreferences
import fr.mjoudar.realestatemanager.utils.preferences.MyPreferences
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UtilsModule {
    @Binds
    abstract fun bindPreferences(dataStorePreferences: DataStorePreferences): MyPreferences

    companion object {
        @Singleton
        @Provides
        fun provideApplication(@ApplicationContext app: Context): BaseApplication {
            return app as BaseApplication
        }

        @Singleton
        @Provides
        fun provideProperties(): List<Offer?> {
            return ArrayList()
        }

        @Singleton
        @Provides
        fun provideProperty(): Offer {
            return Offer()
        }
    }

}