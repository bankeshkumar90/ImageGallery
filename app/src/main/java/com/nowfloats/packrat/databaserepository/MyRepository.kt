package com.nowfloats.packrat.databaserepository

import androidx.lifecycle.LiveData
import com.nowfloats.packrat.addjobs.metaDataBeanItem
import com.nowfloats.packrat.network.RegexApiResponse
import com.nowfloats.packrat.roomdatabase.*
import com.nowfloats.packrat.roomdatabase.modal.ProductProperty

class MyRepository(private val daoClass: DaoClass) {

    //adds the image into the room database
   suspend  fun addImage(entityClass: EntityClass) {
        daoClass.addImage(entityClass)
    }

    suspend fun addMetaDataInfo(productEntityClass: ProductEntityClass){
        daoClass.saveMetaDataInfoToRoomDb(productEntityClass)
    }
    suspend fun fetchMetaDataInfo(): List<ProductEntityClass>{
        return daoClass.fetchAllMetaDataInfo()
    }

    //livedata which provides the image present inside database
    fun displayImageDetails(): LiveData<List<EntityClass>> {
        return daoClass.getAllImages()
    }

    fun getAllSavedImages(): List<EntityClass> {
        return daoClass.getAllSavedImages()
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

    /*suspend fun getProductProperties(): ProductProperty{
        return daoClass.getProductProperty()
    }*/
}