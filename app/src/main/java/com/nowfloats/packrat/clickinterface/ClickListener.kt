package com.nowfloats.packrat.clickinterface


// gives the adapter position on click
interface ClickListener {
    fun onClick(position: Int)
    fun onClickDelete(position: Int?)
}