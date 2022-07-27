package com.alirahimi.batterymanager.activity

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.BatteryManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import androidx.core.content.ContextCompat
import com.alirahimi.batterymanager.R
import com.alirahimi.batterymanager.databinding.ActivityMainBinding
import com.alirahimi.batterymanager.services.BatteryAlarmService


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @SuppressLint("RtlHardcoded")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        startService()


        binding.imageMenu.setOnClickListener {
            binding.drawer.openDrawer(Gravity.LEFT)
        }

        binding.includeDrawer.textAppUsage.setOnClickListener {
            startActivity(Intent(this@MainActivity, UsageBatteryActivity::class.java))
            binding.drawer.closeDrawer(Gravity.LEFT)
        }

        registerReceiver(batteryDataReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))

    }

    private var batteryDataReceiver: BroadcastReceiver = object : BroadcastReceiver() {

        @SuppressLint("SetTextI18n")
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
            if (batteryLevel < 15.0) {
                binding.circularProgressBar.progressBarColor = Color.RED
            } else if (batteryLevel < 50.0) {
                binding.circularProgressBar.progressBarColor = Color.YELLOW
            } else {
                binding.circularProgressBar.progressBarColor = Color.GREEN
            }

            val health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0)
            when (health) {
                BatteryManager.BATTERY_HEALTH_DEAD -> {
                    binding.textHealth.text =
                        "your battery is fully Dead! Please change your battery"
                    binding.textHealth.setTextColor(Color.parseColor("000000"))
                    binding.imageHealth.setImageResource(R.drawable.dead_health)

                }
                BatteryManager.BATTERY_HEALTH_GOOD -> {
                    binding.textHealth.text = "your battery is Good take care"
                    binding.textHealth.setTextColor(Color.GREEN)
                    binding.imageHealth.setImageResource(R.drawable.good_helath)
                }
                BatteryManager.BATTERY_HEALTH_COLD -> {
                    binding.textHealth.text = "your battery is Cold, warm it"
                    binding.textHealth.setTextColor(Color.BLUE)
                    binding.imageHealth.setImageResource(R.drawable.cold_health)
                }
                BatteryManager.BATTERY_HEALTH_OVERHEAT -> {
                    binding.textHealth.text = "your battery is OverHeat!, take it to refrigerator"
                    binding.textHealth.setTextColor(Color.RED)
                    binding.imageHealth.setImageResource(R.drawable.overheat_health)
                }
                BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> {
                    binding.textHealth.text = "your battery Voltage is high!"
                    binding.textHealth.setTextColor(Color.YELLOW)
                    binding.imageHealth.setImageResource(R.drawable.voltage_health)
                }
                else -> {
                    binding.textHealth.text =
                        "your battery is fully Dead! Please change your battery"
                    binding.textHealth.setTextColor(Color.parseColor("000000"))
                    binding.imageHealth.setImageResource(R.drawable.dead_health)

                }
            }
        }
    }

    private fun startService() {
        val serviceIntent = Intent(this, BatteryAlarmService::class.java)
        ContextCompat.startForegroundService(this, serviceIntent)
    }
}