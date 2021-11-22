package com.nowfloats.packrat.roomdatabase

import android.os.Parcel
import android.os.Parcelable
import androidx.room.*
import com.nowfloats.packrat.addjobs.metaDataBeanItem

@Entity(tableName = "productInfo")
data class productDataInfo(
    @ColumnInfo(name = "CollectionId") var collectionId: String,
    @ColumnInfo(name = "metaDataList") var metaDataList : List<metaDataBeanItem>
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null
}