package com.ilustris.motiv.foundation.utils

import android.content.Context
import android.graphics.Color

object ColorUtils {

    fun toHex(intColor: Int): String {
        return java.lang.String.format("#%06X", 0xFFFFFF and intColor)
    }

    fun getMaterialColors(context: Context): List<String> {
        val fields = Class.forName("com.github.mcginty" + ".R\$color").declaredFields
        return fields.filter { it.getInt(null) != Color.TRANSPARENT }.map {
            val colorId = it.getInt(null)
            val color = context.resources.getColor(colorId)
            toHex(color)
        }

    }

}