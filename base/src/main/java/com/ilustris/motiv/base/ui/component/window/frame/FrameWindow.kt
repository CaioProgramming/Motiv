@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)

package com.ilustris.motiv.base.ui.component.window.frame

import android.graphics.Bitmap
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilustris.motiv.base.data.model.Quote
import com.ilustris.motiv.base.data.model.QuoteDataModel
import com.ilustris.motiv.base.data.model.Style
import com.ilustris.motiv.base.data.model.StyleProperties
import com.ilustris.motiv.base.data.model.TextAlignment
import com.ilustris.motiv.base.data.model.TextProperties
import com.ilustris.motiv.base.data.model.User
import com.ilustris.motiv.base.data.model.Window
import com.ilustris.motiv.base.ui.component.AnimatedText
import com.ilustris.motiv.base.ui.component.window.StyleCardAction
import com.ilustris.motiv.base.utils.buildBackColor
import com.ilustris.motiv.base.utils.buildFont
import com.ilustris.motiv.base.utils.buildStyleShadow
import com.ilustris.motiv.base.utils.cardShape
import com.ilustris.motiv.base.utils.getFontStyle
import com.ilustris.motiv.base.utils.getTextAlign
import com.ilustris.motiv.base.utils.getTextColor
import com.ilustris.motiv.base.utils.getWindowGradient
import com.ilustris.motiv.foundation.ui.component.CardBackground
import com.ilustris.motiv.foundation.ui.theme.colorsFromPalette
import com.ilustris.motiv.foundation.ui.theme.gradientFill
import com.ilustris.motiv.foundation.ui.theme.motivBrushes
import com.ilustris.motiv.foundation.ui.theme.paletteFromBitMap

@Composable
fun FrameWindow(
    quoteDataModel: QuoteDataModel,
    animationEnabled: Boolean,
    loadAsGif: Boolean,
    styleCardAction: StyleCardAction? = null,
) {

    val style = quoteDataModel.style
    val user = quoteDataModel.user
    val quote = quoteDataModel.quoteBean

    val shape = style.cardShape()
    var backgroundBitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }
    var animationCompleted by remember {
        mutableStateOf(false)
    }
    val colorsPalette = backgroundBitmap?.paletteFromBitMap()?.colorsFromPalette()

    val brush = getWindowGradient(colorsPalette)
    val backgroundBorderSize = 1.dp
    val backgroundColor = style.styleProperties?.backgroundColor.buildBackColor()

    LaunchedEffect(Unit) {
        if (!animationEnabled) {
            animationCompleted = true
        }
    }
    Column(
        modifier = Modifier
            .background(backgroundColor.copy(alpha = 0.3f), shape)
            .border(
                width = backgroundBorderSize,
                brush = brush,
                shape = shape
            )
            .clip(shape)
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        FrameTitle(userName = user?.name ?: "", brush = brush)


        CardBackground(
            loadAsGif = loadAsGif,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.6f)
                .padding(4.dp)
                .background(backgroundColor.copy(alpha = 0.3f), shape)
                .clip(shape),
            backgroundImage = style.backgroundURL
        ) {
            if (backgroundBitmap == null) {
                backgroundBitmap = it.asAndroidBitmap()
            }
        }

        FrameText(
            style = style,
            quote = quote,
            styleCardAction = styleCardAction,
            animationEnabled = animationEnabled,
            animationCompleted = animationCompleted,
            onCompleteAnimation = {
                animationCompleted = it
            }
        )


    }

}

