package com.ilustris.motiv.base.utils

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ilustris.motiv.base.data.model.Quote
import com.ilustris.motiv.base.data.model.QuoteDataModel
import com.ilustris.motiv.base.data.model.Style
import com.ilustris.motiv.base.data.model.StyleProperties
import com.ilustris.motiv.base.data.model.TextProperties
import com.ilustris.motiv.base.data.model.User
import com.ilustris.motiv.base.data.model.Window
import com.ilustris.motiv.base.ui.component.window.StyleCardAction
import com.ilustris.motiv.base.ui.component.window.classic.ClassicWindow
import com.ilustris.motiv.base.ui.component.window.frame.FrameWindow
import com.ilustris.motiv.base.ui.component.window.modern.ModernWindow
import com.ilustris.motiv.foundation.ui.theme.MotivTheme
import com.ilustris.motiv.foundation.ui.theme.defaultRadius
import com.ilustris.motiv.foundation.ui.theme.gradientAnimation
import com.ilustris.motiv.foundation.ui.theme.motivBrushes


@Composable
fun getWindowGradient(colors: List<Color>? = null) = gradientAnimation(colors ?: motivBrushes())

@Composable
fun Window.cardBackgroundModifier(window: Window) {
    val backgroundShape = RoundedCornerShape(backgroundBorderSize())

}


fun Window.backgroundBorderSize(): Dp {
    return when (this) {
        Window.CLASSIC -> 5.dp
        Window.MODERN -> 2.dp
        Window.FRAME -> 1.dp
    }
}

fun Window.contentBorderSize(): Dp {
    return when (this) {
        Window.CLASSIC -> 3.dp
        Window.MODERN -> 1.dp
        Window.FRAME -> 3.dp
    }
}

fun Style?.cardShape(): RoundedCornerShape {
    val radius = this?.styleProperties?.customWindow?.let {
        when (it) {
            Window.CLASSIC -> 3.dp
            Window.MODERN -> 10.dp
            Window.FRAME -> 5.dp
        }
    } ?: defaultRadius
    return RoundedCornerShape(radius)
}

fun Modifier.borderForWindow(window: Window?, brush: Brush): Modifier = composed {

    val shapeForWindows = RoundedCornerShape(defaultRadius)

    when (window) {
        Window.CLASSIC -> border(
            width = 2.dp,
            brush = brush,
            shape = shapeForWindows
        )

        Window.MODERN -> border(
            width = 1.dp,
            brush = brush,
            shape = shapeForWindows
        )

        else -> border(
            width = 1.dp,
            brush = brush,
            shape = shapeForWindows
        )
    }
}


@Composable
fun BuildQuoteWindow(
    quoteDataModel: QuoteDataModel,
    loadAsGif: Boolean = false,
    action: StyleCardAction? = null,
    animationEnabled: Boolean
) {

    when (quoteDataModel.style.styleProperties?.customWindow ?: Window.MODERN) {
        Window.FRAME -> FrameWindow(
            quoteDataModel = quoteDataModel,
            animationEnabled = animationEnabled,
            loadAsGif = loadAsGif,
            styleCardAction = action
        )

        Window.CLASSIC -> ClassicWindow(
            quoteDataModel = quoteDataModel,
            animationEnabled = animationEnabled,
            loadAsGif = loadAsGif,
            styleCardAction = action
        )

        else -> ModernWindow(
            quoteDataModel = quoteDataModel,
            animationEnabled = animationEnabled,
            loadAsGif = loadAsGif,
            styleCardAction = action
        )
    }
}


@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewWindow() {
    MotivTheme {
        LazyColumn(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(), verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            val windowTypes = Window.values().reversed()
            items(windowTypes) {
                val quoteDataModel = QuoteDataModel(
                    Quote("testing quote"),
                    user = User(name = "Ilustris"),
                    style = Style(
                        styleProperties = StyleProperties(
                            customWindow = it,
                            backgroundColor = "#ccacfc"
                        ),
                        textProperties = TextProperties(textColor = "#8634eb")
                    ),
                )
                Column(
                    modifier = Modifier
                        .height(300.dp)
                        .fillMaxWidth()
                ) {
                    BuildQuoteWindow(quoteDataModel = quoteDataModel, animationEnabled = false)
                }

            }
        }
    }

}

