package com.nowfloats.packrat.addjobs

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.work.*
import com.google.common.util.concurrent.ListenableFuture
import com.nowfloats.packrat.roomdatabase.ProductFormData
import com.nowfloats.packrat.utils.AppConstant
import com.nowfloats.packrat.workmanager.MetaDataWorker
import com.nowfloats.packrat.workmanager.UploadWorker
import java.io.Serializable
import java.lang.Exception

class AddProductViewModel(application: Application) : AndroidViewModel(application), Serializable {
    var clickadd: MutableLiveData<Int> = MutableLiveData()
    var addBottomClick: MutableLiveData<MetaDataBeanItem> = MutableLiveData()
    var addBottomClickPosition: MutableLiveData<Int> = MutableLiveData()
    var clickdeleteview: MutableLiveData<Int> = MutableLiveData()
    var deleteFormViews: MutableLiveData<Int> = MutableLiveData()
    var getProductData: MutableLiveData<ProductFormData> = MutableLiveData()
    var saveMetaData: MutableLiveData<Boolean> = MutableLiveData()
    var fragmentMapObj:HashMap<Int, ArrayList<MetaDataBeanItem>> = HashMap<Int, ArrayList<MetaDataBeanItem>>()
    var imageProcessed: MutableLiveData<Int> = MutableLiveData()
    var metaDataTobeProcessed = ""

    fun deleteViewOnClick(position: Int) {
        clickdeleteview.value = position

    }

    fun addViewOnClick() {
        clickadd.value = 1
    }

    fun imageProcessed(){
        imageProcessed.postValue(1)
    }

    fun bottomDialogClick(item: MetaDataBeanItem, position: Int) {
        addBottomClick.value = item
        addBottomClickPosition.value = position
    }

    fun deleteAddFormViews() {
        deleteFormViews.value
    }

    fun getProductFormData(productFormData: ProductFormData) {
        getProductData.value = productFormData
    }

    fun saveFormMetaData() {
        saveMetaData.value = true
    }

    fun updateFragmentIndex(tabPosition: Int, fragmentObj: ArrayList<MetaDataBeanItem>){
        //fragmentMapObj.put(tabPosition, fragmentObj)
    }

    fun deletFragmentData(deletedTabPos:Int, totalRemaining:Int): Int{
        var calculateHeaderMap:HashMap<Int, ArrayList<MetaDataBeanItem>> = HashMap<Int, ArrayList<MetaDataBeanItem>>()
        try {
            fragmentMapObj.get(deletedTabPos)?.clear()
            if(fragmentMapObj.size>0){
                    if(deletedTabPos==0){
                        for ((key, value) in fragmentMapObj) {
                            if(key!=0) {
                                calculateHeaderMap.put(key -1 , value)
                            }
                        }
                    }else if(deletedTabPos == totalRemaining){
                        for ((key, value) in fragmentMapObj) {
                            if(key!=totalRemaining) {
                                if(key > deletedTabPos)
                                    calculateHeaderMap.put(key -1 , value)
                                else
                                    calculateHeaderMap.put(key , value)
                            }
                        }
                    }else if(deletedTabPos < totalRemaining){
                        for ((key, value) in fragmentMapObj) {
                            if(key!= deletedTabPos) {
                                if(key > deletedTabPos)
                                    calculateHeaderMap.put(key -1 , value)
                                else
                                    calculateHeaderMap.put(key , value)
                            }
                        }
                    }


            }
        }catch (e:Exception){
            e.printStackTrace()
        }
        finally {
            fragmentMapObj = calculateHeaderMap
        }

        return fragmentMapObj.size
    }
    fun clearFragmentData(){
        fragmentMapObj.clear()
    }

