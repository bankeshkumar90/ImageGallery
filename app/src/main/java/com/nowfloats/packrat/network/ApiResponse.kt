package com.nowfloats.packrat.network

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ApiResponse : Serializable {
    @SerializedName("message")
    val message: String? = ""

    @SerializedName("collectionId")
    val title: String? = ""

    override fun toString(): String {
        return "Response{" +
                "message = '" + message + '\'' +
                ",collectionId = '" + title + '\''
                "}"
    }
}