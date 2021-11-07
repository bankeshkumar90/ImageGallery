package com.nowfloats.packrat.addjobs

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

)