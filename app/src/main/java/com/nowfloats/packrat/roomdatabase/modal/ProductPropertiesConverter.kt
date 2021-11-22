package com.nowfloats.packrat.roomdatabase.modal

import androidx.room.TypeConverter
import com.google.gson.reflect.TypeToken


import com.google.gson.Gson
 import com.nowfloats.packrat.network.RegexApiResponse
import java.lang.reflect.Type


class ProductPropertiesConverter {

    @TypeConverter
    fun stringToMetaDataList(json: String?): List<RegexApiResponse>? {
        val gson = Gson()
        val type: Type = object : TypeToken<List<RegexApiResponse?>?>() {}.type
        return gson.fromJson<List<RegexApiResponse>>(json, type)
    }

    @TypeConverter
    fun stringToMetaData(list: List<RegexApiResponse?>?): String? {
        val gson = Gson()
        val type = object : TypeToken<List<RegexApiResponse?>?>() {}.type
        return gson.toJson(list, type)
    }
}
