package com.nowfloats.packrat.databaserepository

import androidx.lifecycle.LiveData
import com.nowfloats.packrat.addjobs.metaDataBeanItem
import com.nowfloats.packrat.roomdatabase.DaoClass
import com.nowfloats.packrat.roomdatabase.EntityClass
import com.nowfloats.packrat.roomdatabase.ProductFormData
import com.nowfloats.packrat.roomdatabase.productDataInfo

class MyRepository(private val daoClass: DaoClass) {

    //adds the image into the room database
   suspend  fun addImage(entityClass: EntityClass) {
        daoClass.addImage(entityClass)
    }

    //livedata which provides the image present inside database
    fun displayImageDetails(): LiveData<List<EntityClass>> {
        return daoClass.getAllImages()
    }

    //use repository to call dao to delete the previous image_table
    suspend fun deletePreviousImage() {
        daoClass.deleteAllImages()
    }

    //use repository to call dao to delete the previous image_table
     fun deleteDetailById(id:Int) {
        daoClass.deleteById(id)
    }

    //adds the image into the room database
    suspend  fun addProductData(productFormData: ProductFormData) {
        daoClass.addProductData(productFormData)
    }

    suspend fun saveMetaData(productDataInfo: productDataInfo){
        daoClass.saveProductMetaData(productDataInfo)
    }
    suspend fun getAllMetaData():LiveData<List<productDataInfo>>{
        return daoClass.getAllMetaData()
    }
}