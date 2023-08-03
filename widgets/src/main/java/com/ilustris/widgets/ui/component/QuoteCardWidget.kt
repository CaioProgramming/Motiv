package com.ilustris.widgets.ui.component

import ai.atick.material.MaterialColor
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.action.clickable
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.wrapContentSize
import androidx.glance.text.FontFamily
import androidx.glance.text.FontStyle
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import com.ilustris.motiv.base.data.model.DEFAULT_FONT_FAMILY
import com.ilustris.motiv.base.data.model.QuoteDataModel
import com.ilustris.motiv.base.data.model.Style
import com.ilustris.motiv.base.data.model.TextAlignment
import com.ilustris.motiv.base.utils.FontUtils
import com.ilustris.motiv.base.utils.INITIALACT
import com.ilustris.motiv.base.utils.buildTextColor

@Composable
fun QuoteCardWidget(quoteDataModel: QuoteDataModel) {

    val context = LocalContext.current
    val font = quoteDataModel.style?.buildWidgetFont(context)
    val backColor = quoteDataModel.style?.styleProperties?.backgroundColor.buildWidgetBackColor()
    val textColor = quoteDataModel.style?.textColor.buildTextColor()
    val textAlign = quoteDataModel.style.buildWidgetAlign()
    val quoteTextStyle = TextStyle(
        color = androidx.glance.unit.ColorProvider(textColor),
        fontFamily = font,
        fontSize = 20.sp,
        textAlign = textAlign
    )

    val authorTextStyle = TextStyle(
        color = androidx.glance.unit.ColorProvider(textColor),
        fontFamily = font,
        fontStyle = FontStyle.Italic,
        fontSize = 16.sp,
        textAlign = textAlign
    )


    Column(
        modifier = GlanceModifier.background(backColor).padding(16.dp).wrapContentSize()
            .clickable {
                context.startActivity(Intent(context, Class.forName(INITIALACT)).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                })
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = quoteDataModel.quoteBean.quote,
            style = quoteTextStyle,
            modifier = GlanceModifier.fillMaxWidth().padding(8.dp)

        )
        Text(
            text = quoteDataModel.quoteBean.author,
            style = authorTextStyle,
            modifier = GlanceModifier.fillMaxWidth().padding(4.dp)
        )
    }

}

@Composable
fun String?.buildWidgetBackColor() = if (this == null) {
    MaterialColor.Gray400
} else {
    Color(android.graphics.Color.parseColor(this))
}

fun Style?.buildWidgetFont(context: Context): FontFamily {
    return if (this == null) FontFamily(DEFAULT_FONT_FAMILY) else {
        if (textProperties != null) {
            FontFamily(textProperties!!.fontFamily)
        } else {
            val family = FontUtils.getFamily(context, font)
            return FontFamily(family)
        }
    }
}

fun Style?.buildWidgetAlign() = if (this == null) {
    TextAlign.Center
} else {
    textProperties?.let {
        it.textAlignment.getWidgetTextAlign()
    } ?: textAlignment.getWidgetTextAlign()
}

fun TextAlignment?.getWidgetTextAlign(): TextAlign {
    return if (this == null) {
        TextAlign.Center
    } else {
        return when (this) {
            TextAlignment.JUSTIFY -> TextAlign.Start
            TextAlignment.CENTER -> TextAlign.Center
            TextAlignment.START -> TextAlign.Start
            TextAlignment.END -> TextAlign.End
            else -> TextAlign.Center
        }
    }


}


