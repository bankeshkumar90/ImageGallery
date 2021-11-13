package com.nowfloats.packrat.roomdatabase.modal

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nowfloats.packrat.network.RegexApiResponse
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductProperty(
    var properies: List<RegexApiResponse>?
): Parcelable