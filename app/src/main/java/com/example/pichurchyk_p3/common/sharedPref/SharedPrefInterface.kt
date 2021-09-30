package com.example.pichurchyk_p3.common.sharedPref

import android.content.Context
import android.content.Context.MODE_PRIVATE

class SharedPrefInterface(context: Context) {

    private val sharedPref = context.getSharedPreferences("queryData", MODE_PRIVATE)

    fun setPref(key: String ,value: Any){
        val editor = sharedPref?.edit()
        when (value) {
            is Int -> editor?.putInt(key, value)
            is String -> editor?.putString(key, value)
            is Boolean -> editor?.putBoolean(key, value)
            is Float -> editor?.putFloat(key, value)
        }
        editor?.apply()
    }

    fun getPrefString(key: String) : Any? {
        return sharedPref?.getString(key, null)
    }
    fun getPrefInt(key: String) : Int? {
        return sharedPref?.getInt(key, 0)
    }
}