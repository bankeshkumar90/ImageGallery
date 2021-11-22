package com.nowfloats.packrat.homescreen

import android.app.Application
import com.nowfloats.packrat.databaserepository.MyRepository
import com.nowfloats.packrat.roomdatabase.DatabaseClass
import android.os.StrictMode
import android.os.StrictMode.VmPolicy


//Application class
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
    }
    val daoClass by lazy {
        val listDatabase = DatabaseClass.getDatabase(this)
        listDatabase.getImageDao()
    }
    val myRepository by lazy {
        MyRepository(daoClass)
    }
}