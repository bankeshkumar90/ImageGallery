package com.nowfloats.packrat.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.nowfloats.packrat.R
import com.nowfloats.packrat.adapter.ImageAdapter
import com.nowfloats.packrat.camera.CameraActivity
import com.nowfloats.packrat.clickInterface.ClickListener
import com.nowfloats.packrat.dialog.BottomViewDialog
import com.nowfloats.packrat.repository.MyRepository
import com.nowfloats.packrat.room.EntityClass
import com.nowfloats.packrat.viewModel.MyViewModel
import com.nowfloats.packrat.viewModel.ViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), ClickListener {

    private lateinit var myApplication: MyApplication
    private lateinit var myRepository: MyRepository
    private lateinit var viewModel: MyViewModel
    private lateinit var viewModelFactory: ViewModelFactory
    private var imageList = emptyList<EntityClass>()
    private lateinit var imageAdapter: ImageAdapter
    private val CAMERA_REQESUT_CODE = 1
    private lateinit var bottomViewDialog: BottomViewDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.elevation = 0F
        checkPermissions()

        initViewsAndListeners()

        setRecyclerView()

    }

    private fun setRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = imageAdapter
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.displayImage().observe(this, Observer {
            progressBar.visibility = View.GONE
            imageAdapter.updateList(it)
        })

    }

    /*
    below function initialize the variables
     */
    private fun initViewsAndListeners() {
        myApplication = application as MyApplication
        myRepository = myApplication.myRepository

        imageList = arrayListOf<EntityClass>()

        imageAdapter = ImageAdapter(imageList, this)

        viewModelFactory = ViewModelFactory(myRepository)

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(MyViewModel::class.java)

        ll_shelfView.setOnClickListener {
            dispatchTakePictureIntent()
        }
        ll_ProductView.setOnClickListener {
            dispatchTakePictureIntent()
        }

    }


    /*
         below function checks the required permissions and asks the same
         to user if not granted yet
   */
    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PERMISSION_GRANTED
        ) {

        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                CAMERA_REQESUT_CODE
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
                this,
                Manifest.permission.CAMERA
            ) == PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PERMISSION_GRANTED
        ) {

        } else {
            Toast.makeText(
                this,
                "Permissions are denied, please allow camera permission from settings",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    /*
       below function is called everytime any view is clicked in the recyclerview
   */
    override fun onClick(position: Int) {
        var uri = ""
        viewModel.displayImage().observe(this, Observer {
            uri = it[position].path
            val intent = Intent(this, ImageDetails::class.java)
            intent.putExtra("uri", uri)
            startActivity(intent)
        })
    }

    override fun onClickDelete(position: Int?) {
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.deleteImageById(position!!)
        }

    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.home_menu, menu)
        return true
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

    @SuppressLint("WrongConstant")
    private fun dispatchTakePictureIntent() {
        /*val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, CAMERA_REQESUT_CODE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }*/
        /*BottomViewDialog().apply {
            show(supportFragmentManager, BottomViewDialog.TAG)
        }*/
        bottomViewDialog = BottomViewDialog()
        bottomViewDialog.setStyle( 0, R.style.BottomSheetDialog)
        bottomViewDialog.show(supportFragmentManager, BottomViewDialog.TAG)

    }
}