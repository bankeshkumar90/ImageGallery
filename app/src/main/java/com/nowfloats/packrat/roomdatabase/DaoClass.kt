package com.nowfloats.packrat.roomdatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nowfloats.packrat.addjobs.metaDataBeanItem

//Data Access Object
@Dao
interface DaoClass {

    //adds the image into database
    @Insert
    fun addImage(entityClass: EntityClass)

    /*
   This will return a LiveData<List<EntityClass>> , so whenever the database is changed the observer
   is notified
    */
    @Query("select * from image_table")
    fun getAllImages(): LiveData<List<EntityClass>>


    //delete previous data present in database
    @Query("DELETE FROM image_table")
    fun deleteAllImages()

    @Query("DELETE FROM image_table WHERE id = :id")
    fun deleteById(id: Int)


    @Insert
    fun addProductData(productFormData: ProductFormData)

    @Insert
    fun saveProductMetaData(productMetaData: productDataInfo)

    @Query("select * from productInfo")
    fun getAllMetaData(): LiveData<List<productDataInfo>>
}