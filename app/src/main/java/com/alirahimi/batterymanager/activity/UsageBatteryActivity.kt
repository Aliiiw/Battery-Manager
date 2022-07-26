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


        val adapter = BatteryUsageAdapter(batteryPercents, batteryUsage.getTotalTime())
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

    }
}