package com.nowfloats.packrat.imageviewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nowfloats.packrat.addjobs.MetaDataBeanItem
import com.nowfloats.packrat.databaserepository.MyRepository
import com.nowfloats.packrat.network.ApiService
import com.nowfloats.packrat.network.Network
import com.nowfloats.packrat.network.RegexApiResponse
import com.nowfloats.packrat.roomdatabase.EntityClass
import com.nowfloats.packrat.roomdatabase.ProductEntityClass
import com.nowfloats.packrat.roomdatabase.ProductFormData
import com.nowfloats.packrat.roomdatabase.modal.ProductProperty
import com.nowfloats.packrat.roomdatabase.productDataInfo
import kotlinx.coroutines.*
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
    var entityClassList: ArrayList<EntityClass> = ArrayList<EntityClass>()
    var valueUpdated : LiveData<Boolean> = MutableLiveData()
    //addImage
    suspend fun saveImageInformationToRoomDb(entityClass: EntityClass) {
        repository.addImage(entityClass)
    }

    suspend fun saveMetaDataInformationToRoomDb(metaDataInfo: ProductEntityClass) {
        repository.addMetaDataInfo(metaDataInfo)
    }

    suspend fun fetchMetaDataInformationToRoomDb() :List<ProductEntityClass>{
        return repository.fetchMetaDataInfo()
    }
    //addImage
    fun deleteImageInfoByName(imagePath: String) {
        repository.deleteImageInfoByName(imagePath)
    }
    init {
        System.out.println("message")
    }
    /*//fetch all the data from database using livedata
    fun displayImage(): LiveData<List<EntityClass>> {
        return repository.displayImageDetails()
    }*/

    fun getAllSavedImageInfo(): ArrayList<EntityClass>{
        /*GlobalScope.launch { Dispatchers.IO(){
            entityClassList = repository.getSavedImagesInfo() as ArrayList<EntityClass>
            valueUpdated.value == true
        } }*/
        return  repository.getSavedImagesInfo() as ArrayList<EntityClass>
    }

    fun haveEmptyJobQueued(): Boolean{
        val list = repository.getSavedImagesInfo() as ArrayList<EntityClass>
           return list.isNullOrEmpty()
    }
    fun getSavedImages(): List<EntityClass>{
        viewModelScope.launch() {
             repository.getSavedImages()
        }
        return emptyList()
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

    suspend fun saveMetaData(MetaDataBeanItem: ArrayList<MetaDataBeanItem>){
        try{
            val metadata: productDataInfo = productDataInfo("",MetaDataBeanItem )
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

/*
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
*/

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
                                    try {
                                        productProperty =
                                            response?.body() as ArrayList<RegexApiResponse>
                                        var properties = ProductProperty( response?.body() as List<RegexApiResponse>)
                                        saveProperties(properties)
                                    }catch (e:Exception){
                                        e.printStackTrace()
                                    }
                                }

                             }else if(response.code() ==403){
                                 createMockData()
                            }
                        }
                        override fun onFailure(call: Call<List<RegexApiResponse>?>, t: Throwable) {
                             t.message?.let {
                                Log.e("error:", it)
                            }
                            createMockData()
                        }
                    })
                }
        }catch (e: Exception){
            e.printStackTrace()
            createMockData()
        }
         return productProperty
    }

    private fun createMockData(){
        if(productProperty.size>0)
            return
            var regexApiResponse = RegexApiResponse()
            regexApiResponse.name = "Product Name"
            regexApiResponse.regEx = "^[a-zA-Z0-9 ]{1,50}\$"
            productProperty.add(regexApiResponse)

        var regexApiResponse1 = RegexApiResponse()
         regexApiResponse1.regEx = "^[0-9]*(\\.[0-9][0-9]?)?"
            regexApiResponse1.name = "Quantity"
            productProperty.add(regexApiResponse1)

        var regexApiResponse2 = RegexApiResponse()
        regexApiResponse2.regEx = "^[a-zA-Z0-9 ]{1,50}\$"
            regexApiResponse2.name = "Barcode"
            productProperty.add(regexApiResponse2)

        var regexApiResponse3 = RegexApiResponse()
        regexApiResponse3.regEx = "^[0-9]*(\\.[0-9][0-9]?)?"
            regexApiResponse3.name = "Price"
        regexApiResponse3.value = ""

        productProperty.add(regexApiResponse3)

        var regexApiResponse4 = RegexApiResponse()
        regexApiResponse4.value = ""
        regexApiResponse4.regEx = ""

        regexApiResponse4.name = "Others"
            productProperty.add(regexApiResponse4)
    }

    private fun saveProperties(productPropertyDetail: ProductProperty){
        GlobalScope.launch { Dispatchers.IO(){
            try {
                    var property = repository.getProperties()
                if(property==null)
                    repository.saveProperties(productPropertyDetail)
                else
                    productProperty = property.properies as ArrayList<RegexApiResponse>

            }catch (e:Exception){
                e.printStackTrace()
            }
        } }
    }
    fun getProperties(){
        try{
            GlobalScope.launch { Dispatchers.IO(){
                var property = repository.getProperties()
                if(property==null){
                    fetchFromAPI()
                    return@IO
                }
                productProperty = property.properies as ArrayList<RegexApiResponse>
            } }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
}