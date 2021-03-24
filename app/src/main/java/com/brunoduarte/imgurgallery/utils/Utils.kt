package com.brunoduarte.imgurgallery.utils

import android.content.Context
import android.util.DisplayMetrics




class Utils {
    companion object {
        fun convertPixelsToDp(px: Float, context: Context): Float {
            return px / (context.resources
                .displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
        }
    }
}