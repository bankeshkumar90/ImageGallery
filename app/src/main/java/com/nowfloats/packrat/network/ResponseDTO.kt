package com.nowfloats.packrat.network

import com.google.gson.annotations.SerializedName
import java.io.Serializable



class ResponseDTO : Serializable {
    @SerializedName("data")
    val data: DataDTO? = null

    @SerializedName("success")
    val isSuccess = false

    @SerializedName("status")
    val status = 0

    @SerializedName("message")
    private val message: String? = null

    @SerializedName("CollectionId")
    private val CollectionId: String? = null
    override fun toString(): String {
        return "Response{" +
                "data = '" + data + '\'' +
                ",success = '" + message + '\'' +
                ",status = '" + CollectionId + '\'' +
                "}"
    }
}