package com.alirahimi.batterymanager.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.RingtoneManager
import android.net.Uri
import android.os.*
import androidx.core.app.NotificationCompat
import com.alirahimi.batterymanager.R

class BatteryAlarmService : Service() {
    private var manager: NotificationManager? = null
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

            var batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)


            var plugState = ""
            if (intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0) == 0) {

                plugState = "your phone on Battery charge"
            } else {

                plugState = "your phone is charging using cable"
            }


            if (batteryLevel > 97) {
                startAlarm()
                plugState = "your phone is fully charged!"

            }

            updateNotification(batteryLevel, plugState)
        }
    }

    private fun startAlarm() {
        val alarm: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val ring = RingtoneManager.getRingtone(applicationContext, alarm)
        ring.play()

        val vibrate = getSystemService(VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) vibrate.vibrate(
            VibrationEffect.createOneShot(
                1500,
                VibrationEffect.DEFAULT_AMPLITUDE
            )
        )
        else vibrate.vibrate(1500)
    }
}