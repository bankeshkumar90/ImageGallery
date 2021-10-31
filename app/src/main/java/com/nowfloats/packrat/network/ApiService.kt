package com.nowfloats.packrat.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface ApiService {
    @Multipart
    @POST("UploadImageToGCP?code=DefaultEndpointsProtocol=https;AccountName=stpackratdevcentralin;AccountKey=XvCqqW48CZ860dpdyNBVzbrrBYFmEESbPO/XaKtlu6xp4ixhmyPJoBg4UZ3yVp7RcG1vKmdanmDzLv47ZUZgBQ==;BlobEndpoint=https://stpackratdevcentralin.blob.core.windows.net/;TableEndpoint=https://stpackratdevcentralin.table.core.windows.net/;QueueEndpoint=https://stpackratdevcentralin.queue.core.windows.net/;FileEndpoint=https://stpackratdevcentralin.file.core.windows.net/&clientId=AzureWebJobsStorage")
    fun uploadImage(
        @Part image: MultipartBody.Part,
        @Part("CollectionId") CollectionId: String?
    ): Call<ResponseDTO?>?


    @Multipart
    @POST("UploadImageToGCP?code=DefaultEndpointsProtocol=https;AccountName=stpackratdevcentralin;AccountKey=XvCqqW48CZ860dpdyNBVzbrrBYFmEESbPO/XaKtlu6xp4ixhmyPJoBg4UZ3yVp7RcG1vKmdanmDzLv47ZUZgBQ==;BlobEndpoint=https://stpackratdevcentralin.blob.core.windows.net/;TableEndpoint=https://stpackratdevcentralin.table.core.windows.net/;QueueEndpoint=https://stpackratdevcentralin.queue.core.windows.net/;FileEndpoint=https://stpackratdevcentralin.file.core.windows.net/&clientId=AzureWebJobsStorage")
    fun upload(
        @Part("CollectionId") description: RequestBody?,
        @Part file: MultipartBody.Part
    ): Call<ResponseBody?>?

}