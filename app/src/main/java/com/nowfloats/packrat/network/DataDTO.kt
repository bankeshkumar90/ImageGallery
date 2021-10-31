package com.nowfloats.packrat.network

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class DataDTO : Serializable {
    @SerializedName("id")
    val id: String? = null


    @SerializedName("status")
    val status = 0

    @SerializedName("message")
    val message: String? = null

    @SerializedName("link")
    val link: String? = null
    override fun toString(): String {
        return "DataDTO{" +
                "id = '" + id + '\'' +
                "message = '" + message + '\'' +
                ",status = '" + status + '\'' +
                "}"
    }
}