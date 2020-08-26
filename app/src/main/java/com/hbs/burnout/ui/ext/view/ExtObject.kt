package com.hbs.burnout.ui.ext.view

import android.graphics.Bitmap
import android.graphics.Matrix

fun Bitmap.rotate(degrees: Float): Bitmap {
    val matrix = Matrix().apply { postRotate(degrees) }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}