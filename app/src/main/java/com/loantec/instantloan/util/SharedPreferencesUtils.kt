package com.loantec.instantloan.util

import android.annotation.SuppressLint
import android.content.Context

class SharedPreferencesUtils {

    private val FILE_NAME = "cash"

    @SuppressLint("CommitPrefEdits")
    fun saveData(context: Context, key: String, data: Any) {
        val type = data.javaClass.simpleName
        val preferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        if ("Integer" == type) {
            editor.putInt(key, data as Int)
        } else if ("Boolean" == type) {
            editor.putBoolean(key, data as Boolean)
        } else if ("String" == type) {
            editor.putString(key, data as String)
        } else if ("Float" == type) {
            editor.putFloat(key, data as Float)
        } else if ("Long" == type) {
            editor.putLong(key, data as Long)
        }
        editor.apply()
    }


    fun getData(context: Context, key: String, defValue: Any): Any? {

        val type = defValue.javaClass.simpleName
        val sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)


        if ("Integer" == type) {
            return sharedPreferences.getInt(key, defValue as Int)
        } else if ("Boolean" == type) {
            return sharedPreferences.getBoolean(key, defValue as Boolean)
        } else if ("String" == type) {
            return sharedPreferences.getString(key, defValue as String)
        } else if ("Float" == type) {
            return sharedPreferences.getFloat(key, defValue as Float)
        } else if ("Long" == type) {
            return sharedPreferences.getLong(key, defValue as Long)
        }

        return ""
    }
}