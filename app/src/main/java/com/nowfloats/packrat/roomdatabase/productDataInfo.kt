package com.nowfloats.packrat.roomdatabase

import androidx.room.*
import com.nowfloats.packrat.addjobs.MetaDataBeanItem

@Entity(tableName = "productInfo")
data class productDataInfo(
    @ColumnInfo(name = "CollectionId") var collectionId: String,
    @ColumnInfo(name = "metaDataList") var metaDataList : List<MetaDataBeanItem>
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null
}