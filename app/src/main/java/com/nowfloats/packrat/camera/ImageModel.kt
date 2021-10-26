package com.nowfloats.packrat.camera

data class ImageModel(
    var image: String = "", var title: String = "", var resImg: Int = 0,
    var isSelected: Boolean = false
) {
    override fun toString(): String {
        return " image : $image, title : $title, resImg : $resImg, isSelected : $isSelected"
    }
}

