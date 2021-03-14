package com.udacity.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.udacity.DetailActivity
import com.udacity.MainActivity
import com.udacity.R

// Notification ID.
private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0

// TODO: Step 1.1 extension function to send messages
/**
 * Builds and delivers the notification.
 *
 * @param context, activity context.
 */
fun NotificationManager.sendNotification(fileName: String, applicationContext: Context, status: String) {
    // Create the content intent for the notification, which launches this activity.
    // Step 1.11 create intent
    val contentIntent = Intent(applicationContext, DetailActivity::class.java)
        .putExtra("fileName", fileName)
        .putExtra("status", status)

    // Step 1.12 create PendingIntent
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )


    // Step 2.0 add style
   val cloudImage = BitmapFactory.decodeResource(
        applicationContext.resources,
        R.drawable.ic_load_app_notification_200dp
    )
    val bigPicStyle = NotificationCompat.BigPictureStyle()
        .bigPicture(cloudImage)
        .bigLargeIcon(null)



    // Step 1.2 get an instance of NotificationCompat.Builder
    // Build the notification
    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.download_channel_id)
    )



    // Step 1.3 set title, text and icon to builder
    .setSmallIcon(R.drawable.ic_load_app_notification)
    .setContentTitle(applicationContext
    .getString(R.string.notification_title))
    .setContentText(fileName)

    // Step 1.13 set content intent
    .setContentIntent(contentPendingIntent)
    .setAutoCancel(true)

    // Set the action to open the DetailActivity
    .addAction(0,
        applicationContext.getString(R.string.notification_action_details),
        contentPendingIntent)

    // Step 2.5 set priority
    .setPriority(NotificationCompat.PRIORITY_HIGH)

    // Step 2.1 add style to builder
    .setStyle(bigPicStyle)
    .setLargeIcon(cloudImage)


    // Step 1.4 call notify
    notify(NOTIFICATION_ID, builder.build())
}

// Step 1.14 Cancel all notifications
fun NotificationManager.cancelNotifications() {
    cancelAll()
}