package com.nowfloats.packrat.homescreen

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageCapture
import androidx.fragment.app.Fragment
import com.nowfloats.packrat.R
import com.nowfloats.packrat.bottomsheetdialog.BottomViewDialog
import com.nowfloats.packrat.databaserepository.MyRepository
import com.nowfloats.packrat.imageViewModel.MyViewModel
import com.nowfloats.packrat.imageViewModel.ViewModelFactory
import com.nowfloats.packrat.imagepreiveiwfragment.ImagePreview
import com.nowfloats.packrat.utils.AppConstant
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import com.nowfloats.packrat.clickInterface.ImageCaptureListner as ImageCaptureListner

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
    }
    private fun initViewsAndListeners() {
        ll_shelfView.setOnClickListener {
            dispatchTakePictureIntent()
        }
        ll_ProductView.setOnClickListener {
            dispatchTakePictureIntent()
        }

    }
    @SuppressLint("WrongConstant")
    private fun dispatchTakePictureIntent() {

        /*val imageCaptureListner = object : ImageCaptureListner {
            override fun onCameraClick() {
                startCamera()
            }

            override fun onGallery() {
                startCamera()
            }

        }*/
        bottomViewDialog = BottomViewDialog()
        bottomViewDialog.setStyle( 0, R.style.BottomSheetDialog)
        bottomViewDialog.show(fragmentManager!!, BottomViewDialog.TAG)

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
}