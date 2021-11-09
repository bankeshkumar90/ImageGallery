package com.nowfloats.packrat.network

import okhttp3.OkHttpClient

import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Network {
    var BASE_URL = "https://imageuploadtogcptriggerbolb20211021131930.azurewebsites.net/api/"
    companion object {
        private val httpLoggingInterceptor = HttpLoggingInterceptor().setLevel(
            HttpLoggingInterceptor.Level.BODY
        )

        val instance: Retrofit
            get() = Retrofit.Builder()
                .baseUrl("https://imageuploadtogcptriggerbolb20211021131930.azurewebsites.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build())
                .build()
    }
}