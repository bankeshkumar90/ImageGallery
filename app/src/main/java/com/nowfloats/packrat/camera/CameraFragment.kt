package com.nowfloats.packrat.camera

import android.Manifest.permission.CAMERA
import android.R.attr
import android.app.Activity.RESULT_OK
import android.content.ContentValues
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import com.nowfloats.packrat.R
import com.nowfloats.packrat.databaserepository.MyRepository
import com.nowfloats.packrat.homescreen.MyApplication
import com.nowfloats.packrat.imageViewModel.MyViewModel
import com.nowfloats.packrat.imageViewModel.ViewModelFactory
import com.nowfloats.packrat.imagepreiveiwfragment.ImagePreview
import com.nowfloats.packrat.utils.AppConstant
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.File
import androidx.core.app.ActivityCompat.startActivityForResult

import android.provider.MediaStore

import android.content.Intent
import android.net.Uri
import android.util.Log
import java.util.ArrayList
import androidx.core.app.ActivityCompat.startActivityForResult
import android.R.attr.data
import android.os.Build


class CameraFragment : Fragment() {
    private var camera: Camera? = null
    private var preview: Preview? = null
    private val TAKE_PHOTO_CODE = 101
    private val SELECT_PICTURE_CODE = 102
    lateinit var viewModel: MyViewModel
    lateinit var viewModelFactory: ViewModelFactory
    var imageName = ""
    var path = ""
    lateinit var myApplication: MyApplication
    lateinit var myRepository: MyRepository
    private var imageCapture: ImageCapture? = null
    lateinit var outputFileUri: Uri
    var image_uri : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_camera, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViewsAndListeners(view)
        val extras = arguments
        if(extras?.getInt(AppConstant.REQUEST_TYPE,0)==AppConstant.REQ_GALLERY_CODE)
            openGallery()
        else
            openCamera()
        setHasOptionsMenu(false)
    }

    private fun initViewsAndListeners(view: View) {
        myApplication = activity?.application as MyApplication
        myRepository = myApplication.myRepository
        viewModelFactory = ViewModelFactory(myRepository)
        viewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(MyViewModel::class.java)

    }

    private  fun showPreview(image_uri: Uri) {
        viewModel.addImageToList(""+image_uri)
        val bundle = Bundle()
        bundle.putString("uri", ""+image_uri)
        bundle.putString(AppConstant.IMAGE_URI, ""+image_uri)
        bundle.putStringArrayList(AppConstant.IMAGE_LIST, viewModel.imageList )
        val imagePreview = ImagePreview()
        imagePreview.arguments = bundle
        val fragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.add(R.id.fram_dashboard, imagePreview, "ImagePreview").commit()
    }


    fun openCamera()
    {
        imageName = "image${System.currentTimeMillis()}.jpg"
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, imageName)
        image_uri = context?.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            cameraIntent.putExtra("android.intent.extras.LENS_FACING_FRONT", 0);
        } else {
            cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 0);
        }*/
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, TAKE_PHOTO_CODE)
    }

    private fun openGallery(){
        val i = Intent()
        i.type = "image/*"
        //i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        i.action = Intent.ACTION_GET_CONTENT
        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE_CODE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TAKE_PHOTO_CODE) {
            image_uri?.let { showPreview(it) }
        }else if( requestCode == SELECT_PICTURE_CODE){
            //update imageList
            val selectedImageUri: Uri = data?.data as Uri
            if (null != selectedImageUri) {
                // update the preview image in the layout
                selectedImageUri?.let { showPreview(it) }
            }
        }
    }
}