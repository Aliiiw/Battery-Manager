package com.alirahimi.batterymanager.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alirahimi.batterymanager.R
import com.alirahimi.batterymanager.model.BatteryModel

class BatteryUsageAdapter(private val battery: MutableList<BatteryModel>) :
    RecyclerView.Adapter<BatteryUsageAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BatteryUsageAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.item_battery_usage, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: BatteryUsageAdapter.ViewHolder, position: Int) {
        holder.test.text = "${battery[position].packageName} : ${battery[position].percentUsage}"
    }

    override fun getItemCount(): Int {
        return battery.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var test: TextView = view.findViewById(R.id.testHeader)
    }

}