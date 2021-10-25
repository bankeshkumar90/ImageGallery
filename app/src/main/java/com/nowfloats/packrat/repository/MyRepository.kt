package com.nowfloats.packrat.repository

import androidx.lifecycle.LiveData
import com.nowfloats.packrat.room.DaoClass
import com.nowfloats.packrat.room.EntityClass

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
}