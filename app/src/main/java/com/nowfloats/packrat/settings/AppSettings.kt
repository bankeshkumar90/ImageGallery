package com.nowfloats.packrat.settings

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.camera.core.ImageCapture
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.nowfloats.packrat.R
import com.nowfloats.packrat.addjobs.AddProduct
import com.nowfloats.packrat.bottomsheetdialog.BottomViewDialog
import com.nowfloats.packrat.camera.CameraFragment
import com.nowfloats.packrat.clickInterface.ClickListener
import com.nowfloats.packrat.clickInterface.OnImageDialogSelector
import com.nowfloats.packrat.databaserepository.MyRepository
import com.nowfloats.packrat.homescreen.MyApplication
import com.nowfloats.packrat.imageViewModel.MyViewModel
import com.nowfloats.packrat.imageViewModel.ViewModelFactory
import com.nowfloats.packrat.imagelistadapter.ImageAdapter
import com.nowfloats.packrat.roomdatabase.EntityClass
import com.nowfloats.packrat.utils.AppConstant
import kotlinx.android.synthetic.main.fragment_image_preview.*
import kotlinx.android.synthetic.main.fragment_image_preview.btnSaveImage
import kotlinx.android.synthetic.main.fragment_job_status.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList
import androidx.work.WorkInfo

import androidx.work.WorkManager
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.android.synthetic.main.fragment_job_status.backButton
import kotlinx.android.synthetic.main.fragment_job_status.clearBtn
import kotlinx.android.synthetic.main.fragment_settings.*
import java.util.concurrent.ExecutionException


class AppSettings : Fragment()  {
    private var previousEmail = ""
    private var previousCompression = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(false)
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        onBackPressed()
    }

    private fun initViews(){
        btnSaveSettings.setOnClickListener(){
            onBackPress()
            /*if(AppConstant().isValidEmail(etEmail.text.toString())){
                //save email
                    AppConstant().saveKeysByTagINLocalPrefs(context!!, AppConstant.CREATED_BY, etEmail.text.toString())
                    var compressionType = AppConstant.NONE
                    if(noneRadioBtn.isChecked != true){
                         compressionType = AppConstant.MEDIUM
                     }
                    AppConstant().saveKeysByTagINLocalPrefs(context!!, AppConstant.COMPRESSION_TYPE, compressionType)

                Toast.makeText(context!!, context!!.resources.getString(R.string.prefs_saved), Toast.LENGTH_SHORT).show()
            }else{
                etEmail.setError(resources.getString(R.string.enter_email))
            }*/
        }
        backButtonSettings.setOnClickListener {
            onBackPress()
        }
        var emailExists = AppConstant().getValuesByTagFromLocalPrefs(context!!, AppConstant.CREATED_BY, AppConstant.CREATED_BY)
        if(!emailExists.isNullOrEmpty()){
            btnSaveSettings.visibility = View.GONE
            etEmail.setText(emailExists)
            previousEmail = emailExists
        }else{
            btnSaveSettings.visibility = View.VISIBLE
        }
        var compressionType = AppConstant().getValuesByTagFromLocalPrefs(context!!, AppConstant.COMPRESSION_TYPE, AppConstant.MEDIUM)
        previousCompression = ""+compressionType
        if(!compressionType.isNullOrEmpty()){
           if(compressionType.equals(AppConstant.NONE)){
               noneRadioBtn.isChecked = true
           }else
               mediumRadioBtn.isChecked = true
        }
    }

    fun onBackPressed(){
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // Do custom work here
                    onBackPress()
                 }
            }
            )
    }

    fun onBackPress(){
        if(AppConstant().isValidEmail(etEmail.text.toString())){
            //save email
            AppConstant().saveKeysByTagINLocalPrefs(context!!, AppConstant.CREATED_BY, etEmail.text.toString())
            var compressionType = AppConstant.NONE
            if(!noneRadioBtn.isChecked){
                compressionType = AppConstant.MEDIUM
            }
            AppConstant().saveKeysByTagINLocalPrefs(context!!, AppConstant.COMPRESSION_TYPE, compressionType)
            if(!previousEmail.equals(etEmail.text.toString(), true) || !previousCompression.equals(compressionType, true))
                Toast.makeText(context!!, context!!.resources.getString(R.string.prefs_saved), Toast.LENGTH_SHORT).show()

            requireActivity().supportFragmentManager?.popBackStack(AppConstant.SETTINGS_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }else{
            etEmail.setError(resources.getString(R.string.enter_email))
        }
    }

}