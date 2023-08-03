package com.ilustris.motiv.base.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.creat.motiv.base.R
import com.ilustris.motiv.foundation.ui.theme.gradientAnimation
import com.ilustris.motiv.foundation.ui.theme.gradientFill
import com.ilustris.motiv.foundation.ui.theme.motivBrushes


@Composable
fun MotivLoader(
    message: String? = null,
    modifier: Modifier
) {
    val infiniteTransition = rememberInfiniteTransition()
    val offsetAnimation = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 100f,
        animationSpec = infiniteRepeatable(
            tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        )
    )
    val brush = Brush.linearGradient(
        motivBrushes(),
        start = Offset(offsetAnimation.value, offsetAnimation.value),
        end = Offset(x = offsetAnimation.value * 10, y = offsetAnimation.value * 5)
    )
    Column(
        modifier = modifier
            .graphicsLayer(alpha = 0.99f)
            .drawWithCache {
                onDrawWithContent {
                    drawContent()
                    drawRect(brush, blendMode = BlendMode.SrcAtop)
                }
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val waveAnimation by rememberLottieComposition(
            LottieCompositionSpec.RawRes(R.raw.rotating_wave)
        )

        val waveProgress by animateLottieCompositionAsState(
            waveAnimation,
            isPlaying = true,
            iterations = LottieConstants.IterateForever
        )

        LottieAnimation(
            waveAnimation,
            waveProgress,
            modifier = Modifier
                .size(50.dp)
                .gradientFill(gradientAnimation())
        )

        AnimatedVisibility(visible = message != null, enter = fadeIn(), exit = fadeOut()) {
            Text(
                text = message ?: "",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }


    }
}