package com.nowfloats.packrat.clickInterface

import android.widget.LinearLayout
import com.nowfloats.packrat.addjobs.metaDataBeanItem

interface ProdClickListener {
    fun onClickAdd(position: Int)
    fun onClickItemDelete(position: Int?)
    fun onItemDeleteAtPos(position: Int, productList:ArrayList<metaDataBeanItem>)
}