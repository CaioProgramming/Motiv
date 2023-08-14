@file:OptIn(ExperimentalMaterial3Api::class)

package com.ilustris.motiv.base.ui.component.window.classic

import ai.atick.material.MaterialColor
import android.graphics.Bitmap
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.EaseInElastic
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
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
import androidx.compose.material.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.creat.motiv.base.R
import com.ilustris.motiv.base.data.model.Quote
import com.ilustris.motiv.base.data.model.QuoteDataModel
import com.ilustris.motiv.base.data.model.Style
import com.ilustris.motiv.base.ui.component.AnimatedText
import com.ilustris.motiv.base.ui.component.window.StyleCardAction
import com.ilustris.motiv.base.utils.AD_GIF
import com.ilustris.motiv.base.utils.FontUtils
import com.ilustris.motiv.base.utils.buildFont
import com.ilustris.motiv.base.utils.buildStyleShadow
import com.ilustris.motiv.base.utils.getFontStyle
import com.ilustris.motiv.base.utils.getTextAlign
import com.ilustris.motiv.base.utils.getTextColor
import com.ilustris.motiv.base.utils.getWindowGradient
import com.ilustris.motiv.foundation.ui.theme.colorsFromPalette
import com.ilustris.motiv.foundation.ui.theme.gradientFill
import com.ilustris.motiv.foundation.ui.theme.isPreviewMode
import com.ilustris.motiv.foundation.ui.theme.motivGradient
import com.ilustris.motiv.foundation.ui.theme.paletteFromBitMap
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.glide.GlideImageState
import com.skydoves.landscapist.glide.GlideRequestType

@Composable
fun ClassicTitle(
    userPic: String? = AD_GIF,
    userName: String,
    textColor: Color,
    brush: Brush
) {
    val fontFamily = FontUtils.getFontFamily("VT323")
    val title = "${userName}.exe"
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (!isPreviewMode()) {
                GlideImage(
                    imageModel = {
                        userPic
                    },
                    imageOptions = ImageOptions(
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center,
                    ),
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                            CircleShape
                        )
                        .clip(CircleShape)
                        .border(
                            1.dp,
                            color = MaterialTheme.colorScheme.onBackground,
                            CircleShape
                        )

                )
            } else {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.4f),
                            CircleShape
                        )
                        .border(1.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
                )
            }

            Text(
                text = title,
                color = textColor,
                style = MaterialTheme.typography.bodyLarge,
                fontFamily = fontFamily,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .graphicsLayer(alpha = 0.6f)
                .gradientFill(brush)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_pixel_heart),
                contentDescription = stringResource(id = R.string.app_name),
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(24.dp)
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_half_pixel_heart),
                contentDescription = stringResource(id = R.string.app_name),
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(24.dp)
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_pixel_outline_heart),
                contentDescription = stringResource(id = R.string.app_name),
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(24.dp)
            )
        }
    }

}


@Composable
fun ClassicWindow(
    quoteDataModel: QuoteDataModel,
    animationEnabled: Boolean,
    loadAsGif: Boolean,
    styleCardAction: StyleCardAction? = null,
) {

    val quoteModel = quoteDataModel.quoteBean
    val style = quoteDataModel.style
    val backgroundBorderSize = 5.dp
    val cardShape = RoundedCornerShape(3.dp)

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(cardShape)
            .border(
                1.dp,
                MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                cardShape
            )
            .border(
                backgroundBorderSize,
                brush,
                cardShape
            )
    ) {

        if (!isPreviewMode()) {
            GlideImage(
                imageModel = { style.backgroundURL },
                glideRequestType = if (loadAsGif) GlideRequestType.GIF else GlideRequestType.BITMAP,
                modifier = Modifier
                    .matchParentSize()
                    .graphicsLayer(scaleX = 1.2f, scaleY = 1.2f)
                    .alpha(imageAlpha)
                    .blur(imageBlur)
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

        Column(
            modifier = Modifier
                .padding(8.dp)
                .matchParentSize()
                .animateContentSize(tween(1000, easing = LinearEasing))
        ) {
            val userPic = quoteDataModel.user?.picurl ?: ""
            val userName = quoteDataModel.user?.name ?: ""


            val contentBorderSize = animateDpAsState(
                targetValue = 3.dp,
                label = "backgroundBorder",
                animationSpec = tween(
                    1000,
                    easing = FastOutLinearInEasing,
                    delayMillis = 1000
                )
            )


            ClassicTitle(
                userPic = userPic,
                userName = userName,
                textColor = style.getTextColor(),
                brush = brush
            )

            Divider(
                color = MaterialColor.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .gradientFill(brush),
                thickness = contentBorderSize.value
            )

            ClassicText(
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

    LaunchedEffect(Unit) {
        if (!animationEnabled) {
            animationCompleted = true
        }
    }
}

@Composable
fun ClassicText(
    quote: Quote, style: Style,
    styleCardAction: StyleCardAction?,
    animationCompleted: Boolean,
    animationEnabled: Boolean,
    onCompleteAnimation: (Boolean) -> Unit
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


@Preview
@Composable
fun ClassicTitlePreview() {
    Column(modifier = Modifier.fillMaxWidth()) {
        ClassicTitle(
            userName = "Motiv",
            textColor = MaterialColor.DeepPurpleA400,
            brush = motivGradient()
        )
    }
}

@Preview
@Composable
fun ClassicWindowPreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        ClassicWindow(
            quoteDataModel = QuoteDataModel(
                Quote("Classic preview", "Author preview"),
                style = Style(),
                user = null
            ),
            animationEnabled = false,
            loadAsGif = false,
        )
    }
}