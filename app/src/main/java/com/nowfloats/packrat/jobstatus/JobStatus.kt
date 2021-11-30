package com.nowfloats.packrat.jobstatus

import android.app.ProgressDialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.nowfloats.packrat.R
import com.nowfloats.packrat.databaserepository.MyRepository
import com.nowfloats.packrat.homescreen.MyApplication
import com.nowfloats.packrat.imageviewmodel.MyViewModel
import com.nowfloats.packrat.imageviewmodel.ViewModelFactory
import com.nowfloats.packrat.roomdatabase.EntityClass
import com.nowfloats.packrat.utils.AppConstant
import kotlinx.android.synthetic.main.fragment_image_preview.*
import kotlinx.android.synthetic.main.fragment_job_status.*
import java.util.*
import kotlin.collections.ArrayList
import androidx.work.WorkInfo

import androidx.work.WorkManager
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.android.synthetic.main.gallery_image_picker.*
import kotlinx.coroutines.*
import java.util.concurrent.ExecutionException


class JobStatus : Fragment()  {

    lateinit var viewModel: MyViewModel
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var myApplication: MyApplication
    lateinit var myRepository: MyRepository
    private lateinit var imageAdapter: JobStatusAdapter
    var loading: ProgressDialog? = null
    var jobModelList = ArrayList<JobModel>()
    var listToDelete = ArrayList<JobModel>()

    var collectionID = "63bbf9094f51c63e_1637212501863_259"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(false)

        return inflater.inflate(R.layout.fragment_job_status, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        backButton.setOnClickListener {
            requireActivity().supportFragmentManager?.popBackStack(AppConstant.JOB_PREIVEW_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
        clearBtn.setOnClickListener {
        var listToDelete = getListItem()
            if(listToDelete.isNullOrEmpty()) {
                clearBtn.isEnabled = false
                Toast.makeText(
                    context!!,
                    "Yey your task bucket have synced and cleared successfully",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            try{
                CoroutineScope(Dispatchers.IO).launch {
                    for(job in jobModelList){
                        if(job.jobStatus.equals(AppConstant.STATUS_SUCCESS, true)) {
                             viewModel.deleteImageInfoByName(job.savedImagePath)
                            listToDelete.remove(job)
                        }
                    }
                    updateList()
                }
                /*if(listToDelete.size==0){
                    if(jobModelList.size>0){
                        jobListView.recycledViewPool.clear()
                        imageAdapter.notifyDataSetChanged()
                    }else{
                        clearBtn.isEnabled = false
                    }
                } else if(listToDelete.equals(jobModelList)){
                    clearBtn.isEnabled = true
                    imageAdapter.notifyDataSetChanged()
                }else{
                    setRecyclerView(listToDelete)
                }*/
             }catch (e:Exception){
                e.printStackTrace()
            }

        }

        try{
            CoroutineScope(Dispatchers.IO).launch {
                val list = viewModel.getAllSavedImageInfo()
                if(list.size>0) {
                    setRecyclerView(getJobModelList(list))
                    if(loading!=null)
                        loading!!.dismiss()
                }else {
                    if (loading != null)
                        loading!!.dismiss()
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
        clearBtn.isEnabled = false
    }

    private fun updateList(){
        GlobalScope.launch {

            withContext(Dispatchers.Main) {
                try {
                    if(listToDelete.size==0){
                        if(jobModelList.size>0){
                            imageAdapter.clearAll()
                            jobListView.recycledViewPool.clear()
                            imageAdapter.notifyDataSetChanged()
                        }
                        clearBtn.isEnabled = false
                    } else if(listToDelete.equals(jobModelList)){
                        clearBtn.isEnabled = true
                        imageAdapter.notifyDataSetChanged()
                    }else{
                        setRecyclerView(listToDelete)
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }
                //imageAdapter = JobStatusAdapter(context!!, listToDelete)
                imageAdapter.notifyDataSetChanged()
            }
        }
    }
    fun getListItem(): ArrayList<JobModel> {
        var list = ArrayList<JobModel>()
        list = listToDelete
        return list
    }

    private fun setRecyclerView(jobList: ArrayList<JobModel>) {
        GlobalScope.launch {
            imageAdapter = JobStatusAdapter(context!!, jobList)
            withContext(Dispatchers.Main) {
                clearBtn.isEnabled = !jobList.isNullOrEmpty()
                val linearLayoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
                jobListView.apply {
                    layoutManager = linearLayoutManager
                    adapter = imageAdapter
                    loading?.dismiss()
                    scrollToPosition(jobList.size-1)
                }
            }
         }
    }

    private fun initViews() {
        myApplication = activity?.application as MyApplication
        myRepository = myApplication.myRepository
        viewModelFactory = ViewModelFactory(myRepository)
        viewModel = ViewModelProviders.of(activity!!, viewModelFactory)
            .get(MyViewModel::class.java)
        loading = ProgressDialog(context!!)
        loading!!.setCancelable(true);
        loading!!.setMessage(AppConstant.FETCH_STATUS);
        loading!!.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loading!!.show()

    }


    private fun getJobModelList(imageList: List<EntityClass>): ArrayList<JobModel>{
        for (image in imageList){
            var jobModel = JobModel()
            //collectionID = image.CollectionId
            var tag  = AppConstant().getLastStringAfter("%", image.path)
            jobModel.imagePath =image.path//""+ AppConstant.getPath(context!!, Uri.parse(image.path))
            jobModel.savedImagePath = image.path
            jobModel.uri = Uri.parse( image.path) //Uri.parse(""+ AppConstant.getPath(context!!, Uri.parse(image.path)))
            jobModel.imageId = AppConstant().getLastStringAfter("%", image.path)
            jobModel.jobStatus = getJobStatusByTag(tag, image.path)
            if(!image.time.isNullOrEmpty()){
                try {
                    jobModel.savedTimeStamp = image.time.substring(0,20)
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
            jobModelList.add(jobModel)
            listToDelete.add(jobModel)
        }
        return jobModelList
    }


    private fun getJobStatusByTag(tag: String, imagePath:String): String {
        val instance = WorkManager.getInstance(myApplication)
        val statuses: ListenableFuture<List<WorkInfo>> = instance.getWorkInfosByTag(tag)
        return try {
            var state = "Pending"

            val workInfoList: List<WorkInfo> = statuses.get()
            for (workInfo in workInfoList) {
                 state = "" + workInfo.state
            }
            if(workInfoList.size==0){
                if(statuses.isDone){
                    state = AppConstant.STATUS_SUCCESS
                }
            }
           state
        } catch (e: ExecutionException) {
            e.printStackTrace()
            ""
        } catch (e: InterruptedException) {
            e.printStackTrace()
            ""
        }catch (e:Exception){
            e.printStackTrace()
            ""
        }
    }

    private fun workStatus(tag: String){
        WorkManager.getInstance(myApplication).getWorkInfosByTagLiveData(tag)
            .observe(viewLifecycleOwner) {workInfo ->
                try {
                    if (workInfo != null) {
                        println("jobstatus work size"+workInfo.size)
                        if (workInfo.first().equals(WorkInfo.State.SUCCEEDED)) {

                        }
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
    }
/*
    fun isLastVisible(): Boolean {
        val layoutManager = jobListView.getLayoutManager() as LinearLayoutManager
        val pos = layoutManager.findLastCompletelyVisibleItemPosition()
        val numItems: Int = jobListView.getAdapter()?.getItemCount() ?: 0
        return pos >= numItems - 1
    }

    fun dismissDialog(){
        if(isLastVisible()){
            loading?.dismiss()
        }else
            dismissDialog()
    }*/

}