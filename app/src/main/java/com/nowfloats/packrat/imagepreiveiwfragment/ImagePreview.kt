package com.nowfloats.packrat.imagepreiveiwfragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.nowfloats.packrat.R
import com.nowfloats.packrat.addjobs.AddProduct
import com.nowfloats.packrat.camera.CameraFragment
import com.nowfloats.packrat.clickInterface.ClickListener
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
    private var imageName: String? = ""
    private lateinit var imageAdapter: ImageAdapter
    //private var imageList = emptyList<EntityClass>()
    private var imagePathList = ArrayList<String>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        uri = arguments?.getString(AppConstant.IMAGE_URI)
        imageName = arguments?.getString("image")
        imagePathList = arguments?.getStringArrayList(AppConstant.IMAGE_LIST) as ArrayList<String>
        setHasOptionsMenu(false)
        return inflater.inflate(R.layout.fragment_image_preview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        setRecyclerView()
        imgPreview.setImageURI(Uri.parse(uri))
        btnSaveImage.setOnClickListener {
            val currentTime = Calendar.getInstance().time
            val entityClass =
                EntityClass(uri!!, currentTime.toString(), uri!!)
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.addImage(entityClass)
                sendAddproductScreen()
            }

        }
        btn_open_camera.setOnClickListener {
            //Will save the imagelist in viewmodel and after uploading then push it to local database
            val ft: FragmentTransaction = fragmentManager!!.beginTransaction()
            ft.replace(R.id.fram_dashboard, CameraFragment())
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            ft.addToBackStack(null)
            ft.commit()
        }
    }

    private fun sendAddproductScreen() {
        val bundle = Bundle()
        bundle.putStringArrayList(AppConstant.IMAGE_LIST, imagePathList )
        val addProduct = AddProduct()
        addProduct.arguments = bundle

        val ft: FragmentTransaction = fragmentManager!!.beginTransaction()
        ft.replace(R.id.fram_dashboard, addProduct)
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        ft.addToBackStack(null)
        ft.commit()
    }

    private fun setRecyclerView() {
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
        imageAdapter = ImageAdapter( this, imagePathList)
        viewModelFactory = ViewModelFactory(myRepository)
        viewModel = ViewModelProviders.of(activity!!, viewModelFactory)
            .get(MyViewModel::class.java)
    }



    override fun onResume() {
        super.onResume()
        /*viewModel.displayImage().observe(this, {
            imageAdapter.updateList(it)
        })*/
        viewModel.imageArrayList.observe(this,{
            imageAdapter.updateImageList(it)
        })
    }

    override fun onClick(position: Int) {
        imgPreview.setImageURI(Uri.parse(imagePathList[position]))
    }

    override fun onClickDelete(position: Int?) {
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.deleteImageById(position!!)
        }
    }
    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.clear()
    }
}