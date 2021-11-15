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

class MainViewModel  (application: Application, private val repository: MyRepository) : AndroidViewModel(application){

    fun initiateBackGroundProcess(){
        val imageListDetails = getImageProperty()
        val constraints : Constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()
        // use for perioddic tasks
        //val periodicWork = PeriodicWorkRequestBuilder<UploadWorker>(AppConstant.MINIMUM_TIME_INTERVAL,TimeUnit.MINUTES).setConstraints(constaints).build()
        //val oneTimeWork = OneTimeWorkRequestBuilder<UploadWorker>().setConstraints(constraints).build()

        val workManager = WorkManager.getInstance(getApplication())
        for (image in imageListDetails) {
            android.os.Handler().postDelayed({
                processWorkRequest(image.path, image.CollectionId, workManager, constraints )
            }, AppConstant.NETWORK_CALL_DELAY)
        }
    }

    fun getImageProperty(): List<EntityClass>{
        return repository.getAllSavedImages()
    }

    fun processWorkRequest(imagePath:String, collectionId:String, workManager:WorkManager, constraints : Constraints ){
        var inputPutData = Data.Builder().putString(AppConstant.REQUEST_TYPE, imagePath)
            .putString(AppConstant.COLLECTION_ID, collectionId).build()
        val periodicWork = PeriodicWorkRequestBuilder<UploadWorker>(16, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .setInputData(inputPutData)
            .build()

        workManager.enqueueUniquePeriodicWork(
            imagePath,
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWork
        )

        workManager.getWorkInfoByIdLiveData(periodicWork.id)
            .observe(getApplication(), Observer { workInfo ->
                if (workInfo != null) {
                    if (workInfo.state == WorkInfo.State.ENQUEUED) {
                    } else if (workInfo.state == WorkInfo.State.RUNNING) {
                    } else if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                        //update db using another worker thread
                        var idOfWorkInfo =  workInfo.id
                    }
                }
            }
        )
    }
}