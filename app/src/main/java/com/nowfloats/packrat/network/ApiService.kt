package com.nowfloats.packrat.network

import retrofit2.http.Multipart
import retrofit2.http.POST
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("UploadImageToGCP?code=DefaultEndpointsProtocol=https;AccountName=stpackratdevcentralin;AccountKey=XvCqqW48CZ860dpdyNBVzbrrBYFmEESbPO/XaKtlu6xp4ixhmyPJoBg4UZ3yVp7RcG1vKmdanmDzLv47ZUZgBQ==;BlobEndpoint=https://stpackratdevcentralin.blob.core.windows.net/;TableEndpoint=https://stpackratdevcentralin.table.core.windows.net/;QueueEndpoint=https://stpackratdevcentralin.queue.core.windows.net/;FileEndpoint=https://stpackratdevcentralin.file.core.windows.net/&clientId=AzureWebJobsStorage")
      fun uploadImage(
        @Part image: MultipartBody.Part,
        @Part("CollectionId") CollectionId: String?
    ): ResponseDTO

}