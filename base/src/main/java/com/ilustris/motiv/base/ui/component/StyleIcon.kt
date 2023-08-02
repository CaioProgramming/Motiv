package com.ilustris.motiv.base.ui.component

import android.graphics.Bitmap
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.ilustris.motiv.base.data.model.Style
import com.ilustris.motiv.foundation.ui.theme.brushsFromPalette
import com.ilustris.motiv.foundation.ui.theme.grayGradients
import com.ilustris.motiv.foundation.ui.theme.motivGradient
import com.ilustris.motiv.foundation.ui.theme.paletteFromBitMap
import com.ilustris.motiv.foundation.ui.theme.radioIconModifier
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.glide.GlideImageState
import com.skydoves.landscapist.glide.GlideRequestType

@Composable
fun StyleIcon(style: Style, isSaving: Boolean, selectStyle: (Style) -> Unit) {

    var visualizerBitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }
    val infiniteTransition = rememberInfiniteTransition()

    val rotationAnimation = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (isSaving) 360f * 5 else 0f,
        animationSpec = infiniteRepeatable(
            tween(5000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )
    var blurAnimation = animateDpAsState(
        targetValue = if (isSaving) 15.dp else 0.dp,
        animationSpec = tween(1000, easing = FastOutLinearInEasing), label = ""
    )


    val borderBrush = visualizerBitmap?.paletteFromBitMap()?.brushsFromPalette() ?: motivGradient()

    GlideImage(
        imageModel = { style.backgroundURL },
        glideRequestType = GlideRequestType.BITMAP,
        onImageStateChanged = { state ->
            if (state is GlideImageState.Success) {
                state.imageBitmap?.let {
                    visualizerBitmap = it.asAndroidBitmap()
                }
            }
        },
        imageOptions = ImageOptions(
            Alignment.Center,
            contentScale = ContentScale.Crop
        ),
        modifier = Modifier
            .padding(8.dp)
            .radioIconModifier(
                brush = borderBrush,
                rotationValue = rotationAnimation.value,
                sizeValue = 64.dp,
            )
            .blur(blurAnimation.value)
            .clickable {
                selectStyle(style)
            }
            .background(
                grayGradients(),
                CircleShape
            )

    )


}