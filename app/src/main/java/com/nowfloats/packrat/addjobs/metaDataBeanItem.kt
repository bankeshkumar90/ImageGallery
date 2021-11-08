package com.nowfloats.packrat.addjobs

import android.os.Parcel
import android.os.Parcelable

data class metaDataBeanItem(
    var id: String ="",
    var productName: String ="",
    var productValue: String ="",
    var productVisible: Boolean = false,

    var barcode: String ="",
    var barcodeValue: String ="",
    var barcodeVisbile: Boolean = false,

    var price: String ="",
    var priceValue: String ="",
    var priceVisible: Boolean = false,

    var quantity: String ="",
    var quantityValue: String ="",
    var quantityVisible :Boolean = false,

    var othersName: String ="",
    var othersValue: String ="",
    var othersVisible : Boolean = false,

    var weigth: String ="",
    var weigthValue: String ="",
    var weightVisible : Boolean = false

): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readByte() != 0.toByte(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readByte() != 0.toByte(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readByte() != 0.toByte(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readByte() != 0.toByte(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readByte() != 0.toByte(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(productName)
        parcel.writeString(productValue)
        parcel.writeByte(if (productVisible) 1 else 0)
        parcel.writeString(barcode)
        parcel.writeString(barcodeValue)
        parcel.writeByte(if (barcodeVisbile) 1 else 0)
        parcel.writeString(price)
        parcel.writeString(priceValue)
        parcel.writeByte(if (priceVisible) 1 else 0)
        parcel.writeString(quantity)
        parcel.writeString(quantityValue)
        parcel.writeByte(if (quantityVisible) 1 else 0)
        parcel.writeString(othersName)
        parcel.writeString(othersValue)
        parcel.writeByte(if (othersVisible) 1 else 0)
        parcel.writeString(weigth)
        parcel.writeString(weigthValue)
        parcel.writeByte(if (weightVisible) 1 else 0)
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