package com.nowfloats.packrat.camera

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import com.nowfloats.packrat.fragment.ImagePreview
import com.nowfloats.packrat.home.MyApplication
import com.nowfloats.packrat.repository.MyRepository
import com.nowfloats.packrat.viewModel.MyViewModel
import com.nowfloats.packrat.viewModel.ViewModelFactory
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.File
import com.nowfloats.packrat.R
import com.nowfloats.packrat.fragment.AddProduct

class CameraFragment : Fragment() {
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
        startCamera()
    }

    private fun initViewsAndListeners(view: View) {
        myApplication = activity?.application as MyApplication
        myRepository = myApplication.myRepository
        viewModelFactory = ViewModelFactory(myRepository)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MyViewModel::class.java)
        var btnClick = view.findViewById(R.id.btnClick) as Button
        btnClick.setOnClickListener {
            takePhotos()
        }
    }

    private fun takePhotos() {
        imageName = "image${System.currentTimeMillis()}.jpg"

        /*
        creates a new folder if not already present and add the image into the folder
         */
        var file = File("${activity?.getExternalFilesDir(null)}${File.separator}${folderName}")
        if (!file.exists()) {
            file.mkdir()
        }

        var fileName = File(file, imageName)
        path = fileName.absolutePath

        val output = ImageCapture.OutputFileOptions.Builder(fileName).build()


        imageCapture?.takePicture(
            output,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                /*
                below callback is evokes if the image is saved successfully in the storage
                 */
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    /*btnClick.visibility = View.GONE
                    btnShowPreview.visibility = View.VISIBLE*/
                    showPreview()
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(
                        context,
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
            /*val fragmentTransaction = fragmentManager!!.beginTransaction()
            fragmentTransaction.add(R.id.Frame, imagePreview, "ImagePreview").commit()*/

            val ft: FragmentTransaction = fragmentManager!!.beginTransaction()
            ft.replace(R.id.fram_dashboard, imagePreview)
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            ft.addToBackStack(null)
            ft.commit()
        }

    }

    private fun showPreview() {
        val bundle = Bundle()
        bundle.putString("uri", path)
        bundle.putString("image", imageName)
        val imagePreview = ImagePreview()
        imagePreview.arguments = bundle
//        btnShowPreview.visibility = View.GONE
        val fragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.add(R.id.fram_dashboard, imagePreview, "ImagePreview").commit()
    }

    private fun startCamera() {
        /*
        initiates the cameraX into the UI
         */
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context!!)
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()
            preview = Preview.Builder().build()
            preview?.setSurfaceProvider(cameraView.createSurfaceProvider(camera?.cameraInfo))
            imageCapture = ImageCapture.Builder().build()
            val cameraSelector =
                CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
            cameraProvider.unbindAll()
            camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
        }, ContextCompat.getMainExecutor(context))
    }
}