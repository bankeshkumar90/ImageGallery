package com.nowfloats.packrat.imageViewModel

import android.os.Process
import android.util.Log
import android.util.Log.DEBUG
import android.util.Log.INFO
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nowfloats.packrat.databaserepository.MyRepository
import com.nowfloats.packrat.roomdatabase.EntityClass
import com.nowfloats.packrat.roomdatabase.ProductFormData
import java.util.logging.Level.INFO

/**
 * This is a VM layer in the `MVVM` architecture, where we are notifying the Activity/view about the
 * response changes via live data
 */
class MyViewModel(private val repository: MyRepository) : ViewModel() {
    var imageArrayList: LiveData<ArrayList<String>> = MutableLiveData()
    var imageList = ArrayList<String>()
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
    suspend fun deleteImage() {
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
}