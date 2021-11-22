package com.nowfloats.packrat.network

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import androidx.room.PrimaryKey

class RegexApiResponse : Serializable {
    @SerializedName("name") var name: String? = ""

    @SerializedName("regEx")  var  regEx: String? = ""

    @SerializedName("value")  var  value: String = ""

    override fun toString(): String {
        return "Response{" +
                "message = '" + name + '\'' +
                ",regEx = '" + regEx + '\'' +
                ",value = '" + value + '\''
        "}"
    }
}