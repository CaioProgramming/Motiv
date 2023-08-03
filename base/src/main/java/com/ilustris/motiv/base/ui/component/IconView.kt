package com.ilustris.motiv.base.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ilustris.motiv.base.data.model.Icon
import com.ilustris.motiv.foundation.ui.theme.gradientAnimation
import com.ilustris.motiv.foundation.ui.theme.grayGradients
import com.ilustris.motiv.foundation.ui.theme.radioIconModifier
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.glide.GlideImageState

@Composable
fun IconView(
    icon: Icon,
    iconSize: Dp = 170.dp,
    isSelected: Boolean = false,
    onSelectIcon: (Icon) -> Unit
) {
    val loadedImage = remember { mutableStateOf(false) }
    val alphaAnimation = animateFloatAsState(
        targetValue = if (loadedImage.value) 1f else 0f,
        tween(1000)
    )


    GlideImage(
        imageModel = { icon.uri },
        onImageStateChanged = {
            loadedImage.value = it is GlideImageState.Success
        },
        modifier = Modifier
            .padding(8.dp)
            .alpha(alphaAnimation.value)
            .radioIconModifier(
                0f,
                iconSize,
                if (isSelected) gradientAnimation() else grayGradients(),
                2.dp
            )
            .clickable {
                onSelectIcon(icon)
            }
    )
}