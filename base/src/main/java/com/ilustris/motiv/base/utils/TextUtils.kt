package com.ilustris.motiv.base.utils

import android.content.Context
import android.graphics.Typeface
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


}