    fun deleteFragmentObjectItem(selcetedPosition: Int, itemDeletPosition: Int){
        try {
            if(fragmentMapObj.size>0){
                fragmentMapObj.get(selcetedPosition)?.removeAt(itemDeletPosition)
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    fun initiateBackGroundProcess(imageList: ArrayList<String>, CollectionId:String, metaDataString: String){
        val constraints : Constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)

            .setRequiresBatteryNotLow(true)
            .build()
        // use for perioddic tasks
        //val periodicWork = PeriodicWorkRequestBuilder<UploadWorker>(AppConstant.MINIMUM_TIME_INTERVAL,TimeUnit.MINUTES).setConstraints(constaints).build()
        //val oneTimeWork = OneTimeWorkRequestBuilder<UploadWorker>().setConstraints(constraints).build()

        val workManager = WorkManager.getInstance(getApplication())
        val paralleWorks = mutableListOf<OneTimeWorkRequest>()
        for (image in imageList) {
            var inputPutData = Data.Builder().putString(AppConstant.REQUEST_TYPE, image)
                .putString(AppConstant.COLLECTION_ID, CollectionId).build()
            var TAG = AppConstant().getLastStringAfter("%", image)
            val workRequest = OneTimeWorkRequestBuilder<UploadWorker>()
                .setConstraints(constraints)
                .setInputData(inputPutData)
                .addTag(TAG)
                .build()
            paralleWorks.add(workRequest)
            //processWorkRequest(image, CollectionId, workManager, constraints )
        }
        var inputPutData = Data.Builder().putString(AppConstant.REQUEST_META_DATA, metaDataString)
            .putString(AppConstant.COLLECTION_ID, CollectionId).build()
        val metaDataWork = OneTimeWorkRequestBuilder<MetaDataWorker>()
            .setConstraints(constraints)
            .setInputData(inputPutData)
            .addTag(CollectionId)
            .build()
        workManager
            .beginWith(paralleWorks)
            .then(metaDataWork)
            .enqueue()

        //processMetaDataWorkRequest(metaDataString,CollectionId, workManager, constraints)
    }

    fun processMetaDataWorkRequest(metaDataString:String, collectionId:String, workManager: WorkManager, constraints : Constraints){
        var inputPutData = Data.Builder().putString(AppConstant.REQUEST_META_DATA, metaDataString)
            .putString(AppConstant.COLLECTION_ID, collectionId).build()
        val oneTimeWork = OneTimeWorkRequestBuilder<MetaDataWorker>()
            .setConstraints(constraints)
            .setInputData(inputPutData)
            .addTag(collectionId)
            .build()

        /*val periodicWork = OneTimeWorkRequestBuilder<MetaDataWorker>(17, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .setInputData(inputPutData)
            .addTag(collectionId)
            .build()*/

        val future: ListenableFuture<List<WorkInfo>> = workManager.getWorkInfosByTag(collectionId)
        val list: List<WorkInfo> = future.get()
        if (list == null || list.size == 0) {
            workManager.enqueueUniqueWork(
                collectionId,
                ExistingWorkPolicy.KEEP,
                oneTimeWork
            )
        }

    }
    fun processWorkRequest(imagePath:String, collectionId:String, workManager: WorkManager, constraints : Constraints){
        var inputPutData = Data.Builder().putString(AppConstant.REQUEST_TYPE, imagePath)
            .putString(AppConstant.COLLECTION_ID, collectionId).build()
        var TAG = AppConstant().getLastStringAfter("%", imagePath)
        val retryWorker = OneTimeWorkRequestBuilder<UploadWorker>()
            .setConstraints(constraints)
            .setInputData(inputPutData)
            .addTag(TAG)
            .build()

        val future: ListenableFuture<List<WorkInfo>> = workManager.getWorkInfosByTag(TAG)
        val list: List<WorkInfo> = future.get()
        if (list == null || list.size == 0) {
            workManager.enqueueUniqueWork(
                imagePath,
                ExistingWorkPolicy.KEEP,
                retryWorker
            )
        }

    }

}

private fun <T> LiveData<T>.observe(application: Application, observer: Observer<T>) {

}