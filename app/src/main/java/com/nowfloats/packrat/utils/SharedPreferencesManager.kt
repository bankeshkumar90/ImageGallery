package com.nowfloats.packrat.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class SharedPreferencesManager(context: Context) {
    private val privateSharedPreferences: SharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)

    private val gson = Gson()


    private fun getStringFromSharedPreferences(key: String): String {
        return privateSharedPreferences.getString(key, "")!!
    }

    fun saveListInLocal(list: ArrayList<Int>?, key: String?) {
        val editor = privateSharedPreferences.edit()
//        val gson = Gson()
        val json = gson.toJson(list)
        editor.putString(key, json)
        editor.apply() // This line is IMPORTANT !!!
    }

    fun getListFromLocal(key: String?): ArrayList<Int?>? {
//        val gson = Gson()
        val json = privateSharedPreferences.getString(key, null)
        val type: Type = object : TypeToken<ArrayList<Int?>?>() {}.type
        return gson.fromJson(json, type)
    }

    companion object {
        private const val SHARED_PREFERENCES_KEY = "com.pack.rat"
    }
}
