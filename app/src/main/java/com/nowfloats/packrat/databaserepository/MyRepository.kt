package com.nowfloats.packrat.databaserepository

import androidx.lifecycle.LiveData
import com.nowfloats.packrat.roomdatabase.DaoClass
import com.nowfloats.packrat.roomdatabase.EntityClass

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
}