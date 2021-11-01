package com.nowfloats.packrat.addjobs

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.nowfloats.packrat.R
import com.nowfloats.packrat.bottomsheetdialog.BottomViewDialog
import com.nowfloats.packrat.bottomsheetdialog.FullBottomSheetDialogFragment
import com.nowfloats.packrat.clickInterface.ProdClickListener
import com.nowfloats.packrat.roomdatabase.ProductEntityClass
import kotlinx.android.synthetic.main.product_item.*


class ProductDataFragment : Fragment(), ProdClickListener {
    private lateinit var dataModel: AddProductViewModel
    private lateinit var prodAdapter: ProductDataAdapter
    private lateinit var bottomViewDialog: FullBottomSheetDialogFragment

    //    private var prodList = emptyList<ProductEntityClass>()
    private var tabPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataModel = ViewModelProviders.of(requireActivity()).get(AddProductViewModel::class.java)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
//            prodAdapter.notifyDataSetChanged()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater!!.inflate(R.layout.product_item, container, false)
        prodAdapter = ProductDataAdapter(context!!, this)
        setObserver()
        return view
    }

    fun setObserver() {
        dataModel.clickadd.observe(this, Observer {
            var prodlist = ProductEntityClass("", "", "", "")
            prodAdapter.updateList(prodlist)
        })
        dataModel.addBottomClick.observe(this, Observer {
            prodAdapter.setFormView(it!!.mTitle)
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
        setRecyclerView()
    }

    private fun setRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        add_recyclerview.apply {
            layoutManager = linearLayoutManager
            adapter = prodAdapter
        }
    }


    companion object {
        val Positionval = "position"
        fun newInstance(tabPosition: Int): ProductDataFragment {
            /*val args = Bundle()
            args.putInt(Positionval, page)
            val fragment = ProductDataFragment()
            fragment.arguments = args
            return fragment*/
            val tabFragment = ProductDataFragment()
            tabFragment.tabPosition = tabPosition
            return tabFragment
        }
    }

    @SuppressLint("WrongConstant")
    override fun onClickAdd(position: Int) {
//        bottomViewDialog = BottomViewAddProductData()
        bottomViewDialog = FullBottomSheetDialogFragment()
        bottomViewDialog.setStyle(0, R.style.BottomSheetDialog)
        bottomViewDialog.show(fragmentManager!!, BottomViewDialog.TAG)
    }

    override fun onClickItemDelete(position: Int?) {
        dataModel.deleteViewOnClick(position!!)
    }


}
