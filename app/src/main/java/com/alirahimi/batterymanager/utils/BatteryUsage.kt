package com.alirahimi.batterymanager.utils

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.provider.Settings


class BatteryUsage(context: Context) {
    private val myContext = context

    init {
        //check if the permission is enabled

        if (getUsageStateList().isEmpty()) {
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            context.startActivity(intent)
        }
    }


    fun getUsageStateList(): List<UsageStats> {
        val usm = getUsageStatsManager(myContext)
        val startTime = System.currentTimeMillis() - (24 * 3600 * 1000)
        val endTime = System.currentTimeMillis()

        return usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime)
    }


    private fun getUsageStatsManager(context: Context): UsageStatsManager {
        return context.getSystemService("usagestats") as UsageStatsManager
    }


    fun getTotalTime(): Long {
        var totalTime: Long = 0
        for (item in getUsageStateList()) {
            totalTime += item.totalTimeInForeground
        }
        return totalTime
    }

}
