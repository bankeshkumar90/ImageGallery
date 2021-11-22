package com.nowfloats.packrat.homescreen

import android.Manifest
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.nowfloats.packrat.R
import com.nowfloats.packrat.databaserepository.MyRepository
import com.nowfloats.packrat.imageViewModel.MyViewModel
import com.nowfloats.packrat.imageViewModel.ViewModelFactory
import androidx.lifecycle.ViewModelProvider
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.nowfloats.packrat.addjobs.AddProductViewModel
import com.nowfloats.packrat.roomdatabase.EntityClass
import com.nowfloats.packrat.utils.AppConstant
import java.util.*
import android.content.Intent
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION


class MainActivity : AppCompatActivity() {

    private lateinit var myApplication: MyApplication
    private lateinit var myRepository: MyRepository
    private lateinit var viewModel: MyViewModel
    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var mainViewModelFactory: MainViewModelFactory

    //    private var imageList = emptyList<EntityClass>()
//    private lateinit var imageAdapter: ImageAdapter
    private val CAMERA_REQESUT_CODE = 1
//    private lateinit var bottomViewDialog: BottomViewDialog
    private lateinit var mainViewModel:MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard_activity)
        supportActionBar?.elevation = 0F
        supportActionBar?.hide()
        initViewsAndListeners()
        setDashBoard()

//        setRecyclerView()

    }

   /* private fun setRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = imageAdapter
        }
    }*/

    override fun onResume() {
        super.onResume()
       mainViewModel.getUnSyncedSavedImageProperty().observe(this,{
           if(it.size<1)
               return@observe
           //mainViewModel.initiateBackGroundProcess(it)
           //observeWorkStatus(it)
       })


    }

    private fun observeWorkStatus(imageList: List<EntityClass>){
        if(imageList.size<1)
            return
        var TAG = AppConstant().getLastStringAfter("%", imageList[0].path)
        //var uuid = UUID.fromString(uuidString.substring(uuidString.lastIndexOf("%") + 1))
        WorkManager.getInstance(getApplication()).getWorkInfosByTagLiveData(TAG)
            .observe(this, Observer { workInfo ->
                if (workInfo != null) {
                        println("status->"+workInfo.size)
                }
            }
            )
    }
    /*
    below function initialize the variables
     */
    private fun initViewsAndListeners() {
        myApplication = application as MyApplication
        myRepository = myApplication.myRepository

//        imageList = arrayListOf<EntityClass>()

//        imageAdapter = ImageAdapter(imageList, this)

        viewModelFactory = ViewModelFactory(myRepository)
        mainViewModelFactory = MainViewModelFactory(myApplication, myRepository)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MyViewModel::class.java)
        mainViewModel = ViewModelProviders.of(this,mainViewModelFactory ).get(MainViewModel::class.java)
         /* ll_shelfView.setOnClickListener {
            dispatchTakePictureIntent()
        }
        ll_ProductView.setOnClickListener {
            dispatchTakePictureIntent()
        }*/
        /*for (workInfo in workInfoList) {
            val state = workInfo.state
            running = state == WorkInfo.State.RUNNING ||
                    state == WorkInfo.State.ENQUEUED
        }*/
    }

    fun setDashBoard(){
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fram_dashboard, DashBoardFragment())
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        ft.addToBackStack(null)
        ft.commit()
    }




    /*
       below function is called everytime any view is clicked in the recyclerview
   */
    /*override fun onClick(position: Int) {
        var uri = ""
        viewModel.displayImage().observe(this, Observer {
            uri = it[position].path
            val intent = Intent(this, ImageDetails::class.java)
            intent.putExtra("uri", uri)
            startActivity(intent)
        })
    }*/

  /*  override fun onClickDelete(position: Int?) {
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.deleteImageById(position!!)
        }

    }*/




    /*@SuppressLint("WrongConstant")
    private fun dispatchTakePictureIntent() {
        *//*val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, CAMERA_REQESUT_CODE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }*//*
        *//*BottomViewDialog().apply {
            show(supportFragmentManager, BottomViewDialog.TAG)
        }*//*
        bottomViewDialog = BottomViewDialog()
        bottomViewDialog.setStyle( 0, R.style.BottomSheetDialog)
        bottomViewDialog.show(supportFragmentManager, BottomViewDialog.TAG)

    }*/


}