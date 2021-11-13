package com.nowfloats.packrat.roomdatabase.modal

import androidx.room.TypeConverter
import com.google.gson.reflect.TypeToken

import com.google.gson.Gson
import com.nowfloats.packrat.addjobs.metaDataBeanItem
import java.lang.reflect.Type


class ProductPropertiesConverter {

    @TypeConverter
    fun stringToMetaDataList(json: String?): List<ProductProperty>? {
        val gson = Gson()
        val type: Type = object : TypeToken<List<ProductProperty?>?>() {}.type
        return gson.fromJson<List<ProductProperty>>(json, type)
    }

    @TypeConverter
    fun stringToMetaData(list: List<ProductProperty?>?): String? {
        val gson = Gson()
        val type = object : TypeToken<List<ProductProperty?>?>() {}.type
        return gson.toJson(list, type)
    }
}
