package com.nowfloats.packrat.home

import android.app.Application
import com.nowfloats.packrat.repository.MyRepository
import com.nowfloats.packrat.room.DatabaseClass

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