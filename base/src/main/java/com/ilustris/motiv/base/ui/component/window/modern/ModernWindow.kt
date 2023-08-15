@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.ilustris.motiv.base.ui.component.window.modern

import android.graphics.Bitmap
import androidx.compose.animation.core.EaseInElastic
import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilustris.motiv.base.data.model.Quote
import com.ilustris.motiv.base.data.model.QuoteDataModel
import com.ilustris.motiv.base.data.model.Style
import com.ilustris.motiv.base.data.model.User
import com.ilustris.motiv.base.ui.component.AnimatedText
import com.ilustris.motiv.base.ui.component.window.StyleCardAction
import com.ilustris.motiv.base.utils.FontUtils
import com.ilustris.motiv.base.utils.buildFont
import com.ilustris.motiv.base.utils.buildStyleShadow
import com.ilustris.motiv.base.utils.getFontStyle
import com.ilustris.motiv.base.utils.getTextAlign
import com.ilustris.motiv.base.utils.getTextColor
import com.ilustris.motiv.base.utils.getWindowGradient
import com.ilustris.motiv.foundation.ui.theme.colorsFromPalette
import com.ilustris.motiv.foundation.ui.theme.defaultRadius
import com.ilustris.motiv.foundation.ui.theme.gradientFill
import com.ilustris.motiv.foundation.ui.theme.isPreviewMode
import com.ilustris.motiv.foundation.ui.theme.motivBrushes
import com.ilustris.motiv.foundation.ui.theme.motivGradient
import com.ilustris.motiv.foundation.ui.theme.paletteFromBitMap
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.glide.GlideImageState
import com.skydoves.landscapist.glide.GlideRequestType

@Composable
fun ModernWindow(
    quoteDataModel: QuoteDataModel,
    animationEnabled: Boolean,
    loadAsGif: Boolean,
    styleCardAction: StyleCardAction? = null,
) {

    val quoteModel = quoteDataModel.quoteBean
    val style = quoteDataModel.style
    val user = quoteDataModel.user
    val backgroundBorderSize = 1.dp
    val cardShape = RoundedCornerShape(defaultRadius)

    var backgroundBitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }

    var imageLoaded = backgroundBitmap != null

    var animationCompleted by remember {
        mutableStateOf(false)
    }

    val colorsPalette = backgroundBitmap?.paletteFromBitMap()?.colorsFromPalette()

    val brush = getWindowGradient(colorsPalette)

    val imageBlur by animateDpAsState(
        targetValue = if (imageLoaded) 0.dp else 50.dp,
        tween(2500, delayMillis = 500, easing = EaseInElastic), label = "blur"
    )

    val imageAlpha by animateFloatAsState(
        targetValue = if (imageLoaded && animationCompleted) 1f else 0f,
        tween(1500), label = "alpha"
    )

    LaunchedEffect(Unit) {
        if (!animationEnabled) {
            animationCompleted = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(backgroundBorderSize, brush, cardShape)
            .background(MaterialTheme.colorScheme.surface, cardShape)
    ) {
        ModernWindowTitle(userName = user?.name, textColor = style.getTextColor(), brush = brush)
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .gradientFill(brush), thickness = 1.dp
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(cardShape)
        ) {
            if (!isPreviewMode()) {
                GlideImage(
                    imageModel = { style.backgroundURL },
                    glideRequestType = if (!loadAsGif) GlideRequestType.BITMAP else GlideRequestType.GIF,
                    modifier = Modifier
                        .matchParentSize()
                        .alpha(imageAlpha)
                        .blur(imageBlur)
                        .graphicsLayer(scaleY = 1.2f, scaleX = 1.2f)
                        .clip(cardShape),
                    onImageStateChanged = {
                        imageLoaded = it is GlideImageState.Success
                        if (it is GlideImageState.Success && backgroundBitmap == null) {
                            backgroundBitmap = it.imageBitmap?.asAndroidBitmap()
                        }
                    },
                )
            } else {
                Box(
                    Modifier
                        .background(motivGradient())
                        .matchParentSize()
                        .graphicsLayer(scaleX = 1.2f, scaleY = 1.2f)
                        .alpha(imageAlpha)
                        .blur(imageBlur)
                        .clip(cardShape)
                )
            }
            ModernTexts(
                quote = quoteModel,
                style = style,
                styleCardAction = styleCardAction,
                animationCompleted = animationCompleted,
                animationEnabled = animationEnabled,
                onCompleteAnimation = {
                    animationCompleted = it
                }
            )
        }

    }

}

@Composable
fun ModernTexts(
    quote: Quote, style: Style,
    animationCompleted: Boolean,
    animationEnabled: Boolean,
    onCompleteAnimation: (Boolean) -> Unit,
    styleCardAction: StyleCardAction? = null
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
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
        val textStyle = MaterialTheme.typography.headlineMedium.copy(
            shadow = shadowStyle,
            color = textColor,
            textAlign = textAlign,
            fontFamily = defaultFont,
            fontStyle = fontStyle
        )
        val authorTextStyle = MaterialTheme.typography.labelMedium.copy(
            shadow = shadowStyle,
            color = textColor,
            textAlign = textAlign,
            fontFamily = defaultFont,
            fontStyle = fontStyle
        )

        if (styleCardAction == null) {
            AnimatedText(
                text = quote.quote,
                animationEnabled = animationEnabled,
                animationOption = style.animationProperties?.animation,
                transitionMethod = style.animationProperties?.transition,
                textStyle = textStyle,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                onCompleteAnimation(true)
            }

            Text(
                text = quote.author, modifier = Modifier
                    .padding(16.dp)
                    .graphicsLayer(alpha = alphaAnimation.value),
                textAlign = TextAlign.Center,
                style = authorTextStyle,
                fontStyle = FontStyle.Italic,
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
fun ModernWindowTitle(userName: String? = "Motiv", textColor: Color, brush: Brush) {

    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .gradientFill(brush)
        ) {
            motivBrushes().forEach {
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(12.dp)
                        .background(it, CircleShape)
                        .border(
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                            width = 1.dp,
                            shape = CircleShape
                        )
                )
            }
        }

        val fontFamily = FontUtils.getFontFamily("Lato")
        val title = "${userName}.app"

        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            fontFamily = fontFamily,
            color = textColor
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.graphicsLayer(alpha = 0f)
        ) {
            motivBrushes().forEach {
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(12.dp)
                        .background(it, CircleShape)
                        .border(
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                            width = 1.dp,
                            shape = CircleShape
                        )
                )
            }
        }
    }
}

@Preview
@Composable
fun ModernWindowPreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        ModernWindow(
            quoteDataModel = QuoteDataModel(
                Quote("Modern window", author = "Motiv"),
                style = Style(textColor = "#c98cff"),
                user = User(name = "Motiv")
            ),
            animationEnabled = false,
            loadAsGif = false,
        )
    }
}