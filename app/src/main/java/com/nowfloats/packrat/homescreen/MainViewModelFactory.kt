package com.nowfloats.packrat.homescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nowfloats.packrat.databaserepository.MyRepository

class MainViewModelFactory(val myApplication: MyApplication,private val myRepository: MyRepository,) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(myApplication, myRepository) as T
    }
}