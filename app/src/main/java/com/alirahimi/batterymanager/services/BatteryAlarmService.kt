package com.alirahimi.batterymanager.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.os.IBinder
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.alirahimi.batterymanager.R

class BatteryAlarmService : Service() {
    var manager: NotificationManager? = null
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        startNotification()

        registerReceiver(batteryDataReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))

        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannell =
                NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_MIN)
            manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serviceChannell)
        }
    }

    companion object {
        const val CHANNEL_ID = "BatteryManagerChannel"
        const val CHANNEL_NAME = "BatteryManagerService"
        const val NOTIFICATION_ID = 1
    }

    private fun startNotification() {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Loading...")
            .setContentText("wait for data from battery")
            .setSmallIcon(R.drawable.good_helath)
            .build()

        startForeground(NOTIFICATION_ID, notification)
    }

    private fun updateNotification(batteryLevel: Int, plugStat: String) {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(plugStat)
            .setContentText("Battery charge -> $batteryLevel")
            .setSmallIcon(R.drawable.good_helath)
            .build()

        manager?.notify(NOTIFICATION_ID, notification)
    }


    private var batteryDataReceiver: BroadcastReceiver = object : BroadcastReceiver() {

        @SuppressLint("SetTextI18n")
        override fun onReceive(context: Context, intent: Intent) {


            if (intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0) == 0) {
                //binding.textPlug.text = "Not-Plugged"
            } else {
                //binding.textPlug.text = "Plugged-In"
            }
            updateNotification(batteryLevel = )
        }
    }

}