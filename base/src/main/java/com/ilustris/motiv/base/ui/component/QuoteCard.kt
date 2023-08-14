package com.ilustris.motiv.base.ui.component

import ai.atick.material.MaterialColor
import android.graphics.Bitmap
import androidx.compose.animation.core.EaseInElastic
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.creat.motiv.base.R
import com.ilustris.motiv.base.data.model.QuoteDataModel
import com.ilustris.motiv.base.ui.presentation.QuoteActions
import com.ilustris.motiv.base.utils.BuildQuoteWindow
import com.ilustris.motiv.base.utils.backgroundBorderSize
import com.ilustris.motiv.base.utils.buildFont
import com.ilustris.motiv.base.utils.buildStyleShadow
import com.ilustris.motiv.base.utils.cardShape
import com.ilustris.motiv.base.utils.getFontStyle
import com.ilustris.motiv.base.utils.getTextAlign
import com.ilustris.motiv.base.utils.getTextColor
import com.ilustris.motiv.foundation.ui.theme.brushsFromPalette
import com.ilustris.motiv.foundation.ui.theme.colorsFromPalette
import com.ilustris.motiv.foundation.ui.theme.gradientAnimation
import com.ilustris.motiv.foundation.ui.theme.motivBrushes
import com.ilustris.motiv.foundation.ui.theme.motivGradient
import com.ilustris.motiv.foundation.ui.theme.paletteFromBitMap
import com.silent.ilustriscore.core.utilities.DateFormats
import com.silent.ilustriscore.core.utilities.format
import dev.shreyaspatil.capturable.Capturable
import dev.shreyaspatil.capturable.controller.rememberCaptureController

@Composable
fun QuoteCard(
    quoteDataModel: QuoteDataModel,
    modifier: Modifier,
    loadAsGif: Boolean = true,
    animationEnabled: Boolean = true,
    quoteActions: QuoteActions? = null
) {
    val quote = quoteDataModel.quoteBean
    val context = LocalContext.current
    var backgroundBitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }


    var animationCompleted by remember {
        mutableStateOf(false)
    }
    var sharing by remember {
        mutableStateOf(false)
    }

    val style = quoteDataModel.style
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

    LaunchedEffect(Unit) {
        if (!animationEnabled) animationCompleted = true
    }

    val brush = backgroundBitmap?.paletteFromBitMap()?.brushsFromPalette() ?: motivGradient()

    val captureController = rememberCaptureController()

    var imageLoaded by remember {
        mutableStateOf(false)
    }

    val imageBlur by animateDpAsState(
        targetValue = if (imageLoaded) 0.dp else 50.dp,
        tween(2500, delayMillis = 500, easing = EaseInElastic), label = "blur"
    )

    val imageAlpha by animateFloatAsState(
        targetValue = if (imageLoaded && animationCompleted) 1f else 0f,
        tween(1500), label = "alpha"
    )

    var dropDownState by remember {
        mutableStateOf(false)
    }
    val dropDownOptions =
        if (quoteDataModel.isUserQuote) listOf("Excluir", "Editar") else listOf("Denunciar")

    Column(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        val cardGradient = gradientAnimation(
            backgroundBitmap?.paletteFromBitMap()?.colorsFromPalette() ?: motivBrushes()
        )
        val backgroundBorderSize = animateDpAsState(
            targetValue = style.styleProperties?.customWindow?.backgroundBorderSize()
                ?: 2.dp,
            label = "backgroundBorder",
            animationSpec = tween(
                1500,
                easing = FastOutLinearInEasing,
                delayMillis = 500
            )
        )

        val cardShape = style.cardShape()

        Capturable(controller = captureController, onCaptured = { bitmap, error ->
            if (sharing) {
                bitmap?.let {
                    quoteActions?.onShare(quoteDataModel, it.asAndroidBitmap())
                    sharing = false
                }
            }

        }, modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.8f)
            .pointerInput(Unit) {
                detectTapGestures(onLongPress = {
                    dropDownState = true
                }, onDoubleTap = {
                    quoteActions?.onLike(quoteDataModel)
                })
            }) {


            BuildQuoteWindow(
                quoteDataModel = quoteDataModel,
                animationEnabled = animationEnabled,
                loadAsGif = loadAsGif
            )

            DropdownMenu(
                expanded = dropDownState,
                onDismissRequest = {
                    dropDownState = false
                },
            )
            {
                dropDownOptions.forEach { option ->
                    DropdownMenuItem(text = {
                        Text(
                            text = option,
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(8.dp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }, onClick = {
                        when (option) {
                            "Excluir" -> quoteActions?.onDelete(quoteDataModel)
                            "Editar" -> {
                                quoteActions?.onEdit(quoteDataModel)
                            }

                            "Denunciar" -> {
                                quoteActions?.onReport(quoteDataModel)
                            }
                        }
                        dropDownState = false
                    })
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth()
                .graphicsLayer(alpha = imageAlpha)

        ) {
            Column {
                Text(
                    text = "Postado por ${quoteDataModel.user?.name}",
                    modifier = Modifier
                        .padding(vertical = 2.dp)
                        .clickable {
                            quoteActions?.onClickUser(quote.userID)
                        },
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.W400,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
                Text(
                    text = "Em  ${quote.data?.toDate()?.format(DateFormats.DD_OF_MM_FROM_YYYY)}",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }


            Row {
                quoteActions?.let {
                    IconButton(
                        onClick = { quoteActions.onLike(quoteDataModel) },
                        modifier = Modifier.padding(4.dp)
                    ) {
                        val isFavorite = quoteDataModel.isFavorite
                        val color =
                            if (isFavorite) MaterialColor.Red500 else MaterialTheme.colorScheme.onBackground
                        val icon =
                            if (isFavorite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder
                        Icon(
                            icon,
                            contentDescription = "Curtir",
                            tint = color,
                        )
                    }

                    IconButton(
                        onClick = {
                            sharing = true
                            captureController.capture()
                        }, modifier = Modifier.padding(4.dp)
                    ) {
                        Icon(
                            painterResource(id = R.drawable.share),
                            contentDescription = "Compartilhar",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

            }

        }
    }


}




