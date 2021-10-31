package com.nowfloats.packrat.bottomsheetdialog

import androidx.annotation.DrawableRes

public class Item(icPreview24dp: Int, s: String) {
    private var mDrawableRes = 0

    private var mTitle: String? = null

    fun Item(@DrawableRes drawable: Int, title: String?) {
        mDrawableRes = drawable
        mTitle = title
    }

    fun getDrawableResource(): Int {
        return mDrawableRes
    }

    fun getTitle(): String? {
        return mTitle
    }
}