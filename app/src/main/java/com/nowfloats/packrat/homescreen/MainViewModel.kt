package com.nowfloats.packrat.homescreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.work.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.nowfloats.packrat.databaserepository.MyRepository
import com.nowfloats.packrat.roomdatabase.EntityClass
import java.util.concurrent.TimeUnit
import com.nowfloats.packrat.utils.AppConstant
import com.nowfloats.packrat.workmanager.UploadWorker
import java.util.logging.Handler
import androidx.work.WorkManager
import com.google.common.util.concurrent.ListenableFuture
import java.util.*


class MainViewModel  (application: Application, private val repository: MyRepository) : AndroidViewModel(application){
    lateinit var app:Application
    init {
        app = application
    }

    fun initiateBackGroundProcess1(imageListDetails: List<EntityClass>){
        val constraints : Constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()
        // use for perioddic tasks
        //val periodicWork = PeriodicWorkRequestBuilder<UploadWorker>(AppConstant.MINIMUM_TIME_INTERVAL,TimeUnit.MINUTES).setConstraints(constaints).build()
        //val oneTimeWork = OneTimeWorkRequestBuilder<UploadWorker>().setConstraints(constraints).build()

        val workManager = WorkManager.getInstance(getApplication())
        for (image in imageListDetails) {
            processWorkRequest(image.path, image.CollectionId, workManager, constraints )
        }
    }


    fun getUnSyncedSavedImageProperty(): LiveData<List<EntityClass>> {
        return repository.getAllSavedImages()
    }

    fun processWorkRequest(imagePath:String, collectionId:String, workManager:WorkManager, constraints : Constraints ){
        var inputPutData = Data.Builder().putString(AppConstant.REQUEST_TYPE, imagePath)
            .putString(AppConstant.COLLECTION_ID, collectionId).build()
        var TAG = AppConstant().getLastStringAfter("%", imagePath)
        val periodicWork = PeriodicWorkRequestBuilder<UploadWorker>(16, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .setInputData(inputPutData)
            .addTag(TAG)
            .build()

        val future: ListenableFuture<List<WorkInfo>> = workManager.getWorkInfosByTag(TAG)
        val list: List<WorkInfo> = future.get()
        // start only if no such tasks present
        // start only if no such tasks present
        if (list == null || list.size == 0) {
            workManager.enqueueUniquePeriodicWork(
                imagePath,
                ExistingPeriodicWorkPolicy.KEEP,
                periodicWork
            )
        }

    }
}

private fun <T> LiveData<T>.observe(application: Application, observer: Observer<T>) {

}
