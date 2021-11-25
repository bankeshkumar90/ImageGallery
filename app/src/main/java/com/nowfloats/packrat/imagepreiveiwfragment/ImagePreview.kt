package com.nowfloats.packrat.imagepreiveiwfragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageCapture
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class ImagePreview : Fragment(), ClickListener {

    lateinit var viewModel: MyViewModel
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var myApplication: MyApplication
    lateinit var myRepository: MyRepository
    private var uri: String? = ""
    private lateinit var imageAdapter: ImageAdapter
    //private var imageList = emptyList<EntityClass>()


    var imageName = ""
    var path = ""
    var image_uri : Uri? = null
    private lateinit var bottomViewDialog: BottomViewDialog
    private var shelf = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        shelf = arguments?.getBoolean(AppConstant.SHELF,false)!!

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(false)
        initViews()
        val extras = arguments

        if(extras?.getInt(AppConstant.REQUEST_TYPE,0)==AppConstant.REQ_GALLERY_CODE)
            openGallery()
        else{
            if(checkPermissions()){
                openCamera()
            }else{
                // request Permission in Fragmemt
                // request Permission in Fragmemt
                requestCameraPermission()
            }
        }
         return inflater.inflate(R.layout.fragment_image_preview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(false)
        btnSaveImage.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                 sendAddproductScreen()
            }
        }
        btn_open_camera.setOnClickListener {
            //Will save the imagelist in viewmodel and after uploading then push it to local database
            dispatchTakePictureIntent()
        }

     }

    private fun sendAddproductScreen() {
        val bundle = Bundle()
        bundle.putStringArrayList(AppConstant.IMAGE_LIST, viewModel.imageList )
        bundle.putBoolean(AppConstant.SHELF, shelf)

        val addProduct = AddProduct()
        addProduct.arguments = bundle

        val ft: FragmentTransaction = fragmentManager!!.beginTransaction()
        ft.replace(R.id.fram_dashboard, addProduct)
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        ft.addToBackStack(AppConstant.ADD_PRODUCT_TAG)
        ft.commit()

    }

    private fun setRecyclerView() {
        imageAdapter = ImageAdapter( this, viewModel.imageList)
        val linearLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        camera_list.apply {
            layoutManager = linearLayoutManager
            adapter = imageAdapter
        }
    }

    private fun initViews() {
        myApplication = activity?.application as MyApplication
        myRepository = myApplication.myRepository
        //imageList = arrayListOf<EntityClass>()
        viewModelFactory = ViewModelFactory(myRepository)
        viewModel = ViewModelProviders.of(activity!!, viewModelFactory)
            .get(MyViewModel::class.java)
    }



    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        /*viewModel.displayImage().observe(this, {
            imageAdapter.updateList(it)
        })*/
        viewModel.imageArrayList.observe(this,{
            imageAdapter.updateImageList(it)
        })

    }

    override fun onClick(position: Int) {
        imgPreview.setImageURI(Uri.parse(viewModel.imageList[position]))
    }

    override fun onClickDelete(position: Int?) {
        position?.let {
            imageAdapter.deleteImageFromPreview(it)
            if(imageAdapter.itemCount>0) {
                imgPreview.setImageURI(Uri.parse(imageAdapter.getFirstImageURl()))
            }
            else{
                requireActivity().supportFragmentManager?.popBackStack(AppConstant.IMAGE_PREIVEW_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                return
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.deleteImageById(position!!)
        }
    }
    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.clear()
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
        startActivityForResult(cameraIntent, AppConstant.REQ_CAMERA_CODE)
    }

    private fun openGallery(){
        val i = Intent()
        i.type = "image/*"
        i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        i.action = Intent.ACTION_GET_CONTENT
        //i.addCategory(Intent.CATEGORY_OPENABLE)
        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Pictures"), AppConstant.REQ_GALLERY_CODE)
    }
    private fun showPreview(imagePath:String){
        uri = imagePath
        imageName = imagePath
        setRecyclerView()
                try {
                    Glide.with(context!!)
                        .load(Uri.parse(imagePath))
                        .override(480, 960)
                        .centerCrop() // scale to fill the ImageView and crop any extra
                        .into( imgPreview)
                    //holder.imageView.setImageURI(jobItem.uri)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
     }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppConstant.REQ_CAMERA_CODE) {
            var validFile = AppConstant().ifFileExists(context!!, ""+image_uri)
            image_uri?.let {
                if(validFile) {
                    viewModel.addImageToList("" + image_uri)
                }
                if(viewModel.imageList.size>0){
                    if(validFile)
                    showPreview("" + it)
                }
                else{
                    requireActivity().supportFragmentManager?.popBackStack(AppConstant.IMAGE_PREIVEW_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    return
                }
            }
        }else if( requestCode == AppConstant.REQ_GALLERY_CODE){
            //update imageList
            if(data==null){
                if(viewModel.imageList.size<1){
                    requireActivity().supportFragmentManager?.popBackStack(AppConstant.IMAGE_PREIVEW_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    return
                }
                else if(viewModel.imageList.size>0) {
                    showPreview("" + viewModel.imageList[0])
                    return
                }
            }
            if (data!!.clipData != null){
                //picked multiple images
                //get number of picked images
                val count = data.clipData!!.itemCount
                for (i in 0 until count){
                    val imageUri = data.clipData!!.getItemAt(i).uri
                    //add image to list
                    viewModel.addImageToList(""+imageUri)
                }
                //set first image from list to image switcher
                showPreview(""+data.clipData!!.getItemAt(0).uri)
            }
            else{
                //picked single image
                val selectedImageUri: Uri = data?.data as Uri
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    selectedImageUri?.let {
                        viewModel.addImageToList(""+it)
                        showPreview(""+it)
                    }
                }
            }
        }
    }



    @SuppressLint("WrongConstant")
    private fun dispatchTakePictureIntent() {
        var objClick = object : OnImageDialogSelector {
            override fun onDialogTypeSelected(requestCode: Int) {
                if(requestCode==AppConstant.REQ_GALLERY_CODE)
                    openGallery()
                else{
                    if(checkPermissions()){
                        openCamera()
                    }else{
                        // request Permission in Fragmemt
                        // request Permission in Fragmemt
                        //CameraPermissionRequest()
                        requestCameraPermission()
                    }}
            }
        }
        bottomViewDialog = BottomViewDialog(objClick)
        bottomViewDialog.setStyle( 0, R.style.BottomSheetDialog)
        bottomViewDialog.show(fragmentManager!!, BottomViewDialog.TAG)
    }


    private fun checkPermissions() : Boolean{
        var permissionAllowed = false
        if (ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            permissionAllowed = true
            return permissionAllowed
        }
        return permissionAllowed
    }

    private fun requestCameraPermission(){
        requestPermissions(
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            AppConstant.CAMERA_PERMISSION_REQUIRED
        )
        //requestPermissions(  arrayOf(Manifest.permission.CAMERA) ,  AppConstant.CAMERA_PERMISSION_REQUIRED)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            if(requestCode==AppConstant.CAMERA_PERMISSION_REQUIRED){
                openCamera()
            }
        } else {
            Toast.makeText(
                context!!,
                AppConstant.PERMISSION_DENIED_MESSAGE,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}