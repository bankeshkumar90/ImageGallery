package com.nowfloats.packrat.clickInterface

import com.nowfloats.packrat.addjobs.childobjects.PropertyAdapter
import com.nowfloats.packrat.addjobs.metaDataBeanItem

interface ChildItemActionListener {
    fun onClickCross(position: Int?, holder: PropertyAdapter.ViewHolder, propertyList: ArrayList<metaDataBeanItem>)
}