package com.nowfloats.packrat.homescreen

import android.app.Application
import com.nowfloats.packrat.databaserepository.MyRepository
import com.nowfloats.packrat.roomdatabase.DatabaseClass

//Application class
class MyApplication : Application() {
    val daoClass by lazy {
        val listDatabase = DatabaseClass.getDatabase(this)
        listDatabase.getImageDao()
    }
    val myRepository by lazy {
        MyRepository(daoClass)
    }
}