package com.nowfloats.packrat.addjobs

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.gson.JsonObject
import com.nowfloats.packrat.R
import com.nowfloats.packrat.bottomsheetdialog.BottomViewDialog
import com.nowfloats.packrat.bottomsheetdialog.FullBottomSheetDialogFragment
import com.nowfloats.packrat.clickInterface.ClicTabItemListener
import com.nowfloats.packrat.clickInterface.ClickListener
import com.nowfloats.packrat.clickInterface.ProdClickListener
import com.nowfloats.packrat.databaserepository.MyRepository
import com.nowfloats.packrat.homescreen.MyApplication
import com.nowfloats.packrat.imageViewModel.MyViewModel
import com.nowfloats.packrat.imageViewModel.ViewModelFactory
import com.nowfloats.packrat.imagelistadapter.ImageAdapter
import com.nowfloats.packrat.network.*
import com.nowfloats.packrat.roomdatabase.EntityClass
import com.nowfloats.packrat.utils.AppConstant
import kotlinx.android.synthetic.main.fragment_add_product.*
import kotlinx.android.synthetic.main.fragment_add_product.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import com.google.gson.JsonParser
import com.nowfloats.packrat.clickInterface.OnImageDialogSelector
import com.nowfloats.packrat.homescreen.DashBoardFragment
import com.nowfloats.packrat.roomdatabase.ProductEntityClass
import kotlinx.android.synthetic.main.fragment_image_preview.*
import java.util.*
import kotlin.collections.ArrayList
import android.app.Activity
import android.content.pm.PackageManager
import android.view.inputmethod.InputMethodManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.fragment_settings.*

import androidx.core.app.ActivityCompat.requestPermissions
import java.text.SimpleDateFormat


class AddProduct : Fragment(), ClicTabItemListener, ClickListener, ProdClickListener {
    private lateinit var addViewModel: AddProductViewModel
    lateinit var viewModel: MyViewModel
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var myApplication: MyApplication
    lateinit var myRepository: MyRepository

    var tabLayout: TabLayout? = null
    var viewPager: ViewPager? = null
    var pagerAdapter: AddPagerAdapter? = null
    private lateinit var imageAdapter: ImageAdapter
    private lateinit var bottomViewDialog: FullBottomSheetDialogFragment
    private var isclickBottomView = false
    private lateinit var prodAdapter: ProductDataAdapter
    private var addclick_position = 0
    private lateinit var viewObj:View
    private var previousSelectedPosition = 0
    private val DELAY = 500L
    private var uploadCycleCount = 0
    private var generatedCollectionId = ""
    var loading: ProgressDialog? = null
    var image_uri : Uri? = null
    private lateinit var imagePicker: BottomViewDialog
    private var shelf = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shelf = arguments?.getBoolean(AppConstant.SHELF,false)!!

