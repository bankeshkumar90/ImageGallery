package com.nowfloats.packrat.addjobs

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.nowfloats.packrat.roomdatabase.ProductTypeConverter


data class metaDataTabObj (
     var collectionId: Int,
     var metaDataBeanList: ArrayList<metaDataBeanItem> = ArrayList<metaDataBeanItem>()
)