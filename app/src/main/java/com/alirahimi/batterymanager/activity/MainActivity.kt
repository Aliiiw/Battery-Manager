package com.alirahimi.batterymanager.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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

        val batteryUsage = BatteryUsage(this)

        val batteryPercents: MutableList<BatteryModel> = ArrayList()
        for (item in batteryUsage.getUsageStateList()) {

            if (item.totalTimeInForeground > 0) {
                val batteryModel = BatteryModel()
                batteryModel.packageName = item.packageName
                batteryModel.percentUsage =
                    (item.totalTimeInForeground.toFloat() / batteryUsage.getTotalTime()
                        .toFloat() * 100).toInt()
                batteryPercents += batteryModel
            }
        }

        var sortedList =
            batteryPercents.groupBy { it.packageName }.mapValues { entry ->
                entry.value.sumBy { it.percentUsage }
            }.toList().sortedWith(compareBy { it.second }).reversed()

        for (item in sortedList) {
            val timePerApp =
                item.second.toFloat() / 100 * batteryUsage.getTotalTime().toFloat() / 1000 / 60
            val hourTime = (timePerApp / 60).toInt()
            val minutesTime = (timePerApp % 60).toInt()

            Log.e(
                "3636",
                "${item.first} : ${item.second} time usage is -> $hourTime h : $minutesTime m"
            )
        }
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

        }
    }
}