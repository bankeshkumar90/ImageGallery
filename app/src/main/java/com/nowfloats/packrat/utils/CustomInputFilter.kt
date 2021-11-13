package com.nowfloats.packrat.utils

import android.text.InputFilter
import android.text.Spanned
import java.util.regex.Pattern

class CustomInputFilter(var patternText:String) : InputFilter {
    // private val regex = Pattern.compile("^[A-Z0-9]*$")
    private val regex = Pattern.compile(patternText)

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        val matcher = regex.matcher(source)
        return if (matcher.find()) {
            null
        } else {
            ""
        }
    }
}