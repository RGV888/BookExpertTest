package com.pp.bookxpert

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.let {
            showNotification(it.title ?: "", it.body ?: "")
        }
    }

    private fun showNotification(title: String, message: String) {
        val channelId = "book_xpert"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Item Deleted", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.mipmap.ic_book_expert) // Your icon
            .setAutoCancel(true)

        notificationManager.notify(101, builder.build())
    }
}
