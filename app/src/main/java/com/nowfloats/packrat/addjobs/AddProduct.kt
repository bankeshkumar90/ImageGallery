package com.nowfloats.packrat.addjobs

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.ContentUris
import android.content.Context
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
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
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
import com.nowfloats.packrat.homescreen.DashBoardFragment


class AddProduct : Fragment(), ClicTabItemListener, ClickListener, ProdClickListener {
    private lateinit var addViewModel: AddProductViewModel
    lateinit var viewModel: MyViewModel
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var myApplication: MyApplication
    lateinit var myRepository: MyRepository

    private var imageList = emptyList<EntityClass>()
    var tabLayout: TabLayout? = null
    var viewPager: ViewPager? = null
    var pagerAdapter: AddPagerAdapter? = null
    private var imagePathList = ArrayList<String>()
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        (activity as AppCompatActivity?)!!.supportActionBar!!.setTitle(R.string.add_product)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        setHasOptionsMenu(true)
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
        imagePathList = arguments?.getStringArrayList(AppConstant.IMAGE_LIST) as ArrayList<String>
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
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.upload_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    fun uploadAllImages() {
        for (imagepath in imagePathList) {
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
                    if(uploadCycleCount>=imagePathList.size){
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
            for (i in 0 until addViewModel.fragmentMapObj.size){
                val products = addViewModel?.fragmentMapObj?.get(i)
                for (product in products!!){
                    var jsonArray = JSONArray()
                    var jsonObjectProperties = JSONObject()

                    if(product.productVisible==true){
                        var productObject = JSONObject()
                        productObject.put(product.productName, product.productValue)
                        jsonArray.put(productObject)
                    }

                    if(product.priceVisible==true){
                        var priceObject = JSONObject()
                        priceObject.put(product.price, product.priceValue)
                        jsonArray.put(priceObject)
                    }

                    if(product.barcodeVisbile==true){
                        var barCodeObject = JSONObject()
                        barCodeObject.put(product.barcode, product.barcodeValue)
                        jsonArray.put(barCodeObject)
                    }

                    if(product.quantityVisible==true){
                        var quantityObject = JSONObject()
                        quantityObject.put(product.quantity, product.quantityValue)
                        jsonArray.put(quantityObject)
                    }

                    if(product.othersVisible==true){
                        var othersObject= JSONObject()
                        if(!product.othersName.equals("",true)){
                            othersObject.put(product.othersName, product.othersValue)
                            jsonArray.put(othersObject)
                        }
                    }
                    if(jsonArray.length()>0) {
                        jsonObjectProperties.put("properies", jsonArray)
                        productJSONArray.put(jsonObjectProperties)
                    }
                }
               /* propery = properies(jsonArray)
                propertyList.add(propery)*/
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
                landToDashBoard()
            }

            override fun onFailure(call: Call<ApiResponse?>, t: Throwable) {
                loading?.dismiss()
                t.message?.let {
                    Log.e("Upload error:", it)
                }            }
        })
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.upload -> {
                //Upload image file function goes here - startImageUploadService(getRandomCollectionId())
                //    uploadAllImages(imageList)
                saveCurrentData(previousSelectedPosition)
                saveProductDatainDb()
                Handler().postDelayed({
                    loading = ProgressDialog(viewObj.context!!)
                    loading!!.setCancelable(true);
                    loading!!.setMessage(AppConstant.IN_PROGRESS);
                    loading!!.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    loading!!.show()
                    generatedCollectionId = AppConstant().getRandomCollectionId(context!!)
                    uploadAllImages()
                    /*val apiService = Network.instance.create(
                    ApiService::class.java
                     )
                    saveMetaDataToServer(apiService)*/

                },DELAY)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveProductDatainDb() {
        addViewModel.saveFormMetaData()
    }

    private fun initViews(view: View) {
        myApplication = activity?.application as MyApplication
        myRepository = myApplication.myRepository
        imageList = arrayListOf<EntityClass>()
        viewModelFactory = ViewModelFactory(myRepository)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(MyViewModel::class.java)

        initHeaderItems(view)
    }
    private fun initHeaderItems(view: View){
        val linearLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        imageAdapter = ImageAdapter( this, imagePathList)
        view.tab_recycler.apply {
            layoutManager = linearLayoutManager
            adapter = imageAdapter
        }
        imageAdapter.setImageSelected(0)
        setProductRecyclerView(ArrayList<metaDataBeanItem>())
        setObserver()
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

    override fun onClick(position: Int) {
        //will add prod fragment instance here -  saveCurrentWithOldPosition(oldPosition:Int)

        shelfSelected(position)
        Handler().postDelayed({
            imageAdapter.setImageSelected(position)
        },DELAY)
    }

    override fun onClickDelete(position: Int?) {
        //will delete prd frag instance
        var imageDelted = false
        if (position != null) {
            imageDelted = imageAdapter.deleteImage(position)
        }
        if(imageDelted==false){
            return
        }
        //first remove from savedfragment then reduce position by 1 if position >0 then set recy pos otherise landdashboard

        if(imageAdapter.itemCount<1){
            landToDashBoard()
        }else{
            addViewModel.deletFragmentData(position!!, imageAdapter.itemCount)

            if(previousSelectedPosition>= imageAdapter.itemCount){
                previousSelectedPosition = previousSelectedPosition - 1
            }

            try {
                imageAdapter.setImageSelected(previousSelectedPosition)
                setProductRecyclerView(addViewModel.fragmentMapObj.get(previousSelectedPosition)!!)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
        //now cal
    }

    fun setObserver() {
        addViewModel.clickadd.observe(this, Observer {
            prodAdapter.updateList(metaDataBeanItem())
        })
        addViewModel.addBottomClick.observe(this, Observer {
            if (isclickBottomView) {
                saveCurrentData(previousSelectedPosition)
                Handler().postDelayed({
                    val viewHolder: ProductDataAdapter.PickerViewHolder? = viewObj.productListItem.findViewHolderForAdapterPosition(addclick_position) as ProductDataAdapter.PickerViewHolder?
                    prodAdapter.setFormView(it!!.mTitle, viewHolder!!, addclick_position)
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
            updateRecylerView(prodAdapter.productList)
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
//        bottomViewDialog = BottomViewAddProductData()
        println("values>>>0>$position")
        addclick_position = position
        isclickBottomView = true
        bottomViewDialog = FullBottomSheetDialogFragment(position)
        bottomViewDialog.setStyle(0, R.style.BottomSheetDialog)
        bottomViewDialog.show(fragmentManager!!, BottomViewDialog.TAG)
    }

    private fun setProductRecyclerView(prducts :ArrayList<metaDataBeanItem>) {
        try {
            val linearLayoutManager =
                LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
            linearLayoutManager.setReverseLayout(true)
            viewObj.productListItem.layoutManager = linearLayoutManager
            prodAdapter = ProductDataAdapter(context!!, this, prducts)
            viewObj.productListItem.adapter = prodAdapter
            prodAdapter.setData(prducts!!)
            prodAdapter.notifyDataSetChanged()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
    private fun updateRecylerView(prducts :ArrayList<metaDataBeanItem>){
        prodAdapter = ProductDataAdapter(context!!, this, prducts)
        viewObj.productListItem.adapter = prodAdapter
        prodAdapter.setData(prducts!!)
        prodAdapter.notifyDataSetChanged()
    }
    fun shelfSelected(position: Int?){
        saveCurrentData(previousSelectedPosition)
        if(previousSelectedPosition==position)
            return
        Handler().postDelayed({
            previousSelectedPosition = position!!
            if(addViewModel.fragmentMapObj.get(position)==null)
                setProductRecyclerView(ArrayList<metaDataBeanItem>())
            else {
                updateRecylerView(addViewModel.fragmentMapObj.get(position)!!)
            }
        }, DELAY)

    }
    fun landToDashBoard(){
        /*for (fragment in fragmentManager?.getFragments()!!) {
            fragmentManager?.beginTransaction()?.remove(fragment)?.commit()
        }*/

        for (fragment in requireActivity().supportFragmentManager.getFragments()) {
            requireActivity().supportFragmentManager.beginTransaction().remove(fragment).commit()
        }
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        (activity as AppCompatActivity?)!!.supportActionBar!!.setTitle(R.string.app_name)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        val ft: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        ft.replace(R.id.fram_dashboard, DashBoardFragment())
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        ft.addToBackStack(null)
        ft.commit()
        addViewModel?.clearFragmentData()
        viewModel?.clearViewModelData()
    }
    fun saveCurrentData(position: Int){
        for (i in 0 until prodAdapter.productList.size){
            try {
                val view = viewObj.productListItem.findViewHolderForLayoutPosition(i)
                //val productValue: EditText = view.productListItem.getChildAt(i).findViewById(R.id.valueProduct)
                val productValue: EditText = view?.itemView?.findViewById(R.id.valueProduct)!!
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
                prodAdapter.productList[i].othersName = othersName.text.toString()
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
        addViewModel.updateFragmentIndex(position, prodAdapter.productList)
    }

}

fun Bundle.putParcelable(requestType: String, get: ArrayList<metaDataBeanItem>?) {

}

private fun <T> Call<T>?.enqueue(callback: Callback<ResponseDTO>) {

}
