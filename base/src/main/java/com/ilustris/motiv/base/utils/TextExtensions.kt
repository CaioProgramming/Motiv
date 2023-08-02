package com.ilustris.motiv.foundation.utils

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.ilustris.motiv.base.data.model.FontStyle
import com.ilustris.motiv.base.data.model.TextAlignment

fun TextAlignment?.getTextAlign(): TextAlign {
    if (this == null) return TextAlign.Center
    return when (this) {
        TextAlignment.JUSTIFY -> TextAlign.Justify
        TextAlignment.CENTER -> TextAlign.Center
        TextAlignment.START -> TextAlign.Start
        TextAlignment.END -> TextAlign.End
    }
}

fun FontStyle?.getFontStyle(): androidx.compose.ui.text.font.FontStyle {
    if (this == null) return androidx.compose.ui.text.font.FontStyle.Normal
    return when (this) {
        FontStyle.ITALIC -> androidx.compose.ui.text.font.FontStyle.Italic
        else -> androidx.compose.ui.text.font.FontStyle.Normal
    }
}

fun FontStyle?.getFontWeight(): FontWeight {
    if (this == null) return FontWeight.Normal
    return when (this) {
        FontStyle.ITALIC, FontStyle.REGULAR -> FontWeight.Normal
        FontStyle.BOLD -> FontWeight.Bold
        FontStyle.BLACK -> FontWeight.Black
    }
}
