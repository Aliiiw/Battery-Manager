package com.alirahimi.batterymanager.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.alirahimi.batterymanager.R
import com.alirahimi.batterymanager.adapter.BatteryUsageAdapter
import com.alirahimi.batterymanager.databinding.ActivityUsageBatteryBinding
import com.alirahimi.batterymanager.model.BatteryModel
import com.alirahimi.batterymanager.utils.BatteryUsage
import java.util.ArrayList

class UsageBatteryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUsageBatteryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUsageBatteryBinding.inflate(layoutInflater)
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


        val adapter = BatteryUsageAdapter(batteryPercents)
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

    }
}