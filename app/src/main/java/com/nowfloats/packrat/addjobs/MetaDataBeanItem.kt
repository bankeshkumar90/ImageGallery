package com.nowfloats.packrat.addjobs

import android.os.Parcel
import android.os.Parcelable

data class MetaDataBeanItem(
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

    companion object CREATOR : Parcelable.Creator<MetaDataBeanItem> {
        override fun createFromParcel(parcel: Parcel): MetaDataBeanItem {
            return MetaDataBeanItem(parcel)
        }

        override fun newArray(size: Int): Array<MetaDataBeanItem?> {
            return arrayOfNulls(size)
        }
    }
}