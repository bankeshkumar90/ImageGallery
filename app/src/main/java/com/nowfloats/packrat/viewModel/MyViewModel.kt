package com.nowfloats.packrat.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.nowfloats.packrat.repository.MyRepository
import com.nowfloats.packrat.room.EntityClass

/**
 * This is a VM layer in the `MVVM` architecture, where we are notifying the Activity/view about the
 * response changes via live data
 */
class MyViewModel(private val repository: MyRepository) : ViewModel() {

    //addImage
    suspend fun addImage(entityClass: EntityClass) {
        repository.addImage(entityClass)
    }

    //fetch all the data from database using livedata
    fun displayImage(): LiveData<List<EntityClass>> {
        return repository.displayImageDetails()
    }

    //deletes all the record from database
    suspend fun deleteImage() {
        repository.deletePreviousImage()
    }
}