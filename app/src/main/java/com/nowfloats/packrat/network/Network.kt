package com.nowfloats.packrat.network

import okhttp3.OkHttpClient

import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class Network {
    var BASE_URL1 = "https://func-packrat-dev-centralin-products.azurewebsites.net/api/func-packrat-dev-cin-getproperties?code=nsmoVaiArlfXhcZGlpwBlXrtHOMTFUAUWfa9pJHsjDlZWZ4q5Aud6w=="
    var BASE_URL = "https://imageuploadtogcptriggerbolb20211021131930.azurewebsites.net/api/"
    companion object {
        private val httpLoggingInterceptor = HttpLoggingInterceptor().setLevel(
            HttpLoggingInterceptor.Level.BODY
        )
          //  .baseUrl("https://imageuploadtogcptriggerbolb20211021131930.azurewebsites.net/api/")
          /*val instance: Retrofit
              get() = Retrofit.Builder()
                  .baseUrl("https://func-packrat-dev-centralin-products.azurewebsites.net/api/")
                  .addConverterFactory(GsonConverterFactory.create())
                  .client(OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build())
                  .build()*/
        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(120, TimeUnit.SECONDS)
            .connectTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .build()
        val instance: Retrofit
            get() = Retrofit.Builder()
                .baseUrl("https://func-packrat-dev-centralin-metadata.azurewebsites.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
    }
}