package com.nowfloats.packrat.addjobs

data class metaDataBeanItem(
    var id: String ="",
    var itemName: String ="",
    var itemValue: String ="",
    var itemVisible: Boolean = false,
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
    var weigthValue: String =""
)