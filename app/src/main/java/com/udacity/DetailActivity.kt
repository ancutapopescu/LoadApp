package com.udacity

import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var notificationManager: NotificationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        // Initialize Notification Manager
        notificationManager = ContextCompat.getSystemService(
            applicationContext,
            NotificationManager::class.java
        ) as NotificationManager

        notificationManager.cancelAll()

        back_button.setOnClickListener {
            goBack()
        }

        val statusExtra = intent.getStringExtra("status")
        val fileName = intent.getStringExtra("filename")

        file_name.text = fileName
        status.text = statusExtra
    }




    fun goBack() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}
