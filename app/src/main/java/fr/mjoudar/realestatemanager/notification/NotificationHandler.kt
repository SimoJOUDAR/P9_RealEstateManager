package fr.mjoudar.realestatemanager.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import fr.mjoudar.realestatemanager.R
import fr.mjoudar.realestatemanager.domain.models.Offer
import fr.mjoudar.realestatemanager.ui.addEditOffer.AddEditOfferFragment
import fr.mjoudar.realestatemanager.ui.details.OfferDetailsFragment
import fr.mjoudar.realestatemanager.ui.homepage.HomepageActivity

object NotificationHandler {
    fun createNotificationChannel(
        context: Context,
        importance: Int,
        showBadge: Boolean,
        name: String,
        description: String
    ) {
        // 1
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // 2
            val channelId = "${context.packageName}-$name"
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = description
            channel.setShowBadge(showBadge)

            // 3
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun createOfferNotification(
        context: Context,
        title: String,
        message: String?,
        bigText: String,
        autoCancel: Boolean,
        offer: Offer?
    ) {
        val channelId = "${context.packageName}-${context.getString(R.string.app_name)}"
        val notificationBuilder = NotificationCompat.Builder(context, channelId).apply {
            setSmallIcon(R.drawable.ic_check_24dp)
            setContentTitle(title)
            setContentText(message)
            setStyle(NotificationCompat.BigTextStyle().bigText(bigText))
            priority = NotificationCompat.PRIORITY_HIGH
            setAutoCancel(autoCancel)
        }
        val bundle = Bundle()
        bundle.putParcelable(OfferDetailsFragment.OFFER_ARG, offer)
        val pendingIntent = NavDeepLinkBuilder(context)
            .setComponentName(HomepageActivity::class.java)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.offerDetailsFragment)
            .setArguments(bundle)
            .createPendingIntent()
        notificationBuilder.setContentIntent(pendingIntent)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(1001, notificationBuilder.build())
    }

    fun createAgentNotification(
        context: Context,
        title: String,
        message: String?,
        bigText: String,
        autoCancel: Boolean
    ) {
        val channelId = "${context.packageName}-${context.getString(R.string.app_name)}"
        val notificationBuilder = NotificationCompat.Builder(context, channelId).apply {
            setSmallIcon(R.drawable.ic_check_24dp)
            setContentTitle(title)
            setContentText(message)
            setStyle(NotificationCompat.BigTextStyle().bigText(bigText))
            priority = NotificationCompat.PRIORITY_HIGH
            setAutoCancel(autoCancel)

            val intent = Intent(context, HomepageActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
            setContentIntent(pendingIntent)
        }

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(1001, notificationBuilder.build())
    }
}