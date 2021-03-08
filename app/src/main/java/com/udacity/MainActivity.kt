package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.udacity.util.sendNotification
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private var urlToDownload: String? = null
    private lateinit var fileName: String

    private lateinit var customButton: LoadingButton

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        customButton = findViewById(R.id.custom_button)
        customButton.setOnClickListener {
            download()
        }

        createChannel(
            getString(R.string.download_channel_id),
            getString(R.string.download_channel_name)
        )

    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.glide_button ->
                    if (checked) {
                    fileName = getString(R.string.glide_button_text)
                    urlToDownload = GLIDE_URL
                }
                R.id.loadapp_button ->
                    if (checked) {
                    fileName = getString(R.string.loadapp_button_text)
                    urlToDownload = LOAD_APP_URL
                }
                R.id.retrofit_button ->
                    if (checked) {
                    fileName = getString(R.string.retrofit_button_text)
                    urlToDownload = RETROFIT_URL
                    }
                else -> {

                    Toast.makeText(this, "Please select a file to download!", Toast.LENGTH_LONG)
                            .show()
                }
            }
        }


    }


    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (id == downloadID) {

                // Reset download button state, stop animation
                customButton.buttonState = ButtonState.Completed

                notificationManager.sendNotification(urlToDownload.toString(), applicationContext, "Success")
            } else {
                customButton.buttonState = ButtonState.Completed
                notificationManager.sendNotification(urlToDownload.toString(), applicationContext, "Failed")
            }
        }
    }

    private fun download() {
        val request =
            DownloadManager.Request(Uri.parse(urlToDownload))
                .setTitle(fileName)
                .setDescription(getString(R.string.app_description))
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        // Set button state, triggers the animation
        customButton.buttonState = ButtonState.Loading

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID = downloadManager.enqueue(request)
    }

    companion object {
        private const val GLIDE_URL = "https://github.com/bumptech/glide/archive/master.zip"
        private const val LOAD_APP_URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val RETROFIT_URL = "https://github.com/square/retrofit/archive/master.zip"
        private const val CHANNEL_ID = "channelId"

    }

    private fun createChannel(channelId: String, channelName: String) {
        // Create a channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
                // Disable badges for this channel
                .apply {
                    setShowBadge(false)
                }


            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = getString(R.string.app_name)

            val notificationManager = this.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

        override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }


}
