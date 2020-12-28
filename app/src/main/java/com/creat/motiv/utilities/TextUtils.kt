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

            dayCount <= 90 -> "Há ${dayCount / 30} meses"

            else -> {
                fmt.format(qDate)
            }
        }
    }


    fun fonts(): List<FontObject> {
        return listOf(
                FontObject("Arvo", "fonts/Arvo-Regular_201.ttf"),
                FontObject("Audrey", "fonts/Audrey-Normal.otf"),
                FontObject("Cornerstone", "fonts/Cornerstone.ttf"),
                FontObject("Times", "fonts/times.ttf"),
                FontObject("Mightype", "fonts/MightypeScript.otf"),
                FontObject("Amatic", "fonts/AmaticSC-Regular.ttf"),
                FontObject("BlackHan", "fonts/BlackHanSans-Regular.ttf"),
                FontObject("Cabin", "fonts/Cabin-Regular.ttf"),
                FontObject("Cinzel regular", "fonts/Cinzel-Regular.ttf"),
                FontObject("Farsan", "fonts/Farsan-Regular.ttf"),
                FontObject("FingerPaint", "fonts/FingerPaint-Regular.ttf"),
                FontObject("Fredoka One", "fonts/FredokaOne-Regular.ttf"),
                FontObject("Incosolata", "fonts/Inconsolata-Regular.ttf"),
                FontObject("Lalezar", "fonts/Lalezar-Regular.ttf"),
                FontObject("Lobster", "fonts/Lobster-Regular.ttf"),
                FontObject("Mogra", "fonts/Mogra-Regular.ttf"),
                FontObject("Nunito", "fonts/Nunito-Regular.ttf"),
                FontObject("Pacifico", "fonts/Pacifico-Regular.ttf"),
                FontObject("Quicksand", "fonts/Quicksand-Regular.ttf"),
                FontObject("Rakkas", "fonts/Rakkas-Regular.ttf"),
                FontObject("Ranga", "fonts/Ranga-Regular.ttf"),
                FontObject("Rasa", "fonts/Rasa-Regular.ttf")
        )
    }

    fun getTypeFace(context: Context, path: String): Typeface {
        return Typeface.createFromAsset(context.assets, path)
    }

}