package com.ilustris.motiv.base.utils

import android.content.Context
import android.graphics.Color
import com.ilustris.motiv.base.R
import java.util.*
import kotlin.collections.ArrayList

object ColorUtils {
    val randomColor: Int
        get() {
            val rnd = Random()
            return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        }

    val ERROR = R.color.material_red500
    val SUCCESS = R.color.material_green500
    val WARNING = R.color.material_yellow500
    val INFO = R.color.material_blue500

    fun toHex(intColor: Int): String {
        return java.lang.String.format("#%06X", 0xFFFFFF and intColor)
    }

    fun getColors(context: Context): ArrayList<String> {
        val colors = ArrayList<String>()
        val fields = Class.forName("com.github.mcginty" + ".R\$color").declaredFields
        fields.forEach {
            if (it.getInt(null) != Color.TRANSPARENT) {
                val colorId = it.getInt(null)
                val color = context.resources.getColor(colorId)
                colors.add(toHex(color))
            }
        }
        return colors
    }

    fun lighten(color: Int, fraction: Double): Int {
        var red = Color.red(color)
        var green = Color.green(color)
        var blue = Color.blue(color)
        red = lightenColor(red, fraction)
        green = lightenColor(green, fraction)
        blue = lightenColor(blue, fraction)
        val alpha = Color.alpha(color)
        return Color.argb(alpha, red, green, blue)
    }

    fun darken(color: Int, fraction: Double): Int {
        var red = Color.red(color)
        var green = Color.green(color)
        var blue = Color.blue(color)
        red = darkenColor(red, fraction)
        green = darkenColor(green, fraction)
        blue = darkenColor(blue, fraction)
        val alpha = Color.alpha(color)

        return Color.argb(alpha, red, green, blue)
    }

    private fun darkenColor(color: Int, fraction: Double): Int {
        return Math.max(color - color * fraction, 0.0).toInt()
    }

    private fun lightenColor(color: Int, fraction: Double): Int {
        return Math.min(color + color * fraction, 255.0).toInt()
    }
}
