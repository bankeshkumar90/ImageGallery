package com.nowfloats.packrat.workmanager

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.nowfloats.packrat.homescreen.MyApplication
import com.nowfloats.packrat.network.ApiResponse
import com.nowfloats.packrat.network.ApiService
import com.nowfloats.packrat.network.Network
import com.nowfloats.packrat.roomdatabase.DaoClass
import com.nowfloats.packrat.roomdatabase.DatabaseClass
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

class MetaDataWorker(private val appContext: Context, private val workerParameters: WorkerParameters): CoroutineWorker(appContext, workerParameters) {

    override suspend fun doWork(): Result {

        return withContext(Dispatchers.IO) {
            Log.v("metaData init", "")

            val getData = workerParameters.inputData
            val inputStringToSave = getData.getString(AppConstant.REQUEST_META_DATA)
            Log.v("metaData info", "$inputStringToSave")

            try {
                val collectionId = getData.getString(AppConstant.COLLECTION_ID)
                Log.v("collectionId info", "$collectionId")

                var metaDataUploaded = saveMetaDataToServer(inputStringToSave!!, collectionId!!)
                var outPutData = Data.Builder()
                    .putString(AppConstant.REQUEST_META_DATA, inputStringToSave)
                    .putBoolean(AppConstant.META_DATA_SAVED_ON_SERVER, metaDataUploaded)
                    .build()
                Result.success(outPutData)
            } catch (error: Throwable) {
                var outPutData = Data.Builder()
                    .putString(AppConstant.REQUEST_META_DATA, inputStringToSave)
                    .putBoolean(AppConstant.META_DATA_SAVED_ON_SERVER, false)
                    .build()
                Result.failure(outPutData)
            }
        }
    }

    fun saveMetaDataToServer(requestedString:String , collectionId:String): Boolean{
        val imageUploaded = false
        val jsonParser = JsonParser()
        var gsonObject = JsonObject()
        val apiService = Network.instance.create(
            ApiService::class.java
        )
        Log.v("requestedString", ""+requestedString)

        gsonObject = jsonParser.parse(requestedString) as JsonObject

        val call: Call<ApiResponse?>? = apiService.saveMetaDataToServer(gsonObject)
        Log.v("gsonObject", "$gsonObject")

        call?.enqueue(object : Callback<ApiResponse?> {
            override fun onResponse(
                call: Call<ApiResponse?>,
                response: Response<ApiResponse?>
            ) {
                println("MetaData success")
                Log.v("MetaData", ""+response?.body()?.message)
                Log.v("MetaData", "success")

             }

            override fun onFailure(call: Call<ApiResponse?>, t: Throwable) {
                 t.message?.let {
                    Log.e("MetaData error:", it)
                }            }

        })
        return  imageUploaded
    }

}