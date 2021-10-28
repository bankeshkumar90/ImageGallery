package com.nowfloats.packrat.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.nowfloats.packrat.R
import com.nowfloats.packrat.addproduct.AddPagerAdapter
import com.nowfloats.packrat.addproduct.AddProductAdapter
import com.nowfloats.packrat.addproduct.AddProductViewModel
import com.nowfloats.packrat.clickInterface.ClickListener
import com.nowfloats.packrat.home.MyApplication
import com.nowfloats.packrat.repository.MyRepository
import com.nowfloats.packrat.room.EntityClass
import com.nowfloats.packrat.viewModel.MyViewModel
import com.nowfloats.packrat.viewModel.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddProduct : Fragment(), ClickListener {
    private lateinit var addViewModel: AddProductViewModel
    lateinit var viewModel: MyViewModel
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var myApplication: MyApplication
    lateinit var myRepository: MyRepository
//    private lateinit var imageAdapter: AddProductAdapter
    private var imageList = emptyList<EntityClass>()
    var tabLayout: TabLayout? = null
    var viewPager: ViewPager? = null
    var pagerAdapter:AddPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addViewModel = ViewModelProviders.of(this).get(AddProductViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return return inflater.inflate(R.layout.fragment_add_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        setRecyclerView()
    }

    private fun initViews() {
        myApplication = activity?.application as MyApplication
        myRepository = myApplication.myRepository
        imageList = arrayListOf<EntityClass>()
//        imageAdapter = AddProductAdapter(imageList, this)
        viewModelFactory = ViewModelFactory(myRepository)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(MyViewModel::class.java)
        viewPager = view?.findViewById(R.id.add_fragment)
        tabLayout = view?.findViewById(R.id.sliding_tabs)
        setCustomviewPager()
    }

    private fun setCustomviewPager() {
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
                tab!!.customView = pagerAdapter?.getTabView(i)
                //tab.getCustomView().findViewById(R.id.tab_badge);
            }
        })
    }

    private fun setRecyclerView() {
        /*val linearLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        tab_recycler.apply {
            layoutManager = linearLayoutManager
            adapter = imageAdapter
        }*/
    }

    override fun onClick(position: Int) {
    }

    override fun onClickDelete(position: Int?) {
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.deleteImageById(position!!)
        }
    }
}