package com.nowfloats.packrat.roomdatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nowfloats.packrat.addjobs.metaDataBeanItem
import com.nowfloats.packrat.roomdatabase.modal.ProductProperty

//Data Access Object
@Dao
interface DaoClass {

    //adds the image into database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addImage(entityClass: EntityClass)

    /*
   This will return a LiveData<List<EntityClass>> , so whenever the database is changed the observer
   is notified
    */
    @Query("select * from image_table")
    fun getAllImages(): LiveData<List<EntityClass>>

    @Query("select * from image_table")
    fun getSavedImages(): List<EntityClass>


    @Query("select * from image_table where fileUploaded = 0")
    fun getAllSavedImages():LiveData<List<EntityClass>>

    @Query("select * from prod_table")
    fun fetchAllMetaDataInfo(): List<ProductEntityClass>

    @Query("select * from image_table WHERE  CollectionId IN (:CollectionId) AND fileUploaded in (:fileUploaded)")
    fun getJobDoneCollectionIdList( CollectionId:String, fileUploaded : Boolean):  List<EntityClass>

    @Query("select * from image_table WHERE  CollectionId IN (:CollectionId)")
    fun getCollectionIdList( CollectionId:String): List<EntityClass>

    @Query("select * from prod_table WHERE  CollectionId IN (:CollectionId) AND DataUploaded in (:DataUploaded)")
    fun getMetaDataToUpload(CollectionId:String, DataUploaded : Boolean): List<ProductEntityClass>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveMetaDataInfoToRoomDb(productEntityClass: ProductEntityClass)

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


    @Query("UPDATE image_table SET fileUploaded = 1 WHERE path = :imagePath")
    fun updateImageTable(imagePath: String?): Int

    @Query("DELETE FROM image_table WHERE path = :imageId")
    fun deleteImageInfoById(imageId: String)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveProperties(productProperty : ProductProperty)


    @Query("select * from properties WHERE id = 1")
    fun getProperties(): ProductProperty
/*
    @Insert
    fun addProductProperties(properties: ProductProperty)

    @Query("select * from ProductProperty")
    fun getProductProperty(): ProductProperty*/
}