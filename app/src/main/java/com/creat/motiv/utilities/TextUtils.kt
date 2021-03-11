package com.creat.motiv.utilities

import android.content.Context
import android.graphics.Typeface
import com.creat.motiv.model.beans.FontObject
import java.text.SimpleDateFormat
import java.util.*

object TextUtils {
    fun data(qDate: Date): String {

        val now = Calendar.getInstance().time
        val fmt = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale.getDefault())
        val dayCount = ((now.time - qDate.time) / 1000 / 60 / 60 / 24).toInt()
        return when {
            dayCount < 1 -> {
                "Hoje"
            }
            dayCount == 1 -> {
                "Ontem"
            }
            dayCount <= 6 -> "Há ${dayCount} dias"

            dayCount == 7 -> "Há 1 semana"

            dayCount <= 28 -> "Há ${dayCount / 7} semanas"

            dayCount == 30 -> "Há 1 mês"

            dayCount <= 90 -> "Há ${dayCount / 30} ${if (dayCount / 30 < 2) "mês" else "meses"}"

            else -> {
                fmt.format(qDate)
            }
        }
    }


    fun fonts(): List<FontObject> {
        return listOf(
                FontObject("Amatic", "fonts/AmaticSC-Bold.ttf"),
                FontObject("Bangers", "fonts/Bangers-Regular.ttf"),
                FontObject("Bigelow", "fonts/BigelowRules-Regular.ttf"),
                FontObject("Bungee Shade", "fonts/BungeeShade-Regular.ttf"),
                FontObject("Caveat", "fonts/Caveat-Medium.ttf"),
                FontObject("Cormorant", "fonts/CormorantUnicase-Regular.ttf"),
                FontObject("Cutive Mono", "fonts/CutiveMono-Regular.ttf"),
                FontObject("Dot Gothic", "fonts/DotGothic16-Regular.ttf"),
                FontObject("Indie Flower", "fonts/IndieFlower-Regular.ttf"),
                FontObject("Lexend Zetta", "fonts/LexendZetta-Regular.ttf"),
                FontObject("Libre Barcode", "fonts/LibreBarcode39Text-Regular.ttf"),
                FontObject("Lalezar", "fonts/Oswald-Bold.ttf"),
                FontObject("Redressed", "fonts/Redressed-Regular.ttf"),
                FontObject("Righteous", "fonts/Righteous-Regular.ttf"),
                FontObject("Sacramento", "fonts/Sacramento-Regular.ttf"),
                FontObject("Pacifico", "fonts/Pacifico-Regular.ttf"),
                FontObject("Satisfy", "fonts/Satisfy-Regular.ttf")
        )
    }

    fun getTypeFace(context: Context, position: Int): Typeface {
        return loadTypeFace(context, fonts()[position].path)
    }

    private fun loadTypeFace(context: Context, path: String): Typeface {
        return Typeface.createFromAsset(context.assets, path)
    }

}