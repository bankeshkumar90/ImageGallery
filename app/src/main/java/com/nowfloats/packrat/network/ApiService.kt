package com.nowfloats.packrat.network

import com.google.gson.JsonObject
import com.nowfloats.packrat.roomdatabase.modal.ProductProperty
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*


interface ApiService {
    //https://func-packrat-dev-centralin-metadata.azurewebsites.net/api/ImageToGCP?code=ZH0GdGSUGa9/kl7OtQaLhmhRfcz394MZEfzZWYu6hKL1kcb644pDmA==
    //https://func-packrat-dev-centralin-metadata.azurewebsites.net/api/Property?code=k6aTPm3JOoaXylckamOf9mxMp1VyzCZ1SRpa5AacrCxCYgHPXqTpBw==
    //https://func-packrat-dev-centralin-products.azurewebsites.net/api/func-packrat-dev-cin-uploadimagetogcp?code=AItjXopPWc7CEry8svIlhizPNzSDpasAZaUkIvmbxU2MGDxi7X9I6A==
    @Multipart
    @POST("ImageToGCP?code=ZH0GdGSUGa9/kl7OtQaLhmhRfcz394MZEfzZWYu6hKL1kcb644pDmA==")
    fun upload(
        @Part("CollectionId") description: RequestBody?,
        @Part file: MultipartBody.Part
    ): Call<ApiResponse?>?
    //https://func-packrat-dev-centralin-metadata.azurewebsites.net/api/MetadataSave?code=HN5J7tamGmRYkAnHlsOmdNS1yv4qU4NNaHWSbwvRrz0ZsLq6cUKdzA==
    //https://func-packrat-dev-centralin-products.azurewebsites.net/api/func-packrat-dev-cin-uploaddataindb?code=SNFj4TmodhuVY6opiSmoGY7TNGCzLgDyYIdx8NbMgroxX4Wh0Ru4gA==
    @POST("MetadataSave?code=HN5J7tamGmRYkAnHlsOmdNS1yv4qU4NNaHWSbwvRrz0ZsLq6cUKdzA==")
    fun saveMetaDataToServer(
        @Body jsonObj: JsonObject,
        ): Call<ApiResponse?>?

    //https://func-packrat-dev-centralin-metadata.azurewebsites.net/api/
    @GET("Property?code=k6aTPm3JOoaXylckamOf9mxMp1VyzCZ1SRpa5AacrCxCYgHPXqTpBw==")
    fun getProperties(): Call<List<RegexApiResponse>?>?
}