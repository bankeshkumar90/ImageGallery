package com.nowfloats.packrat.homescreen

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageCapture
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.nowfloats.packrat.R
import com.nowfloats.packrat.bottomsheetdialog.BottomViewDialog
import com.nowfloats.packrat.clickInterface.OnImageDialogSelector
import com.nowfloats.packrat.databaserepository.MyRepository
import com.nowfloats.packrat.imageViewModel.MyViewModel
import com.nowfloats.packrat.imageViewModel.ViewModelFactory
import com.nowfloats.packrat.imagepreiveiwfragment.ImagePreview
import com.nowfloats.packrat.jobstatus.JobStatus
import com.nowfloats.packrat.utils.AppConstant
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.ll_ProductView
import kotlinx.android.synthetic.main.activity_main.ll_shelfView
import kotlinx.android.synthetic.main.dashboard_fragment.*
import java.io.File
import com.nowfloats.packrat.clickInterface.ImageCaptureListner as ImageCaptureListner
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.nowfloats.packrat.settings.AppSettings
import kotlinx.android.synthetic.main.fragment_add_product.view.*
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DashBoardFragment:Fragment() {
    private lateinit var bottomViewDialog: BottomViewDialog
    private val TAKE_PHOTO_CODE = 101
    lateinit var viewModel: MyViewModel
    lateinit var viewModelFactory: ViewModelFactory
    var imageName = ""
    var path = ""
    lateinit var myApplication: MyApplication
    lateinit var myRepository: MyRepository
    private var imageCapture: ImageCapture? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressed()

        myApplication = activity?.application as MyApplication
        myRepository = myApplication.myRepository
        viewModelFactory = ViewModelFactory(myRepository)
        viewModel = ViewModelProviders.of(activity!!, viewModelFactory)
            .get(MyViewModel::class.java)
        try{
            viewModel.fetchFromAPI()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return  inflater.inflate(R.layout.dashboard_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewsAndListeners()
        checkPermissions()

    }
    private fun initViewsAndListeners() {
        ll_shelfView.setOnClickListener {
            dispatchTakePictureIntent(true)
        }
        ll_ProductView.setOnClickListener {
            dispatchTakePictureIntent(false)
        }
        btnJobStatus.setOnClickListener(){
            showJobStatus()
        }
        btnSettings.setOnClickListener(){
            showSettings()
        }
        try{
            CoroutineScope(Dispatchers.IO).launch {
                updateBtnICON(viewModel.haveEmptyJobQueued())
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
    @SuppressLint("WrongConstant")
    private fun dispatchTakePictureIntent(shelf: Boolean) {

        /*val imageCaptureListner = object : ImageCaptureListner {
            override fun onCameraClick() {
                startCamera()
            }

            override fun onGallery() {
                startCamera()
            }

        }*/
        var objClick = object : OnImageDialogSelector {
            override fun onDialogTypeSelected(requestCode: Int) {
                val bundle = Bundle()
                bundle.putInt(AppConstant.REQUEST_TYPE, requestCode)
                bundle.putBoolean(AppConstant.SHELF, shelf)

                var imagePreview = ImagePreview()
                imagePreview.arguments = bundle
                val ft: FragmentTransaction = fragmentManager!!.beginTransaction()
                ft.replace(R.id.fram_dashboard, imagePreview)
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                ft.addToBackStack(AppConstant.IMAGE_PREIVEW_TAG)
                ft.commit()
            }
        } 
        bottomViewDialog = BottomViewDialog(objClick)
        bottomViewDialog.setStyle( 0, R.style.BottomSheetDialog)
        bottomViewDialog.show(fragmentManager!!, BottomViewDialog.TAG)

    }

    private fun showJobStatus(){
        var jobPreiview = JobStatus()
        val ft: FragmentTransaction = fragmentManager!!.beginTransaction()
        ft.replace(R.id.fram_dashboard, jobPreiview)
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        ft.addToBackStack(AppConstant.JOB_PREIVEW_TAG)
        ft.commit()
    }

    private fun showSettings(){
        var settings = AppSettings()
        val ft: FragmentTransaction = fragmentManager!!.beginTransaction()
        ft.replace(R.id.fram_dashboard, settings)
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        ft.addToBackStack(AppConstant.SETTINGS_TAG)
        ft.commit()
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.status -> {
                    true
                }
                R.id.setting -> {
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        }

    private fun startCamera() {
        /*
        initiates the cameraX into the UI
         */
        /*val cameraProviderFuture = ProcessCameraProvider.getInstance(context!!)
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()
            preview = Preview.Builder().build()
            preview?.setSurfaceProvider(cameraView.createSurfaceProvider(camera?.cameraInfo))
            imageCapture = ImageCapture.Builder().build()
            val cameraSelector =
                CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
            cameraProvider.unbindAll()
            camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
        }, ContextCompat.getMainExecutor(context))*/

        imageName = "image${System.currentTimeMillis()}.jpg"
        /*
        creates a new folder if not already present and add the image into the folder
         */
        var file = File("${activity?.getExternalFilesDir(null)}${File.separator}${AppConstant.APP_FOLDER_NAME}")
        if (!file.exists()) {
            file.mkdir()
        }
        var fileName = File(file, imageName)
        path = fileName.absolutePath
        val outputFileUri: Uri = Uri.fromFile(file)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri)


        val cameraObjIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri)
        try {
            startActivityForResult(cameraObjIntent, TAKE_PHOTO_CODE)
            //startActivityForResult(cameraIntent, TAKE_PHOTO_CODE)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TAKE_PHOTO_CODE) {
            Log.d("Demo Pic", "Picture is saved")
            showPreview()
        }
    }


    private fun showPreview() {
        val bundle = Bundle()
        bundle.putString("uri", path)
        bundle.putString("image", imageName)
        val imagePreview = ImagePreview()
        imagePreview.arguments = bundle
        val fragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.add(R.id.fram_dashboard, imagePreview, "ImagePreview").commit()
    }

    fun onBackPressed(){
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // Do custom work here
                    requireActivity().finish()
                }
            }
        )
    }

    override fun onDestroyView() {
        val mContainer = activity!!.findViewById<View>(R.id.fram_dashboard) as ViewGroup
        mContainer.removeAllViews()
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()

        var emailExists = AppConstant().getValuesByTagFromLocalPrefs(context!!, AppConstant.CREATED_BY, AppConstant.CREATED_BY)
        if(emailExists.isNullOrEmpty()){
            showSettings()
        }

    }

    private fun updateBtnICON(emptyJobBucket: Boolean){
        try {
            CoroutineScope(Dispatchers.Main).launch {
                if (emptyJobBucket) {
                    if(btnJobStatus!=null)
                    btnJobStatus.setBackgroundResource(R.drawable.ic_uplaod)
                } else {
                    if(btnJobStatus!=null)
                    btnJobStatus.setBackgroundResource(R.drawable.ic_uplaod_dot)
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    /*
         below function checks the required permissions and asks the same
         to user if not granted yet
   */
    private fun checkPermissions() {
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

        } else {
            requestPermissions(
                 arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                AppConstant.CAMERA_PERMISSION_REQUIRED
            )

        }
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

        } else {
            Toast.makeText(
                context!!,
                AppConstant.PERMISSION_DENIED_MESSAGE,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}