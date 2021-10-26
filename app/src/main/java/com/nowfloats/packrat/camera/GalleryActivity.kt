package com.nowfloats.packrat.camera

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nowfloats.packrat.R
import kotlinx.android.synthetic.main.activity_gallery.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class GalleryActivity : AppCompatActivity() {

//    val REQUEST_IMAGE_CAPTURE = 1
    val PICK_IMAGES = 2
    val STORAGE_PERMISSION = 100

    var imageList: ArrayList<ImageModel>? = null
    var selectedImageList: ArrayList<String>? = null
    var imageRecyclerView: RecyclerView? = null
    var resImg = intArrayOf(R.drawable.photo_camera_black_24dp)
    var title = arrayOf("Folder")
    var mCurrentPhotoPath: String? = null
    var imageAdapter: GalleryAdapter? = null
    var projection = arrayOf(MediaStore.MediaColumns.DATA)
    var image: File? = null
    var done: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
            init()
            getAllImages()
            setImageList()
    }

    fun init() {
        imageRecyclerView = findViewById(R.id.recycler_view)
        done = findViewById(R.id.done)
        selectedImageList = ArrayList()
        imageList = ArrayList<ImageModel>()
        done!!.setOnClickListener(View.OnClickListener {
            for (i in selectedImageList!!.indices) {
                Toast.makeText(applicationContext, "" + selectedImageList!!.size, Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    fun setImageList() {
        imageRecyclerView!!.setLayoutManager(GridLayoutManager(applicationContext, 4))
        imageAdapter = GalleryAdapter()
        imageAdapter!!.setdatainList(applicationContext, imageList)
        imageRecyclerView!!.setAdapter(imageAdapter)
        imageAdapter!!.setOnItemClickListener(object : GalleryAdapter.OnItemClickListener {
           override fun onItemClick(position: Int, v: View?) {
                /*if (position == 0) {
                    takePicture()
                } else */if (position == 0) {
                    getPickImageIntent()
                } else {
                    try {
                        if (!imageList!![position].isSelected) {
                            selectImage(position)
                        } else {
                            unSelectImage(position)
                        }
                    } catch (ed: ArrayIndexOutOfBoundsException) {
                        ed.printStackTrace()
                    }
                }
            }
        })
        setImagePickerList()
    }

    // Add Camera and Folder in ArrayList
    fun setImagePickerList() {
        for (i in resImg.indices) {
            val imageModel = ImageModel(title = title[i],resImg =resImg[i])
//            imageModel.resImg=resImg[i]
//            imageModel.setTitle(title[i])
            imageList!!.add(i, imageModel)
        }
        imageAdapter!!.notifyDataSetChanged()
    }

    // get all images from external storage
    fun getAllImages() {
        imageList!!.clear()
        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )
        while (cursor!!.moveToNext()) {
            val absolutePathOfImage =
                cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA))
            val ImageModel = ImageModel(image = absolutePathOfImage)
//            ImageModel.image=absolutePathOfImage
            imageList!!.add(ImageModel)
        }
        cursor.close()
    }

    // start the image capture Intent
    /*fun takePicture() {
        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Continue only if the File was successfully created;
        val photoFile = createImageFile()
        if (photoFile != null) {
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile))
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
        }
    }*/

    fun getPickImageIntent() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(intent, PICK_IMAGES)
    }

    // Add image in selectedImageList
    fun selectImage(position: Int) {
        // Check before add new item in ArrayList;
        if (!selectedImageList!!.contains(imageList!![position].image)) {
            imageList!![position].isSelected=true
            selectedImageList!!.add(0, imageList!![position].image)
//            selectedImageAdapter.notifyDataSetChanged()
            imageAdapter!!.notifyDataSetChanged()
        }
    }

    // Remove image from selectedImageList
    fun unSelectImage(position: Int) {
        for (i in selectedImageList!!.indices) {
            if (imageList!![position].image != null) {
                if (selectedImageList!![i] == imageList!![position].image) {
                    imageList!![position].isSelected =false
                    selectedImageList!!.removeAt(i)
//                    selectedImageAdapter.notifyDataSetChanged()
                    imageAdapter!!.notifyDataSetChanged()
                }
            }
        }
    }

    fun createImageFile(): File? {
        // Create an image file name
        val dateTime = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "IMG_" + dateTime + "_"
        val storageDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        try {
            image = File.createTempFile(imageFileName, ".jpg", storageDir)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image!!.absolutePath
        return image
    }

    // Get image file path
    fun getImageFilePath(uri: Uri?) {
        val cursor = contentResolver.query(uri!!, projection, null, null, null)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val absolutePathOfImage =
                    cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA))
                if (absolutePathOfImage != null) {
                    checkImage(absolutePathOfImage)
                } else {
                    checkImage(uri.toString())
                }
            }
        }
    }

    fun checkImage(filePath: String) {
        // Check before adding a new image to ArrayList to avoid duplicate images
        if (!selectedImageList!!.contains(filePath)) {
            for (pos in imageList!!.indices) {
                if (imageList!![pos].image != null) {
                    if (imageList!![pos].image.equals(filePath)) {
                        imageList!!.removeAt(pos)
                    }
                }
            }
            addImage(filePath)
        }
    }

    // add image in selectedImageList and imageList
    fun addImage(filePath: String) {
        val imageModel = ImageModel(filePath)
//        imageModel.setImage(filePath)
        imageModel.isSelected = true
        imageList!!.add(2, imageModel)
        selectedImageList!!.add(0, filePath)
//        selectedImageAdapter.notifyDataSetChanged()
        imageAdapter!!.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            /*if (requestCode == REQUEST_IMAGE_CAPTURE) {
                if (mCurrentPhotoPath != null) {
                    addImage(mCurrentPhotoPath!!)
                }
            } else*/ if (requestCode == PICK_IMAGES) {
                if (data!!.clipData != null) {
                    val mClipData = data.clipData
                    for (i in 0 until mClipData!!.itemCount) {
                        val item = mClipData.getItemAt(i)
                        val uri = item.uri
                        getImageFilePath(uri)
                    }
                } else if (data!!.data != null) {
                    val uri = data.data
                    getImageFilePath(uri)
                }
            }
        }
    }

}
