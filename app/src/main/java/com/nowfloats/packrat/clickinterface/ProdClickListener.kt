package com.nowfloats.packrat.clickinterface

import com.nowfloats.packrat.addjobs.MetaDataBeanItem

interface ProdClickListener {
    fun onClickAdd(position: Int)
    fun onClickItemDelete(position: Int?)
    fun onItemDeleteAtPos(position: Int, productList:ArrayList<MetaDataBeanItem>)
}