package com.nowfloats.packrat.addjobs

data class metaDataTabObj (
    var metaDataBeanList: ArrayList<metaDataBeanItem> = ArrayList<metaDataBeanItem>(),
    var collectionId:String = ""
)