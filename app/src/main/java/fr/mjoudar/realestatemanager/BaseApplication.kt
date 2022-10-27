package fr.mjoudar.realestatemanager

import android.app.Application
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.HiltAndroidApp
import fr.mjoudar.realestatemanager.notification.NotificationHandler
import timber.log.Timber

@HiltAndroidApp
class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }

        NotificationHandler.createNotificationChannel(
            this,
            NotificationManagerCompat.IMPORTANCE_HIGH, false,
            getString(R.string.app_name), "App notification channel."
        )
    }
}