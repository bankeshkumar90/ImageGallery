package com.nowfloats.packrat.clickinterface

import com.nowfloats.packrat.addjobs.childobjects.PropertyAdapter
import com.nowfloats.packrat.addjobs.MetaDataBeanItem

interface ChildItemActionListener {
    fun onClickCross(position: Int?, holder: PropertyAdapter.ViewHolder, propertyList: ArrayList<MetaDataBeanItem>)
    fun onBeforeClickCross(position: Int?, holder: PropertyAdapter.ViewHolder, propertyList: ArrayList<MetaDataBeanItem>)

}