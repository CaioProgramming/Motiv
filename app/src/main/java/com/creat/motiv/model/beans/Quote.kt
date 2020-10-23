package com.creat.motiv.model.beans

import android.graphics.Color
import java.util.*
import kotlin.collections.ArrayList

data class Quote(
        var key: String? = null,
        var quote: String = "",
        var author: String = "",
        val data: Date = Date(),
        val userID: String = "",
        var backgroundcolor: String = "#ffffff",
        var textcolor: String = "#000000",
        var isReport: Boolean = false,
        var likes: ArrayList<String> = ArrayList(),
        var font: Int = 0) : BaseBean(key ?: "") {

    fun intTextColor(): Int = Color.parseColor(textcolor)
    fun intBackColor(): Int = Color.parseColor(backgroundcolor)
}
