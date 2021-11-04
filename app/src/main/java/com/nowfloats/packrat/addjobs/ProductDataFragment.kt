package com.nowfloats.packrat.addjobs

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nowfloats.packrat.R
import com.nowfloats.packrat.bottomsheetdialog.BottomViewDialog
import com.nowfloats.packrat.bottomsheetdialog.FullBottomSheetDialogFragment
import com.nowfloats.packrat.clickInterface.ProdClickListener
import com.nowfloats.packrat.utils.SharedPreferencesManager
import kotlinx.android.synthetic.main.product_item.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.reflect.Type
import java.util.*
import java.util.logging.Handler
import kotlin.collections.ArrayList


class ProductDataFragment : Fragment(), ProdClickListener {
    private lateinit var dataModel: AddProductViewModel
    private lateinit var prodAdapter: ProductDataAdapter
    private lateinit var bottomViewDialog: FullBottomSheetDialogFragment
    private var isclickBottomView = false

    //    private lateinit var linearLayout: LinearLayout
    private var allProducts: ArrayList<Int>? = ArrayList<Int>()
//    private var alllistProduct: ArrayList<ArrayList<Int>?>? = ArrayList<ArrayList<Int>?>()

    //    val linkList: LinkedList<ArrayList<Int>> = LinkedList(alllistProduct)
    lateinit var ctx: Context


    //    private var prodList = emptyList<ProductEntityClass>()
    private var addclick_position = 0
    private var tabPosition = 0
    private var _hasLoadedOnce = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataModel = ViewModelProviders.of(requireActivity()).get(AddProductViewModel::class.java)
        if (getArguments() != null) {
            /*if (allProducts!!.size > 0)
                allProducts!!.clear()
            allProducts = getArguments()?.getIntegerArrayList(ARG_PRODUCTS);
            alllistProduct!!.add(allProducts)*/
            tabPosition = getArguments()!!.getInt("KEY_POSITION")
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && _hasLoadedOnce) {
            /* val ftr: FragmentTransaction = fragmentManager!!.beginTransaction()
             ftr.detach(this).attach(this).commit()
             prodAdapter.notifyDataSetChanged()
             _hasLoadedOnce = true*/
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater!!.inflate(R.layout.product_item, container, false)
        prodAdapter = ProductDataAdapter(ctx, this)
        setObserver()
        return view
    }

    fun setObserver() {
        dataModel.clickadd.observe(this, Observer {
//            var prodlist = ProductEntityClass("", "", "", "")
            prodAdapter.updateList(it)
        })
        dataModel.addBottomClick.observe(this, Observer {
            if (isclickBottomView) {
                val viewHolder: ProductDataAdapter.PickerViewHolder? =
                    add_recyclerview.findViewHolderForAdapterPosition(addclick_position) as ProductDataAdapter.PickerViewHolder?
                prodAdapter.setFormView(it!!.mTitle, viewHolder!!, addclick_position)

                isclickBottomView = false
            }

            /* Toast.makeText(context!!, "" + it?.mTitle, Toast.LENGTH_LONG)
                 .show()*/
        })
        dataModel.clickdeleteview.observe(this, Observer {
            prodAdapter.deleteview(it)
        })
        dataModel.getData.observe(this, Observer {
            dataModel.getProductData.value = prodAdapter.getProductFormData()

        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        text_view.text = tabPosition.toString() + "  " + allProducts!!.size+"  "+prodAdapter.viewList!!.size
//        setRecyclerView()
    }

    private fun setRecyclerView() {
        val linearLayoutManager =
            LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false)
//        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        add_recyclerview.layoutManager = linearLayoutManager
        add_recyclerview.adapter = prodAdapter
        prodAdapter.setData(allProducts!!)
        prodAdapter.notifyDataSetChanged()
//        prodAdapter.notifyItemChanged(tabPosition)
//        prodAdapter.setData(allProducts!!)
        /* add_recyclerview.apply {
             layoutManager = linearLayoutManager
             adapter = prodAdapter
             prodAdapter.setData(allProducts!!)
         }*/
    }


    companion object {
        private val ARG_PRODUCTS = "PRODS"

        fun newInstance(
            ctx: Context,
            tabPosition: Int,
            pList: List<Int>
        ): ProductDataFragment {
            /*  val args = Bundle()
              args.putInt(Positionval, tabPosition)
              args.putInt(Positionval, pList)
              val fragment = ProductDataFragment()
              fragment.arguments = args
              return fragment*/
            val fragment = ProductDataFragment()
            fragment.ctx = ctx
            val args = Bundle()
            args.putIntegerArrayList(ARG_PRODUCTS, pList as ArrayList<Int>)
            args.putInt("KEY_POSITION", tabPosition)
            fragment.arguments = args
            return fragment
            /*val tabFragment = ProductDataFragment()
            tabFragment.tabPosition = tabPosition
            return tabFragment*/
        }
    }

    @SuppressLint("WrongConstant")
    override fun onClickAdd(position: Int) {
//        bottomViewDialog = BottomViewAddProductData()
        println("values>>>0>$position")
        addclick_position = position
        isclickBottomView = true
        bottomViewDialog = FullBottomSheetDialogFragment()
        bottomViewDialog.setStyle(0, R.style.BottomSheetDialog)
        bottomViewDialog.show(fragmentManager!!, BottomViewDialog.TAG)
    }


    override fun onClickItemDelete(position: Int?) {
        dataModel.deleteViewOnClick(position!!)
    }

    public fun setTabclickRefresh(tabPosi: Int) {
//        prodAdapter.notifyDataSetChanged()
        if (SharedPreferencesManager(ctx).getListFromLocal("item_$tabPosi") != null)
            println("chaeck_tab_click>0>  $tabPosi  =  $tabPosition  ${SharedPreferencesManager(ctx).getListFromLocal("item_$tabPosi")!!.size}")
        if (SharedPreferencesManager(ctx).getListFromLocal("item_$tabPosi") != null) {
            if (allProducts!!.size>0)
                allProducts!!.clear()
            for (i in 0 until SharedPreferencesManager(ctx).getListFromLocal("item_$tabPosi")!!.size) {
                allProducts!!.add(SharedPreferencesManager(ctx).getListFromLocal("item_$tabPosi")!!.get(i)!!)
            }
        }else{
            println("chaeck_tab_click>0>else  $tabPosi  =  $tabPosition  ${allProducts!!.size}")
        }
        android.os.Handler().postDelayed({
            GlobalScope.launch(Dispatchers.Main) {
                setRecyclerView()
            }
        }, 1000)
    }

    public fun setTabclickUnselect(tabPosi: Int) {
        SharedPreferencesManager(ctx).saveListInLocal(prodAdapter!!.viewList, "item_$tabPosi")
        prodAdapter!!.viewList?.clear()
        println("chaeck_tab_click>1>  $tabPosi  =  $tabPosition  ${SharedPreferencesManager(ctx).getListFromLocal("item_$tabPosi")!!.size}")
    }

}
