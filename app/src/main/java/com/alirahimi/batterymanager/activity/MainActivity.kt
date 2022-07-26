package com.alirahimi.batterymanager.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.BatteryManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.alirahimi.batterymanager.utils.BatteryUsage
import com.alirahimi.batterymanager.databinding.ActivityMainBinding
import com.alirahimi.batterymanager.model.BatteryModel
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        registerReceiver(batteryDataReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))

    }

    private var batteryDataReceiver: BroadcastReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {

            binding.textTemperature.text =
                (intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10).toString() + " °C"
            binding.textVoltage.text =
                (intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0) / 1000).toString() + " volt"

            binding.textCpu.text = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY)
            if (intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0) == 0) {
                binding.textPlug.text = "Not-Plugged"
            } else {
                binding.textPlug.text = "Plugged-In"
            }


            val batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0).toFloat()

            if (batteryLevel == 100F) {
                binding.textLight.text = "Full Charge"
            } else {
                binding.textLight.text =
                    (intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)).toString() + " %"
            }
            binding.circularProgressBar.progressMax = 100F
            binding.circularProgressBar.setProgressWithAnimation(batteryLevel)
            if (batteryLevel < 15.0){
                binding.circularProgressBar.progressBarColor = Color.RED
            } else if (batteryLevel < 50.0){
                binding.circularProgressBar.progressBarColor = Color.YELLOW
            }else{
                binding.circularProgressBar.progressBarColor = Color.GREEN
            }
        }
    }
}