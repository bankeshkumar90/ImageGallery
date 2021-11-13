package com.nowfloats.packrat.imageViewModel

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.nowfloats.packrat.addjobs.metaDataBeanItem
import com.nowfloats.packrat.databaserepository.MyRepository
import com.nowfloats.packrat.network.ApiResponse
import com.nowfloats.packrat.network.ApiService
import com.nowfloats.packrat.network.Network
import com.nowfloats.packrat.network.RegexApiResponse
import com.nowfloats.packrat.roomdatabase.EntityClass
import com.nowfloats.packrat.roomdatabase.ProductFormData
import com.nowfloats.packrat.roomdatabase.modal.ProductProperty
import com.nowfloats.packrat.roomdatabase.productDataInfo
import com.nowfloats.packrat.utils.AppConstant
import kotlinx.coroutines.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.Exception

/**
 * This is a VM layer in the `MVVM` architecture, where we are notifying the Activity/view about the
 * response changes via live data
 */
class MyViewModel(private val repository: MyRepository) : ViewModel() {
    var imageArrayList: LiveData<ArrayList<String>> = MutableLiveData()
    var imageList = ArrayList<String>()
    var  productProperty: ArrayList<RegexApiResponse> = ArrayList<RegexApiResponse>()

    //addImage
    suspend fun addImage(entityClass: EntityClass) {
        repository.addImage(entityClass)
    }
    init {
        System.out.println("message")
    }
    //fetch all the data from database using livedata
    fun displayImage(): LiveData<List<EntityClass>> {
        return repository.displayImageDetails()
    }

    //deletes all the record from database
    suspend fun dclickaddeleteImage() {
        repository.deletePreviousImage()
    }

     fun deleteImageById(id:Int) {
        repository.deleteDetailById(id)
    }

    //addImage
    suspend fun addProductData(productFormData: ProductFormData) {
        repository.addProductData(productFormData)
    }

    fun displayImageData(): LiveData<ArrayList<String>> {
        return imageArrayList
    }
    fun addImageToList(imagePath:String){
        imageList.add(imagePath)
        imageArrayList.value?.add(imagePath)
    }

    suspend fun saveMetaData(metaDataBeanItem: ArrayList<metaDataBeanItem>){
        try{
            val metadata: productDataInfo = productDataInfo("",metaDataBeanItem )
            repository.saveMetaData(metadata)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    suspend fun getMetaData(): LiveData<List<productDataInfo>>? {
        try{
            val result = repository.getAllMetaData()
            return result
        }catch (e:Exception){
            e.printStackTrace()
        }
        return null
    }
    fun clearViewModelData(){
        imageList.clear()
        imageArrayList.value?.clear()
    }

    @SuppressLint("NewApi")
     fun getProductProperties(application: Application){
        try{
            runBlocking {
                withContext(Dispatchers.IO) {
                        if(AppConstant().isOnline(application.applicationContext)){
                            fetchFromAPI()
                        }else{
                            Toast.makeText(application.applicationContext, AppConstant.INTERNET_CONNECTION_MESSAGE, Toast.LENGTH_SHORT).show()
                        }
                }
            }

        }catch (e:Exception){
            e.printStackTrace()
        }
    }

     fun fetchFromAPI(): ArrayList<RegexApiResponse>{
        try {
                val apiService = Network.instance.create(ApiService::class.java)
                GlobalScope.launch(Dispatchers.Main) {
                    val call: Call<List<RegexApiResponse>?>? = apiService.getProperties()
                    call?.enqueue(object : Callback<List<RegexApiResponse>?> {
                        override fun onResponse(
                            call: Call<List<RegexApiResponse>?>,
                            response: Response<List<RegexApiResponse>?>
                        ) {
                            if(response.code()==200) {
                                if (response?.body() != null) {
                                    productProperty =
                                        response?.body() as ArrayList<RegexApiResponse>
                                }

                             }else if(response.code() ==403){
                                 createMockData()
                            }
                        }
                        override fun onFailure(call: Call<List<RegexApiResponse>?>, t: Throwable) {
                             t.message?.let {
                                Log.e("error:", it)
                            }
                        }
                    })
                }
        }catch (e: Exception){
            e.printStackTrace()
        }
         return productProperty
    }

    private fun createMockData(){
        if(productProperty.size>0)
            return
            var regexApiResponse = RegexApiResponse()
            regexApiResponse.name = "Product"
            regexApiResponse.value = ""
            productProperty.add(regexApiResponse)

        var regexApiResponse1 = RegexApiResponse()
         regexApiResponse1.value = ""
            regexApiResponse1.name = "Quantity"
            productProperty.add(regexApiResponse1)

        var regexApiResponse2 = RegexApiResponse()
        regexApiResponse2.value = ""
            regexApiResponse2.name = "Barcode"
            productProperty.add(regexApiResponse2)

        var regexApiResponse3 = RegexApiResponse()
        regexApiResponse3.value = ""
            regexApiResponse3.name = "Price"
            productProperty.add(regexApiResponse3)

        var regexApiResponse4 = RegexApiResponse()
        regexApiResponse4.value = ""
            regexApiResponse4.name = "Others"
            productProperty.add(regexApiResponse4)
    }

}