package com.nowfloats.packrat.roomdatabase

import androidx.room.TypeConverter
import com.google.gson.reflect.TypeToken

import com.google.gson.Gson
import com.nowfloats.packrat.addjobs.MetaDataBeanItem
import java.lang.reflect.Type


class ProductTypeConverter {

    @TypeConverter
    fun stringToMetaDataList(json: String?): List<MetaDataBeanItem>? {
        val gson = Gson()
        val type: Type = object : TypeToken<List<MetaDataBeanItem?>?>() {}.type
        return gson.fromJson<List<MetaDataBeanItem>>(json, type)
    }

    @TypeConverter
    fun stringToMetaData(list: List<MetaDataBeanItem?>?): String? {
        val gson = Gson()
        val type = object : TypeToken<List<MetaDataBeanItem?>?>() {}.type
        return gson.toJson(list, type)
    }
}