       // setHasOptionsMenu(true)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        addViewModel = ViewModelProviders.of(requireActivity()).get(AddProductViewModel::class.java)
        return return inflater.inflate(R.layout.fragment_add_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewObj = view
        initViews(view)

//        bindBottomView(view)
        btn_add_product.setOnClickListener {
            saveCurrentData(previousSelectedPosition)
            Handler().postDelayed({
                addViewModel.addViewOnClick()
            },DELAY)
            //showBottomSheetView()
        }
        //showBottomSheetDialog()
        addViewModel.getProductData.observe(this, Observer {
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.addProductData(it)
            }
        })
        btnAddShelfImages.setOnClickListener(View.OnClickListener {
            //open bottomsheet dialog and override image calback
            dispatchTakePictureIntent()
        })
        backButtonAddProduct.setOnClickListener(){
            requireActivity().supportFragmentManager?.popBackStack(AppConstant.ADD_PRODUCT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
        llUpload.setOnClickListener(){
            prodAdapter.saveLatestItemData()
            saveCurrentData(previousSelectedPosition)
            try{
                if(imageAdapter.itemCount<1 || viewModel.imageList.size==0){
                    Toast.makeText(myApplication, AppConstant.IMAGE_MANDATORY, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if(prodAdapter.parentProductList.size<1){
                    Toast.makeText(myApplication, myApplication.resources.getString(R.string.blankProduct), Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if(prodAdapter.parentProductList[0].size<1){
                    Toast.makeText(myApplication, myApplication.resources.getString(R.string.blankProduct), Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if(prodAdapter.parentProductList[0][0].productValue.equals("")||prodAdapter.parentProductList[0][0].productName.equals("")){
                    Toast.makeText(myApplication, myApplication.resources.getString(R.string.blankProduct), Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                var lastItem = prodAdapter.parentProductList[prodAdapter.parentProductList.size-1]
                if(prodAdapter.parentProductList[prodAdapter.parentProductList.size-1][0].productName.equals("",true) ||
                    prodAdapter.parentProductList[prodAdapter.parentProductList.size-1][0].productValue.equals("",true)){
                    Toast.makeText(myApplication, myApplication.resources.getString(R.string.blankProduct), Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if(lastItem[lastItem.size-1].productValue.isNullOrEmpty() || lastItem[lastItem.size-1].productName.isNullOrEmpty()){
                    Toast.makeText(myApplication, myApplication.resources.getString(R.string.blankProduct), Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if(!prodAdapter.haveAnyChild()){
                    Toast.makeText(myApplication, myApplication.resources.getString(R.string.blankProduct), Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }catch (e:Exception){
                e.printStackTrace()
                Toast.makeText(myApplication, myApplication.resources.getString(R.string.blankProduct), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            loading = ProgressDialog(viewObj.context!!)
            loading!!.setCancelable(true);
            loading!!.setMessage(AppConstant.IN_PROGRESS);
            loading!!.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            loading!!.show()
            generatedCollectionId = AppConstant().getRandomCollectionId(context!!)
            var processedMetaData = processMetaDataToSave()
            saveProductDatainDb(processedMetaData)

        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.upload_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    fun uploadAllImages() {
        for (imagepath in viewModel.imageList) {
            Handler().postDelayed({
                GlobalScope.launch(Dispatchers.Main) {
                    var actualFilePath = AppConstant.getPath(context!!, Uri.parse(imagepath))
                    uploadImageService(""+actualFilePath)
                }
            }, DELAY)
        }
    }

    fun uploadImageService(imagePath: String) {
        try {
            val apiService = Network.instance.create(
                ApiService::class.java
            )
            val file = File(imagePath)
            val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            val part: MultipartBody.Part = createFormData("file", file.name, requestBody)
            // MultipartBody.Part is used to send also the actual file name
            val body: MultipartBody.Part = createFormData("file", file.name, requestBody)

            // add another part within the multipart request
            val description = RequestBody.create(
                MultipartBody.FORM, generatedCollectionId
            )

            // finally, execute the request
            val call: Call<ApiResponse?>? = apiService.upload(description, body)
            call?.enqueue(object : Callback<ApiResponse?> {
                override fun onResponse(
                    call: Call<ApiResponse?>,
                    response: Response<ApiResponse?>
                ) {
                    Log.v("Upload", "success")
                    uploadCycleCount++
                    if(uploadCycleCount>=viewModel.imageList.size){
                        Toast.makeText(context!!, "" + "File uploaded successfully", Toast.LENGTH_SHORT)
                            .show()
                        initMetaDataUpload(apiService)
                    }
                }

                override fun onFailure(call: Call<ApiResponse?>, t: Throwable) {
                    t.message?.let {
                        Log.e("Upload error:", it)
                    }
                    loading?.dismiss()
                    Toast.makeText(context!!, "" + "Timeout Occured! Please Re-Try", Toast.LENGTH_SHORT).show()
                }
            })


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initMetaDataUpload(apiService:ApiService){
        Handler().postDelayed({
            GlobalScope.launch(Dispatchers.Main) {
                saveMetaDataToServer(apiService)
                /*val apiService = Network.instance.create(
                ApiService::class.java
                 )*/
            }
        }, DELAY)
    }
    private fun saveMetaDataToServer(apiService:ApiService){
        /*val apiService = Network.instance.create(
            ApiService::class.java
        )*/
        var productJSONArray = JSONArray()
        var requestJSONObject = JSONObject()
        //var propery :properies
        //val propertyList = ArrayList<properies>()
        val productList = ArrayList<products>()
        try {
            for (i in 0 until prodAdapter.parentProductList.size){
                val products = prodAdapter?.parentProductList?.get(i)
                var jsonObjectProperties = JSONObject()
                var jsonArray = JSONArray()

                for (product in products!!){

                    if(!product.productValue.equals("")){
                        var productObject = JSONObject()
                        productObject.put(product.productName, product.productValue)
                        jsonArray.put(productObject)
                    }
                }
                if(jsonArray.length()>0) {
                    jsonObjectProperties.put("properies", jsonArray)
                    productJSONArray.put(jsonObjectProperties)
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
        requestJSONObject.put( "CreatedBy",AppConstant.CREATED_BY)
        requestJSONObject.put( "CreatedByName",AppConstant.CREATED_BY_NAME)
        requestJSONObject.put( "CollectionId",generatedCollectionId)
        requestJSONObject.put( "products",productJSONArray)

        val jsonParser = JsonParser()
        var gsonObject = JsonObject()

        gsonObject = jsonParser.parse(requestJSONObject.toString()) as JsonObject

        val call: Call<ApiResponse?>? = apiService.saveMetaDataToServer(gsonObject)

        call?.enqueue(object : Callback<ApiResponse?> {
            override fun onResponse(
                call: Call<ApiResponse?>,
                response: Response<ApiResponse?>
            ) {
                Log.v("Saved", ""+response?.body()?.message)
                loading?.dismiss()
             }

            override fun onFailure(call: Call<ApiResponse?>, t: Throwable) {
                loading?.dismiss()
                t.message?.let {
                    Log.e("Upload error:", it)
                }            }
        })
    }
    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.upload -> {
                //Upload image file function goes here - startImageUploadService(getRandomCollectionId())
                //    uploadAllImages(imageList)
                prodAdapter.saveLatestItemData()
                saveCurrentData(previousSelectedPosition)
                try{
                    var lastItem = prodAdapter.parentProductList[prodAdapter.parentProductList.size-1]
                    if(prodAdapter.parentProductList[prodAdapter.parentProductList.size-1][0].productName.equals("",true) ||
                        prodAdapter.parentProductList[prodAdapter.parentProductList.size-1][0].productValue.equals("",true)){
                        Toast.makeText(myApplication, myApplication.resources.getString(R.string.blankProduct), Toast.LENGTH_SHORT).show()
                        return false
                    }
                    if(lastItem[lastItem.size-1].productValue.isNullOrEmpty() || lastItem[lastItem.size-1].productName.isNullOrEmpty()){
                        Toast.makeText(myApplication, myApplication.resources.getString(R.string.blankProduct), Toast.LENGTH_SHORT).show()
                        return false
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }
                loading = ProgressDialog(viewObj.context!!)
                loading!!.setCancelable(true);
                loading!!.setMessage(AppConstant.IN_PROGRESS);
                loading!!.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                loading!!.show()
                generatedCollectionId = AppConstant().getRandomCollectionId(context!!)
                var processedMetaData = processMetaDataToSave()
                saveProductDatainDb(processedMetaData)

                *//*Handler().postDelayed({
                    prodAdapter.saveLatestItemData()
                    *//**//*uploadAllImages()
                    val apiService = Network.instance.create(
                    ApiService::class.java
                     )
                    //saveMetaDataToServer(apiService)*//**//*

                },DELAY)*//*
            }
        }
        return super.onOptionsItemSelected(item)
    }*/

    /****
     * This function will save all information of metaData including image information
     */
    private fun saveProductDatainDb(processedMetaData:String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                for (image in viewModel.imageList) {
                    val currentTime = Calendar.getInstance().time
                   /* val sdf = SimpleDateFormat("EEEE")
                    val day = Date()
                    val dayOfTheWeek: String = sdf.format(day)*/


                    val entityClass = EntityClass(
                        image,
                        currentTime.toString(),
                        image,
                        AppConstant().getRandomCollectionId(context!!),
                        false
                    )
                    viewModel.saveImageInformationToRoomDb(entityClass)
                }
                val productEntityClass = ProductEntityClass(generatedCollectionId, false, processedMetaData)
                viewModel.saveMetaDataInformationToRoomDb(productEntityClass)

            }catch (e:Exception){
                e.printStackTrace()
            }
        }
        addViewModel.saveFormMetaData()

        addViewModel.initiateBackGroundProcess(viewModel.imageList, generatedCollectionId, processedMetaData)
        AppConstant().hideSoftKeyboard(requireActivity())
        Toast.makeText(context!!, context!!.resources.getString(R.string.jobSaved), Toast.LENGTH_SHORT).show()
        if(loading!=null)
            loading?.dismiss()
        landToDashBoard()

    }

    /****
     * preparing form data to save into local db
     */
    private fun processMetaDataToSave(): String{
        var productJSONArray = JSONArray()
        var requestJSONObject = JSONObject()
        //val propertyList = ArrayList<properies>()
        val productList = ArrayList<products>()
        try {
            for (i in 0 until prodAdapter.parentProductList.size){
                val products = prodAdapter?.parentProductList?.get(i)
                var jsonObjectProperties = JSONObject()
                var jsonArray = JSONArray()
                for (product in products!!){
                    if(!product.productValue.equals("")){
                        var productObject = JSONObject()
                        productObject.put(product.productName, product.productValue)
                        jsonArray.put(productObject)
                    }
                }
                if(jsonArray.length()>0) {
                    jsonObjectProperties.put("properies", jsonArray)
                    productJSONArray.put(jsonObjectProperties)
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
        var savedEmail = AppConstant().getValuesByTagFromLocalPrefs(context!!, AppConstant.CREATED_BY, AppConstant.CREATED_BY)
        requestJSONObject.put( "CreatedBy",savedEmail)
        requestJSONObject.put( "CreatedByName",savedEmail)
        requestJSONObject.put( "CollectionId",generatedCollectionId)
        requestJSONObject.put( "products",productJSONArray)
        //val jsonParser = JsonParser()
        //var gsonObject = JsonObject()
        //gsonObject = jsonParser.parse(requestJSONObject.toString()) as JsonObject

        return  requestJSONObject.toString()
    }

    private fun initViews(view: View) {
        myApplication = activity?.application as MyApplication
        myRepository = myApplication.myRepository
        viewModelFactory = ViewModelFactory(myRepository)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(MyViewModel::class.java)
        viewModel.imageList = arguments?.getStringArrayList(AppConstant.IMAGE_LIST) as ArrayList<String>

        if(shelf){
            tvUploadType.text = context!!.resources.getString(R.string.shelf_images)
        }else{
            tvUploadType.text = context!!.resources.getString(R.string.shelf_images)
            add_lin.visibility = View.GONE
            addViewModel.addViewOnClick()
        }
        initHeaderItems(view)
    }

    private fun initHeaderItems(view: View){
        val linearLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        imageAdapter = ImageAdapter( this, viewModel.imageList)
        view.tab_recycler.apply {
            layoutManager = linearLayoutManager
            adapter = imageAdapter
        }
        imageAdapter.setImageSelected(0)
        var itemList = ArrayList<ArrayList<metaDataBeanItem>>()
        var arrayItem = ArrayList<metaDataBeanItem>()
        arrayItem.add(metaDataBeanItem())
        itemList.add(arrayItem)
        setProductRecyclerView(itemList)
        setObserver()
        viewModel.getProperties()

    }




    override fun onClickCross(position: Int?) {
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.deleteImageById(position!!)
        }
     }

    private fun startImageUploadService(collectionId: String) {
        try {

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /***
     * is used to select image for tabulation header
     */
    override fun onClick(position: Int) {
        //will add prod fragment instance here -  saveCurrentWithOldPosition(oldPosition:Int)

        /*shelfSelected(position)
        Handler().postDelayed({
            imageAdapter.setImageSelected(position)
        },DELAY)*/
    }

    override fun onClickDelete(position: Int?) {
        //will delete prd frag instance
        var imageDelted = false
        if (position != null) {
            imageDelted = imageAdapter.deleteImageFromPreview(position)
        }
        if(imageDelted==false){
            return
        }
        //first remove from savedfragment then reduce position by 1 if position >0 then set recy pos otherise landdashboard

        if(imageAdapter.itemCount<1){
            //landToDashBoard()
            Toast.makeText(context!!, AppConstant.IMAGE_MANDATORY, Toast.LENGTH_SHORT).show()
        }/*else{
            addViewModel.deletFragmentData(position!!, imageAdapter.itemCount)

            if(previousSelectedPosition>= imageAdapter.itemCount){
                previousSelectedPosition = previousSelectedPosition - 1
            }

            try {
                imageAdapter.setImageSelected(previousSelectedPosition)
                //setProductRecyclerView(addViewModel.fragmentMapObj.get(previousSelectedPosition)!!)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }*/
        //now cal
    }

    fun setObserver() {
        addViewModel.clickadd.observe(this, Observer {
            prodAdapter.saveLatestItemData()
            if(prodAdapter.parentProductList.size>0){
                try {
                    var lastItem =
                        prodAdapter.parentProductList[prodAdapter.parentProductList.size - 1]
                    if (lastItem.size == 0) {
                        return@Observer
                    } else if (lastItem.size > 0) {
                        if (prodAdapter.parentProductList[prodAdapter.parentProductList.size - 1][0].productName.equals(
                                "",
                                true
                            )
                        ) {
                            //Toast.makeText(myApplication, "Info required", Toast.LENGTH_SHORT).show()
                            return@Observer
                        }
                        if (lastItem[lastItem.size - 1].productValue.isNullOrEmpty()) {
                            Toast.makeText(
                                myApplication,
                                myApplication.resources.getString(R.string.blankProduct),
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Observer
                        }
                        prodAdapter.updateChild()
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                    Toast.makeText(
                        myApplication,
                        myApplication.resources.getString(R.string.retry_on_error),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@Observer
                }
            }
            val viewHolder: ProductDataAdapter.PickerViewHolder? = viewObj.productListItem.findViewHolderForAdapterPosition(addclick_position) as ProductDataAdapter.PickerViewHolder?
            var itemList = ArrayList<metaDataBeanItem>()
            itemList.add(metaDataBeanItem())
            prodAdapter.updateList(itemList, viewHolder)
            productListItem.scrollToPosition(prodAdapter.parentProductList.size-1)
        })
        addViewModel.addBottomClick.observe(this, Observer {
            if (isclickBottomView) {
                saveCurrentData(previousSelectedPosition)
                Handler().postDelayed({
                    val viewHolder: ProductDataAdapter.PickerViewHolder? = viewObj.productListItem.findViewHolderForAdapterPosition(addclick_position) as ProductDataAdapter.PickerViewHolder?
                    it!!?.let { it1 -> prodAdapter.setFormView(it1, viewHolder!!, addclick_position) }
                    isclickBottomView = false
                }, DELAY)
            }

            /* Toast.makeText(context!!, "" + it?.mTitle, Toast.LENGTH_LONG)
                 .show()*/
        })
        addViewModel.clickdeleteview.observe(this, Observer {
            saveCurrentData(previousSelectedPosition)
            //addViewModel.deleteFragmentObjectItem(previousSelectedPosition, it)
            prodAdapter.deleteview(it)
            //prodAdapter.notifyItemRemoved(it)
            //prodAdapter.notifyItemRangeChanged(it, prodAdapter.parentProductList.size);
            updateRecylerView(prodAdapter.parentProductList)
            Handler().postDelayed({
                prodAdapter.setChildElementsAfterRoot()
            },500)
            saveCurrentData(previousSelectedPosition)
        })
        addViewModel.saveMetaData.observe(this, Observer {
            //save our local values here
            CoroutineScope(Dispatchers.IO).launch {
                for (i in 0 until  addViewModel.fragmentMapObj.size){
                    try {
                       // viewModel.saveMetaData(addViewModel.fragmentMapObj.get(i)!!)
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }
            }
        })
        CoroutineScope(Dispatchers.IO).launch {
            val items = viewModel.getMetaData()

        }
        //setRecyclerView(productList)
    }
    override fun onClickItemDelete(position: Int?) {
        addViewModel.deleteViewOnClick(position!!)
    }

    override fun onItemDeleteAtPos(position: Int, productList: ArrayList<metaDataBeanItem>) {
       saveCurrentData(previousSelectedPosition)
    }

    @SuppressLint("WrongConstant")
    override fun onClickAdd(position: Int) {
        addclick_position = position
        isclickBottomView = true
        if(viewModel.productProperty==null){
            viewModel.fetchFromAPI()
        }else if(viewModel.productProperty.isEmpty()){
            viewModel.fetchFromAPI()
        } else {
            bottomViewDialog = FullBottomSheetDialogFragment(position, viewModel.productProperty)
            bottomViewDialog.setStyle(0, R.style.BottomSheetDialog)
            bottomViewDialog.show(fragmentManager!!, BottomViewDialog.TAG)
        }
     }

    private fun setProductRecyclerView(prducts :ArrayList<ArrayList<metaDataBeanItem>>) {
        try {
            val linearLayoutManager =
                LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
            linearLayoutManager.setReverseLayout(true)
            viewObj.productListItem.layoutManager = linearLayoutManager
            prodAdapter = ProductDataAdapter(context!!, this, prducts, shelf)
            viewObj.productListItem.adapter = prodAdapter
            prodAdapter.setData(prducts!!)
            prodAdapter.notifyDataSetChanged()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    //This function will delete product of recyler item
    private fun updateRecylerView(prducts :ArrayList<ArrayList<metaDataBeanItem>>){
        prodAdapter = ProductDataAdapter(context!!, this, prducts, shelf)
        viewObj.productListItem.adapter = prodAdapter
        //prodAdapter.setData(prducts!!)
        prodAdapter.notifyDataSetChanged()
    }
    fun shelfSelected(position: Int?){
        saveCurrentData(previousSelectedPosition)
        if(previousSelectedPosition==position)
            return
        Handler().postDelayed({
            previousSelectedPosition = position!!
            if(addViewModel.fragmentMapObj.get(position)==null)
                setProductRecyclerView(ArrayList<ArrayList<metaDataBeanItem>>())
            else {
                //updateRecylerView(addViewModel.fragmentMapObj.get(position)!!)
            }
        }, DELAY)

    }

    fun landToDashBoard(){
        /*for (fragment in fragmentManager?.getFragments()!!) {
            fragmentManager?.beginTransaction()?.remove(fragment)?.commit()
        }*/
        addViewModel?.clearFragmentData()
        viewModel?.clearViewModelData()

        requireActivity().supportFragmentManager?.popBackStack(AppConstant.ADD_PRODUCT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        requireActivity().supportFragmentManager?.popBackStack(AppConstant.IMAGE_PREIVEW_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE)

    }
    fun saveCurrentData(position: Int){
        for (i in 0 until prodAdapter.parentProductList.size){
            try {
                val view = viewObj.productListItem.findViewHolderForLayoutPosition(i)
                //val productValue: EditText = view.productListItem.getChildAt(i).findViewById(R.id.valueProduct)
               /* val productValue: EditText = view?.itemView?.findViewById(R.id.valueProduct)!!
                prodAdapter.productList[i].productValue = productValue.text.toString()

                //val priceValue: EditText = viewObj.productListItem.getChildAt(i).findViewById(R.id.valuePrice)
                val priceValue: EditText = view?.itemView?.findViewById(R.id.valuePrice)
                prodAdapter.productList[i].priceValue = priceValue.text.toString()

                //val barCodeValue: EditText = viewObj.productListItem.getChildAt(i).findViewById(R.id.valueBarcode)
                val barCodeValue: EditText = view?.itemView?.findViewById(R.id.valueBarcode)
                prodAdapter.productList[i].barcodeValue = barCodeValue.text.toString()

                //val quantityValue: EditText = viewObj.productListItem.getChildAt(i).findViewById(R.id.valueQuantity)
                val quantityValue: EditText = view?.itemView?.findViewById(R.id.valueQuantity)
                prodAdapter.productList[i].quantityValue = quantityValue.text.toString()

                //val othersValue: EditText = viewObj.productListItem.getChildAt(i).findViewById(R.id.valueOthers)
                val othersValue: EditText = view?.itemView?.findViewById(R.id.valueOthers)
                prodAdapter.productList[i].othersValue = othersValue.text.toString()

                val othersName: EditText = view?.itemView?.findViewById(R.id.labelOthers)
                prodAdapter.productList[i].othersName = othersName.text.toString()*/
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
        //addViewModel.updateFragmentIndex(position, prodAdapter.parentProductList)
    }



    fun openCamera()
    {
        var imageName = "image${System.currentTimeMillis()}.jpg"
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
        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Pictures"), AppConstant.REQ_GALLERY_CODE)
    }
    private fun showPreview(imagePath:String){
        val linearLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        imageAdapter = ImageAdapter( this, viewModel.imageList)
        viewObj?.tab_recycler.apply {
            layoutManager = linearLayoutManager
            adapter = imageAdapter
        }
        imageAdapter.notifyDataSetChanged()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppConstant.REQ_CAMERA_CODE) {
            var validFile = AppConstant().ifFileExists(context!!, ""+image_uri)

            image_uri?.let {
                if(validFile) {
                    viewModel.addImageToList("" + image_uri)
                }
                if(validFile)
                showPreview(""+it)
            }
        }else if( requestCode == AppConstant.REQ_GALLERY_CODE){
            //update imageList
            if(data==null){
                if(viewModel.imageList.size<1){
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
                        requestPermissions(
                            arrayOf(
                                Manifest.permission.CAMERA,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            ), AppConstant.CAMERA_PERMISSION_REQUIRED
                        )
                    }
                }
            }
        }
        imagePicker = BottomViewDialog(objClick)
        imagePicker.setStyle( 0, R.style.BottomSheetDialog)
        imagePicker.show(fragmentManager!!, BottomViewDialog.TAG)
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
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            AppConstant.CAMERA_PERMISSION_REQUIRED
        )
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


fun Bundle.putParcelable(requestType: String, get: ArrayList<metaDataBeanItem>?) {

}

private fun <T> Call<T>?.enqueue(callback: Callback<ResponseDTO>) {

}
