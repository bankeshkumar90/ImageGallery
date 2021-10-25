package com.nowfloats.packrat.camera

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.nowfloats.packrat.fragment.ImagePreview
import com.nowfloats.packrat.home.MyApplication
import com.nowfloats.packrat.repository.MyRepository
import com.nowfloats.packrat.viewModel.MyViewModel
import com.nowfloats.packrat.viewModel.ViewModelFactory
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.File
import com.nowfloats.packrat.R

class CameraActivity : AppCompatActivity() {
    private var camera: Camera? = null
    private var preview: Preview? = null
    lateinit var viewModel: MyViewModel
    lateinit var viewModelFactory: ViewModelFactory
    var imageName = ""
    var path = ""
    var folderName = "Camera Snapshots"
    lateinit var myApplication: MyApplication
    lateinit var myRepository: MyRepository

    private var imageCapture: ImageCapture? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        initViewsAndListeners()
        startCamera()
    }

    private fun initViewsAndListeners() {
        myApplication = application as MyApplication

        myRepository = myApplication.myRepository

        viewModelFactory = ViewModelFactory(myRepository)

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(MyViewModel::class.java)

        btnClick.setOnClickListener {
            takePhotos()
        }
    }

    private fun takePhotos() {
        imageName = "image${System.currentTimeMillis()}.jpg"

        /*
        creates a new folder if not already present and add the image into the folder
         */
        var file = File("${getExternalFilesDir(null)}${File.separator}${folderName}")
        if (!file.exists()) {
            file.mkdir()
        }

        var fileName = File(file, imageName)
        path = fileName.absolutePath

        val output = ImageCapture.OutputFileOptions.Builder(fileName).build()


        imageCapture?.takePicture(
            output,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                /*
                below callback is evokes if the image is saved successfully in the storage
                 */
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    btnClick.visibility = View.GONE
                    btnShowPreview.visibility = View.VISIBLE
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(
                        applicationContext,
                        "An error occurred while saving",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

        btnShowPreview.setOnClickListener {
            /*
            starts the fragment and send the uri of captured image to the fragment
             */

            val bundle = Bundle()
            bundle.putString("uri", path)
            bundle.putString("image", imageName)
            val imagePreview = ImagePreview()
            imagePreview.arguments = bundle

            btnShowPreview.visibility = View.GONE

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.add(R.id.Frame, imagePreview, "ImagePreview").commit()
        }

    }

    private fun startCamera() {

        /*
        initiates the cameraX into the UI
         */

        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()
            preview = Preview.Builder().build()
            preview?.setSurfaceProvider(cameraView.createSurfaceProvider(camera?.cameraInfo))
            imageCapture = ImageCapture.Builder().build()
            val cameraSelector =
                CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
            cameraProvider.unbindAll()
            camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
        }, ContextCompat.getMainExecutor(this))
    }
}