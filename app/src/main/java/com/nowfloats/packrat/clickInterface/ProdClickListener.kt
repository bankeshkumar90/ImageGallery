package com.nowfloats.packrat.clickInterface

import android.widget.LinearLayout

interface ProdClickListener {
    fun onClickAdd(position: Int)
    fun onClickItemDelete(position: Int?)
}