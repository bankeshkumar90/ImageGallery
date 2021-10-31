package com.nowfloats.packrat.network

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ResponseDTO : Serializable {
    @SerializedName("data")
    val data: DataDTO? = null


    @SerializedName("message")
     val message: String? = null


    override fun toString(): String {
        return "Response{" +
                "data = '" + data + '\'' +
                ",message = '" + message + '\'' +
                "}"
    }
}