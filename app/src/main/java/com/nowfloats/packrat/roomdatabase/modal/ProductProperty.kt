package com.nowfloats.packrat.roomdatabase.modal

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nowfloats.packrat.network.RegexApiResponse
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "properties")
data class ProductProperty(
    var properies: List<RegexApiResponse>?
){
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int = 1
}