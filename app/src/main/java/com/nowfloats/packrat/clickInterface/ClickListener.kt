package com.nowfloats.packrat.clickInterface


// gives the adapter position on click
interface ClickListener {
    fun onClick(position: Int)
    fun onClickDelete(position: Int?)
}