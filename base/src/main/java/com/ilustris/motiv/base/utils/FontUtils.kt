package com.ilustris.motiv.foundation.utils

import android.content.Context
import android.util.Log
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import com.creat.motiv.base.R
import com.ilustris.motiv.foundation.ui.theme.fontProvider

object FontUtils {
    fun getFontFamily(fontName: String) = FontFamily(
        Font(
            googleFont = GoogleFont(fontName),
            fontProvider = fontProvider,
            weight = FontWeight.Normal
        ),
        Font(
            googleFont = GoogleFont(fontName),
            fontProvider = fontProvider,
            weight = FontWeight.Bold
        ),
        Font(
            googleFont = GoogleFont(fontName),
            fontProvider = fontProvider,
            style = FontStyle.Italic
        ),
        androidx.compose.ui.text.font.Font(R.font.pathway_variable)
    )

    fun getFont(context: Context, index: Int): FontFamily {
        val family = getFamily(context, index)
        return getFontFamily(family)
    }

    fun getFamily(context: Context, index: Int): String {
        val array = context.resources.getStringArray(R.array.family_names)
        return try {
            array[index]
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(javaClass.simpleName, "getFamily: Error getting font family ${e.message}")
            array.first()
        }
    }

    fun getFamilies(context: Context) = context.resources.getStringArray(R.array.family_names)
}
