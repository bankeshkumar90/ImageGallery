package com.nowfloats.packrat.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.ViewModelProviders
import com.nowfloats.packrat.R
import com.nowfloats.packrat.repository.MyRepository
import com.nowfloats.packrat.room.EntityClass
import com.nowfloats.packrat.viewModel.MyViewModel
import com.nowfloats.packrat.viewModel.ViewModelFactory
import com.nowfloats.packrat.home.MyApplication
import kotlinx.android.synthetic.main.fragment_image_preview.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class ImagePreview : Fragment() {

    lateinit var viewModel: MyViewModel
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var myApplication: MyApplication
    lateinit var myRepository: MyRepository
    private var uri: String? = ""
    private var albumName = ""
    private var imageName: String? = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        uri = arguments?.getString("uri")
        imageName = arguments?.getString("image")
        return inflater.inflate(R.layout.fragment_image_preview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()

        imgPreview.setImageURI(Uri.parse(uri))
        btnSaveImage.setOnClickListener {
            showDialog()
        }

    }

    private fun initViews() {
        myApplication = activity?.application as MyApplication
        myRepository = myApplication.myRepository
        viewModelFactory = ViewModelFactory(myRepository)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(MyViewModel::class.java)
    }

    fun showDialog() {
        val builder: AlertDialog.Builder = android.app.AlertDialog.Builder(requireActivity())
        builder.setTitle("Album Name")

        val current_time = Calendar.getInstance().time
        // Set up the input
        val input = EditText(requireContext())
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setHint("Enter Album Name")
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        // Set up the buttons
        builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
            albumName = input.text.toString()
            val entityClass =
                EntityClass(imageName!!, current_time.toString(), albumName, uri!!)

            /*
            below code saves the image information such as image name, album name, time stamp,
             image path url into database using a coroutine
             */

            CoroutineScope(Dispatchers.IO).launch {
                viewModel.addImage(entityClass)
            }
            activity?.onBackPressed()
        })
        builder.setNegativeButton(
            "Cancel",
            DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        builder.show()
    }
}