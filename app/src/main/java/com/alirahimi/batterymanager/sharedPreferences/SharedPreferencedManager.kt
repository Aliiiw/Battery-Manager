package com.alirahimi.batterymanager.sharedPreferences

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencedManager {

    companion object {

        private var sharedPreferenced: SharedPreferences? = null
        private var editor: SharedPreferences.Editor? = null
        private val sharedPreferencedFlag = "SHARED_PREFERENCE_BOOLEAN"
        private val isServiceActive = "isServiceActive"

        fun isServiceOn(context: Context): Boolean? {
            sharedPreferenced =
                context.getSharedPreferences(sharedPreferencedFlag, Context.MODE_PRIVATE)
            return sharedPreferenced?.getBoolean(isServiceActive, false)
        }


        fun setServiceStat(context: Context, isOn: Boolean?) {
            sharedPreferenced =
                context.getSharedPreferences(sharedPreferencedFlag, Context.MODE_PRIVATE)
            editor = sharedPreferenced?.edit()
            editor?.putBoolean(isServiceActive, isOn!!)
            editor?.apply()
        }
    }
}