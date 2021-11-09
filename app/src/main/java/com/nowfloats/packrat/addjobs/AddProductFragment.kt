package com.nowfloats.packrat.addjobs

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.nowfloats.packrat.R
import com.nowfloats.packrat.clickInterface.ClicTabItemListener
import com.nowfloats.packrat.clickInterface.ClickListener
import com.nowfloats.packrat.databaserepository.MyRepository
import com.nowfloats.packrat.homescreen.MyApplication
import com.nowfloats.packrat.imageViewModel.MyViewModel
import com.nowfloats.packrat.imageViewModel.ViewModelFactory
import com.nowfloats.packrat.network.ApiResponse
import com.nowfloats.packrat.network.ApiService
import com.nowfloats.packrat.network.Network
import com.nowfloats.packrat.network.ResponseDTO
import com.nowfloats.packrat.roomdatabase.EntityClass
import com.nowfloats.packrat.utils.AppConstant
import kotlinx.android.synthetic.main.fragment_add_product.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AddProductFragment : Fragment(), ClicTabItemListener, ClickListener {
    private lateinit var addViewModel: AddProductViewModel
    lateinit var viewModel: MyViewModel
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var myApplication: MyApplication
    lateinit var myRepository: MyRepository

    private lateinit var imageAdapter: AddProductTopAdapter
    private var imageList = emptyList<EntityClass>()
    private var fragmentList = ArrayList<Fragment>()
//    var tabLayout: TabLayout? = null
//    var viewPager: ViewPager? = null
//    var pagerAdapter: AddPagerAdapter? = null

    /*   private var mAdapter: ItemAdapter? = null
       private var mBehavior: BottomSheetBehavior<*>? = null
       private var mBottomSheetDialog: BottomSheetDialog? = null
       private var mDialogBehavior: BottomSheetBehavior<*>? = null*/

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
        initViews()
//        bindBottomView(view)
        setTopRecyclerView()
        btn_add_product.setOnClickListener {
            addViewModel.addViewOnClick()
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

    fun uploadAllImages(imagePathList: List<EntityClass>) {
        for (imagepath in imagePathList) {
            Handler().postDelayed({
                GlobalScope.launch(Dispatchers.Main) {
                    uploadImageService(imagepath.path.toString())
                }
            }, 1000)
        }
    }

    fun uploadImageService(imagePath: String) {
        try {
            val apiService = Network.instance.create(
                ApiService::class.java
            )

            val file = File(imagePath)
            val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            val part: MultipartBody.Part =
                MultipartBody.Part.createFormData("file", file.name, requestBody)


            // MultipartBody.Part is used to send also the actual file name

            //val requestFile: RequestBody = RequestBody.create(context!!.contentResolver.getType(Uri.fromFile( File(imagePath)))!!.toMediaTypeOrNull(), file)

            // MultipartBody.Part is used to send also the actual file name
            val body: MultipartBody.Part =
                MultipartBody.Part.createFormData("file", file.name, requestBody)

            // add another part within the multipart request
            val descriptionString = AppConstant().getRandomCollectionId(context!!)
            val description = RequestBody.create(
                MultipartBody.FORM, descriptionString
            )

            // finally, execute the request
            val call: Call<ApiResponse?>? = apiService.upload(description, body)
            call?.enqueue(object : Callback<ApiResponse?> {
                override fun onResponse(
                    call: Call<ApiResponse?>,
                    response: Response<ApiResponse?>
                ) {
                    Log.v("Upload", "success")
                    Toast.makeText(context!!, "" + "File uploaded successfully", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onFailure(call: Call<ApiResponse?>, t: Throwable) {
                    t.message?.let {
                        Log.e("Upload error:", it)
                    }
                }
            })

            /*    apiService.uploadImage(part, "bankesh123")?.enqueue(object : Callback<ResponseDTO> {
                    override fun onResponse(call: Call<ResponseDTO>, response: Response<ResponseDTO>) {
                        Toast.makeText(context!!, "" + response.body(), Toast.LENGTH_SHORT)
                            .show()
                    }

                    override fun onFailure(call: Call<ResponseDTO>, t: Throwable) {
                        Toast.makeText(context!!, "" + t.message, Toast.LENGTH_SHORT).show()
                    }
                })
    */

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.upload -> {
                //Upload image file function goes here - startImageUploadService(getRandomCollectionId())
                //    uploadAllImages(imageList)
                saveProductDatainDb()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveProductDatainDb() {
        //addViewModel.getDataForm()
    }

    private fun initViews() {
        myApplication = activity?.application as MyApplication
        myRepository = myApplication.myRepository
        imageList = arrayListOf<EntityClass>()
        viewModelFactory = ViewModelFactory(myRepository)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(MyViewModel::class.java)
//        viewPager = view?.findViewById(R.id.add_fragment)
//        tabLayout = view?.findViewById(R.id.sliding_tabs)
        imageAdapter = AddProductTopAdapter(imageList, this)
//        setCustomviewPager()
    }

    /*private fun setCustomviewPager() {
        viewModel.displayImage().observe(this, androidx.lifecycle.Observer {
            imageList = it
            pagerAdapter = AddPagerAdapter(fragmentManager!!, context!!, imageList)
            viewPager!!.adapter = pagerAdapter
            tabLayout!!.setupWithViewPager(viewPager)
            tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {

                }

                override fun onTabUnselected(tab: TabLayout.Tab) {

                }

                override fun onTabReselected(tab: TabLayout.Tab) {

                }
            })

            // Iterate over all tabs and set the custom view
            for (i in 0 until tabLayout!!.tabCount) {
                val tab = tabLayout!!.getTabAt(i)
                tab!!.customView = pagerAdapter?.getTabView(i, this)
                //tab.getCustomView().findViewById(R.id.tab_badge);
            }
        })
    }*/

    private fun setTopRecyclerView() {
//        val linearLayoutManager =
//            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//        tab_recycler.apply {
//            layoutManager = linearLayoutManager
//            adapter = imageAdapter
//        }

    }

    override fun onClickCross(position: Int?) {
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.deleteImageById(position!!)
        }
//        setCustomviewPager()
    }

    /* private fun showBottomSheetView() {
        mBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
        if (mBottomSheetDialog != null) {
            mBottomSheetDialog!!.dismiss()
        }
    }


    fun bindBottomView(view: View) {
         val mBottomSheet = view.findViewById<View>(R.id.bottomSheetF);
         mBehavior = BottomSheetBehavior.from(mBottomSheet)
         mBehavior!!.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
             override fun onStateChanged(bottomSheet: View, newState: Int) {}
             override fun onSlide(bottomSheet: View, slideOffset: Float) {

             }
         })

         val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

         mAdapter = ItemAdapter(createItems(), this)
         recyclerView.apply {
             layoutManager = linearLayoutManager
             adapter = mAdapter
         }

     }

     fun createItems(): List<Item> {
         val items = ArrayList<Item>()
         items.add(Item(R.drawable.ic_preview_24dp, "Product"))
         items.add(Item(R.drawable.ic_share_24dp, "Price"))
         items.add(Item(R.drawable.ic_link_24dp, "Barcode"))
         items.add(Item(R.drawable.ic_content_copy_24dp, "Quantity"))
         items.add(Item(R.drawable.ic_content_copy_24dp, "Others"))
         return items
     }

     override fun onItemClick(item: Item?) {
         Toast.makeText(context!!, "" + item?.mTitle, Toast.LENGTH_LONG)
             .show()
     }

     @SuppressLint("InflateParams")
     private fun showBottomSheetDialog() {
         if (mBehavior!!.state == BottomSheetBehavior.STATE_EXPANDED) {
             mBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
         }
         val view: View = layoutInflater.inflate(R.layout.sheet, null)
         view.findViewById<View>(R.id.fakeShadow).visibility = View.GONE
         val recyclerView = view.findViewById<View>(R.id.recyclerView) as RecyclerView
         recyclerView.setHasFixedSize(true)
         recyclerView.layoutManager = LinearLayoutManager(context!!)
         recyclerView.adapter = ItemAdapter(createItems(), object : ItemAdapter.ItemListener {
             override fun onItemClick(item: Item?) {
                 mDialogBehavior!!.state = BottomSheetBehavior.STATE_HIDDEN
             }
         })
         mBottomSheetDialog = BottomSheetDialog(context!!)
         mBottomSheetDialog!!.setContentView(view)
         mDialogBehavior = BottomSheetBehavior.from(view.parent as View)
         mBottomSheetDialog!!.show()
         mBottomSheetDialog!!.setOnDismissListener(DialogInterface.OnDismissListener {
             mBottomSheetDialog = null
         })
     }*/

    private fun startImageUploadService(collectionId: String) {
        try {

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onClick(position: Int) {
        val ft: FragmentTransaction = fragmentManager!!.beginTransaction()
        ft.replace(R.id.add_fragment, fragmentList.get(position))
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        ft.addToBackStack(null)
        ft.commit()
    }

    override fun onClickDelete(position: Int?) {
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.deleteImageById(position!!)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.displayImage().observe(this, {
            imageAdapter.updateList(it)
            addFragmentWithList(it)
        })
    }

    fun addFragmentWithList(imageList: List<EntityClass>) {
        /*for (i in 0 until imageList.size) {
            var fragitem = ProductDataFragment.newInstance(i)
            fragmentList.add(fragitem)
        }*/
    }
}


private fun <T> Call<T>?.enqueue(callback: Callback<ResponseDTO>) {

}
