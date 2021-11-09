package com.nowfloats.packrat.roomdatabase

import androidx.room.TypeConverter
import com.google.gson.reflect.TypeToken

import com.google.gson.Gson
import com.nowfloats.packrat.addjobs.metaDataBeanItem
import java.lang.reflect.Type


class ProductTypeConverter {

    @TypeConverter
    fun stringToMetaDataList(json: String?): List<metaDataBeanItem>? {
        val gson = Gson()
        val type: Type = object : TypeToken<List<metaDataBeanItem?>?>() {}.type
        return gson.fromJson<List<metaDataBeanItem>>(json, type)
    }

    @TypeConverter
    fun stringToMetaData(list: List<metaDataBeanItem?>?): String? {
        val gson = Gson()
        val type = object : TypeToken<List<metaDataBeanItem?>?>() {}.type
        return gson.toJson(list, type)
    }
}
