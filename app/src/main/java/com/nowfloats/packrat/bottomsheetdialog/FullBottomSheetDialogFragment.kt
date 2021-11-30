package com.nowfloats.packrat.bottomsheetdialog

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.annotation.NonNull
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nowfloats.packrat.R
import com.nowfloats.packrat.addjobs.AddProductViewModel
import com.nowfloats.packrat.addjobs.MetaDataBeanItem
import com.nowfloats.packrat.network.RegexApiResponse
import kotlin.collections.ArrayList

class FullBottomSheetDialogFragment(var recyclerViewPosition:Int, var propertyList: ArrayList<RegexApiResponse>) : BottomSheetDialogFragment(), ItemAdapter.ItemListener {
    private var mBehavior: BottomSheetBehavior<View>? = null
    private lateinit var dataDialogModel: AddProductViewModel
    var metDataItemList: ArrayList<MetaDataBeanItem> =  ArrayList<MetaDataBeanItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataDialogModel =
            ViewModelProviders.of(requireActivity()).get(AddProductViewModel::class.java)
    }

    @NonNull
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog: BottomSheetDialog =
            super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        val view = View.inflate(getContext(), R.layout.sheet, null)
        view.findViewById<View>(R.id.fakeShadow).visibility = View.GONE
        val recyclerView: RecyclerView = view.findViewById<View>(R.id.recyclerView) as RecyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.setLayoutManager(LinearLayoutManager(getContext()))
        metDataItemList = processDataToPass(propertyList)
        val itemAdapter = ItemAdapter(metDataItemList, this, recyclerViewPosition)
        recyclerView.setAdapter(itemAdapter)
        dialog.setContentView(view)
        mBehavior = BottomSheetBehavior.from(view.parent as View)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        mBehavior?.setState(BottomSheetBehavior.STATE_EXPANDED)
    }


    override fun onItemClick(item: MetaDataBeanItem?, position: Int) {
        mBehavior?.setState(BottomSheetBehavior.STATE_HIDDEN)
        dataDialogModel.bottomDialogClick(item!!, position)
    }

    private fun processDataToPass(propertyList: ArrayList<RegexApiResponse>): ArrayList<MetaDataBeanItem> {
        for(i in 0 until propertyList.size){
            var metaDataBeanItem = MetaDataBeanItem()
            metaDataBeanItem.productName = ""+propertyList[i].name
            metaDataBeanItem.productValue = ""+propertyList[i].value
            metaDataBeanItem.productRegex = ""+propertyList[i].regEx
            metDataItemList.add(metaDataBeanItem)
        }
        return metDataItemList
    }

}