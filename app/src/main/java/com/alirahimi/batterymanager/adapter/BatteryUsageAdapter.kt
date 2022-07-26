package com.alirahimi.batterymanager.adapter

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alirahimi.batterymanager.R
import com.alirahimi.batterymanager.model.BatteryModel

class BatteryUsageAdapter(

    private val context: Context,
    private val battery: MutableList<BatteryModel>,
    private val totalTime: Long
) :
    RecyclerView.Adapter<BatteryUsageAdapter.ViewHolder>() {
    private var batteryFinalList: MutableList<BatteryModel> = ArrayList()

    init {
        batteryFinalList = calculateBatteryUsage(battery)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BatteryUsageAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.item_battery_usage, parent, false)
        return ViewHolder(listItem)
    }


    override fun onBindViewHolder(holder: BatteryUsageAdapter.ViewHolder, position: Int) {
        holder.textTime.text = batteryFinalList[position].timeUsage
        holder.textPercent.text = batteryFinalList[position].percentUsage.toString() + " %"
        holder.textAppName.text = getAppName(batteryFinalList[position].packageName.toString())
        holder.progressBar.progress = batteryFinalList[position].percentUsage


    }

    override fun getItemCount(): Int {
        return batteryFinalList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var textPercent: TextView = view.findViewById(R.id.text_percent)
        var textTime: TextView = view.findViewById(R.id.text_time)
        var textAppName: TextView = view.findViewById(R.id.text_app_name)
        var progressBar: ProgressBar = view.findViewById(R.id.progress_bar)

    }

    private fun calculateBatteryUsage(batteryPercents: MutableList<BatteryModel>): MutableList<BatteryModel> {
        val finalList: MutableList<BatteryModel> = ArrayList()
        val sortedList =
            batteryPercents.groupBy { it.packageName }.mapValues { entry ->
                entry.value.sumBy { it.percentUsage }
            }.toList().sortedWith(compareBy { it.second }).reversed()

        for (item in sortedList) {
            val batteryModel = BatteryModel()
            val timePerApp = item.second.toFloat() / 100 * totalTime.toFloat() / 1000 / 60
            val hourTime = (timePerApp / 60).toInt()
            val minutesTime = (timePerApp % 60).toInt()

            batteryModel.packageName = item.first
            batteryModel.percentUsage = item.second
            batteryModel.timeUsage = "$hourTime hours $minutesTime minutes"

            finalList += batteryModel
        }
        return finalList
    }

    fun getAppName(packageName: String): String {
        val packageManager = context.applicationContext.packageManager
        val appInfo: ApplicationInfo? = try {
            packageManager.getApplicationInfo(packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }
        return (if (appInfo != null) packageManager.getApplicationLabel(appInfo) else "(unknown app)") as String
    }
}