@Composable
fun FrameText(
    style: Style,
    quote: Quote,
    animationEnabled: Boolean,
    animationCompleted: Boolean,
    onCompleteAnimation: (Boolean) -> Unit,
    styleCardAction: StyleCardAction? = null
) {
    Column(
        modifier = Modifier
            .padding(vertical = 16.dp, horizontal = 8.dp)
            .wrapContentSize()
    ) {
        val context = LocalContext.current

        val alphaAnimation = animateFloatAsState(
            targetValue = if (animationCompleted || !animationEnabled) 1f else 0f,
            tween(500),
            label = "Author Alpha"
        )

        val shadowStyle = style.buildStyleShadow()
        val textAlign = style.getTextAlign()
        val textColor = style.getTextColor()

        val defaultFont = style.buildFont(context)
        val fontStyle = style.getFontStyle()
        val textStyle = MaterialTheme.typography.headlineSmall.copy(
            shadow = shadowStyle,
            color = textColor,
            textAlign = textAlign,
            fontFamily = defaultFont,
            fontStyle = fontStyle,
            fontWeight = FontWeight.SemiBold
        )
        val authorTextStyle = MaterialTheme.typography.labelMedium.copy(
            shadow = shadowStyle,
            color = textColor,
            textAlign = textAlign,
            fontFamily = defaultFont,
            fontStyle = fontStyle,
            fontWeight = FontWeight.W300
        )

        if (styleCardAction == null) {

            AnimatedText(
                text = quote.quote,
                animationEnabled = animationEnabled,
                animationOption = style.animationProperties?.animation,
                transitionMethod = style.animationProperties?.transition,
                textStyle = textStyle,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
            ) {
                onCompleteAnimation(true)
            }

            Text(
                text = quote.author,
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer(alpha = alphaAnimation.value),
                textAlign = style.getTextAlign(),
                style = authorTextStyle,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Light,
                softWrap = true
            )
        } else {
            TextField(
                value = quote.quote, onValueChange = {
                    styleCardAction.updateQuoteText(it)
                },
                textStyle = textStyle,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                ),
                placeholder = {
                    Text(text = "Digite sua frase", style = textStyle)
                },
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
            )


            TextField(
                value = quote.author,
                onValueChange = {
                    styleCardAction.updateQuoteAuthor(it)
                },
                placeholder = {
                    Text(text = "Autor", style = authorTextStyle)
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer(alpha = alphaAnimation.value),
                textStyle = authorTextStyle.copy(
                    textAlign = style.getTextAlign(),
                    fontWeight = FontWeight.Light,
                    fontStyle = FontStyle.Italic
                ),
            )
        }

    }
}

@Composable
fun FrameTitle(userName: String, brush: Brush) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .gradientFill(brush)
    ) {
        repeat(motivBrushes().size) {
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .size(12.dp)
                    .background(MaterialTheme.colorScheme.surface)
            )
        }
        Text(
            text = userName,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Light
        )
    }
}

@Preview()
@Composable
fun FramePreview() {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        item {
            FrameWindow(
                quoteDataModel = QuoteDataModel(
                    Quote("testQuote", author = "Motiv"),
                    user = User("Ilustris"),
                    style = Style(
                        styleProperties = StyleProperties(
                            backgroundColor = "#ccacfc",
                            customWindow = Window.FRAME
                        ),
                        textProperties = TextProperties(
                            fontFamily = "Lato",
                            fontStyle = com.ilustris.motiv.base.data.model.FontStyle.BOLD,
                            textAlignment = TextAlignment.START
                        )
                    )
                ), animationEnabled = false, loadAsGif = false
            )
        }
        item {
            FrameWindow(
                quoteDataModel = QuoteDataModel(
                    Quote("testQuote", author = "Motiv"),
                    user = User("Ilustris"),
                    style = Style(
                        styleProperties = StyleProperties(
                            backgroundColor = "#ccacfc",
                            customWindow = Window.FRAME
                        ),
                        textProperties = TextProperties(
                            fontFamily = "Lato",
                            fontStyle = com.ilustris.motiv.base.data.model.FontStyle.BOLD,
                            textAlignment = TextAlignment.START
                        )
                    )
                ),
                animationEnabled = false,
                loadAsGif = false,
                styleCardAction = object : StyleCardAction {
                    override fun updateQuoteText(newText: String) {

                    }

                    override fun updateQuoteAuthor(newAuthor: String) {
                    }

                }
            )
        }
    }

}