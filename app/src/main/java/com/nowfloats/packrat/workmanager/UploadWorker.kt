package com.nowfloats.packrat.workmanager

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.nowfloats.packrat.network.ApiResponse
import com.nowfloats.packrat.network.ApiService
import com.nowfloats.packrat.network.Network
import com.nowfloats.packrat.utils.AppConstant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class UploadWorker(private val appContext: Context,private val workerParameters: WorkerParameters): CoroutineWorker(appContext, workerParameters) {
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                var outPutData = Data.Builder()
                    .putString("TAG", "uploadImage")
                    .build()

                val getData = workerParameters.inputData
                val imagePath = getData.getString(AppConstant.REQUEST_TYPE)
                val collectionId = getData.getString(AppConstant.COLLECTION_ID)

                var imageUploaded = uploadImage(imagePath!!, collectionId!!)
                Result.success(outPutData)
            } catch (error: Throwable) {
                Result.failure()
            }
        }
    }

    fun uploadImage(imagePath:String , collectionId:String): Boolean{
        val imageUploaded = false
        val apiService = Network.instance.create(
            ApiService::class.java
        )
        val file = File(imagePath)
        val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        //val part: MultipartBody.Part = MultipartBody.Part.createFormData("file", file.name, requestBody)
        // MultipartBody.Part is used to send also the actual file name
        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("file", file.name, requestBody)

        // add another part within the multipart request
        val description = RequestBody.create(
            MultipartBody.FORM, collectionId
        )
        var outPutData = Data.Builder()
            .putString("TAG", "currentDate")
            .build()
        // finally, execute the request
        val call: Call<ApiResponse?>? = apiService.upload(description, body)
        call?.enqueue(object : Callback<ApiResponse?> {
            override fun onResponse(
                call: Call<ApiResponse?>,
                response: Response<ApiResponse?>
            ) {
                Log.v("Upload", "success")
                if (response.code()==200){
                    //outPutData
                    Result.success(outPutData)
                }
            }

            override fun onFailure(call: Call<ApiResponse?>, t: Throwable) {
                t.message?.let {
                    Log.e("Upload error:", it)
                }
                Result.failure()
            }
        })
        return  imageUploaded
    }
}