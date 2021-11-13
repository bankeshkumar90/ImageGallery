package com.nowfloats.packrat.addjobs

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class metaDataBeanItem(
    var productName: String ="",
    var productValue: String ="",
    var productRegex: String = "",

): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!

    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(productName)
        parcel.writeString(productValue)
        parcel.writeString(productRegex)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<metaDataBeanItem> {
        override fun createFromParcel(parcel: Parcel): metaDataBeanItem {
            return metaDataBeanItem(parcel)
        }

        override fun newArray(size: Int): Array<metaDataBeanItem?> {
            return arrayOfNulls(size)
        }
    }
}