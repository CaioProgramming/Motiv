package com.ilustris.motiv.base.utils

import android.content.Context
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.ilustris.motiv.base.data.model.DEFAULT_FONT_FAMILY
import com.ilustris.motiv.base.data.model.Style
import com.ilustris.motiv.foundation.utils.getFontStyle
import com.ilustris.motiv.foundation.utils.getFontWeight
import com.ilustris.motiv.foundation.utils.getTextAlign

fun Style?.buildStyleShadow() = if (this != null && shadowStyle != null) {
    shadowStyle!!.buildShadow()
} else {
    Shadow()
}

@Composable
fun Style?.getTextColor() = if (this == null) {
    MaterialTheme.colorScheme.onBackground
} else {
    textProperties?.let { textColor.buildTextColor() } ?: textColor.buildTextColor()
}

fun Style?.getTextAlign() = if (this == null) {
    TextAlign.Center
} else {
    textProperties?.let {
        it.textAlignment.getTextAlign()
    } ?: textAlignment.getTextAlign()
}

@Composable
fun String?.buildTextColor() = if (this == null) {
    MaterialTheme.colorScheme.onBackground
} else {
    Color(android.graphics.Color.parseColor(this))
}

@Composable
fun String?.buildBackColor() = if (this == null) {
    MaterialTheme.colorScheme.surface
} else {
    Color(android.graphics.Color.parseColor(this))
}


fun Style?.buildFont(context: Context): FontFamily {
    return if (this == null) FontUtils.getFontFamily(DEFAULT_FONT_FAMILY) else {
        if (textProperties != null) {
            FontUtils.getFontFamily(textProperties!!.fontFamily)
        } else {
            FontUtils.getFont(
                context,
                font
            )
        }
    }
}


fun Style?.getFontStyle() = if (this == null) androidx.compose.ui.text.font.FontStyle.Normal else {
    textProperties?.let { it.fontStyle.getFontStyle() } ?: fontStyle.getFontStyle()
}

fun Style?.getFontWeight() = if (this == null) FontWeight.Normal else {
    textProperties?.let { it.fontStyle.getFontWeight() } ?: fontStyle.getFontWeight()
}


