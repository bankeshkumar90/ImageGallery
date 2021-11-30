package com.nowfloats.packrat.utils

import id.zelory.compressor.constraint.Constraint
import java.io.File

class MyLowerCaseNameConstraint: Constraint {
    override fun isSatisfied(imageFile: File): Boolean {
        return imageFile.name.all { it.isLowerCase() }
    }

    override fun satisfy(imageFile: File): File {
        val destination = File(imageFile.parent, imageFile.name.toLowerCase())
        imageFile.renameTo(destination)
        return destination
    }
}
