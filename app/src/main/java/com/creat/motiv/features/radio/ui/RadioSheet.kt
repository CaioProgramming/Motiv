@file:OptIn(
    ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class,
    ExperimentalAnimationApi::class, ExperimentalFoundationApi::class
)

package com.creat.motiv.features.radio.ui

import android.graphics.Bitmap
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseInElastic
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.creat.motiv.R
import com.creat.motiv.features.radio.presentation.RadioViewModel
import com.ilustris.motiv.base.data.model.Radio
import com.ilustris.motiv.base.ui.component.MotivLoader
import com.ilustris.motiv.foundation.ui.component.CardBackground
import com.ilustris.motiv.foundation.ui.theme.colorsFromPalette
import com.ilustris.motiv.foundation.ui.theme.gradientAnimation
import com.ilustris.motiv.foundation.ui.theme.gradientFill
import com.ilustris.motiv.foundation.ui.theme.grayBrushes
import com.ilustris.motiv.foundation.ui.theme.paletteFromBitMap
import com.ilustris.motiv.foundation.ui.theme.radioIconModifier
import com.silent.ilustriscore.core.model.ViewModelBaseState

@Composable
fun RadioSheet(
    playingRadio: Radio? = null,
    enabled: Boolean = true,
    expanded: Boolean = false,
    isPlaying: Boolean = false,
    onExpand: () -> Unit,
    requestPlayOrPause: (Boolean) -> Unit,
    onSelectRadio: (Radio) -> Unit,
) {
    val radioViewModel = hiltViewModel<RadioViewModel>()
    val state = radioViewModel.viewModelState.observeAsState().value


    AnimatedVisibility(visible = state == ViewModelBaseState.LoadingState) {
        Box(modifier = Modifier.fillMaxWidth()) {
            MotivLoader(
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.Center)
            )

        }
    }

    var speed by remember {
        mutableFloatStateOf(0.5f)
    }
    val wavesAnimation by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.waves)
    )

    val waveProgress by animateLottieCompositionAsState(
        wavesAnimation,
        speed = speed * 0.5f,
        isPlaying = true,
        restartOnPlay = false,
        iterations = LottieConstants.IterateForever
    )



    AnimatedVisibility(
        visible = state is ViewModelBaseState.DataListRetrievedState,
        enter = slideInVertically() + fadeIn(
            tween(1000)
        ),
        exit = fadeOut(),
    ) {
        val radios = if (state is ViewModelBaseState.DataListRetrievedState) {
            state.dataList as List<Radio>
        } else emptyList()

        val radioIndex = playingRadio?.let { radios.indexOf(playingRadio) } ?: 0



        AnimatedContent(
            targetState = playingRadio,
            transitionSpec = {
                fadeIn(tween(2500, easing = LinearOutSlowInEasing)) togetherWith fadeOut(
                    tween(2000, easing = EaseIn)
                )
            },
            label = "radioContent"
        ) { targetRadio ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .animateContentSize(tween(1000)),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                var visualizerBitmap by remember {
                    mutableStateOf<Bitmap?>(null)
                }

                val currentBitmapColors = visualizerBitmap?.paletteFromBitMap()?.colorsFromPalette()
                val currentRadioBrush = gradientAnimation(currentBitmapColors ?: grayBrushes())


                val forwardButtonAlpha = animateFloatAsState(
                    targetValue = if (radioIndex == radios.size - 1) 0f else 1f,
                    tween(1500, easing = EaseInElastic),
                    label = "ForwardAlpha"
                )
                val reverseButtonAlpha = animateFloatAsState(
                    targetValue = if (radioIndex == 0) 0f else 1f,
                    tween(1500, easing = EaseInElastic),
                    label = "ForwardAlpha"
                )

                var collapsedAlphaAnimation =
                    animateFloatAsState(targetValue = if (!expanded) 1f else 0f, tween(1000))
                var expandedAlphaAnimation =
                    animateFloatAsState(targetValue = if (expanded) 1f else 0f, tween(1000))


                WaveAnimation(
                    brush = currentRadioBrush,
                    modifier = Modifier.alpha(collapsedAlphaAnimation.value)
                ) {
                    onExpand()
                }
                CardBackground(
                    modifier = Modifier
                        .padding(16.dp)
                        .radioIconModifier(
                            0f,
                            200.dp,
                            currentRadioBrush,
                            5.dp
                        ),
                    backgroundImage = targetRadio?.visualizer,
                    loadedBitmap = {
                        if (visualizerBitmap == null) {
                            visualizerBitmap = it.asAndroidBitmap()
                        }
                    }
                )
                Text(
                    text = targetRadio?.name ?: "",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.gradientFill(currentRadioBrush)
                )

                if (enabled) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .gradientFill(brush = currentRadioBrush),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {

                        val iconSize = 64.dp

                        IconButton(onClick = {
                            visualizerBitmap = null
                            onSelectRadio(radios[radioIndex - 1])
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_round_fast_rewind_24),
                                contentDescription = "Voltar",
                                modifier = Modifier
                                    .size(iconSize / 2)
                                    .graphicsLayer(alpha = reverseButtonAlpha.value)
                            )
                        }


                        IconButton(onClick = {
                            requestPlayOrPause(!isPlaying)
                        }) {
                            val currentIcon =
                                if (!isPlaying) R.drawable.ic_round_play_arrow_24 else R.drawable.ic_round_pause_24
                            val description = if (isPlaying) "Play" else "Pause"
                            AnimatedContent(
                                targetState = currentIcon,
                                transitionSpec = {
                                    scaleIn() togetherWith scaleOut()
                                },
                                label = "PauseAnimation"
                            ) {
                                Icon(
                                    painter = painterResource(id = it),
                                    contentDescription = description,
                                    modifier = Modifier
                                        .size(iconSize)
                                )
                            }
                        }


                        IconButton(onClick = {
                            visualizerBitmap = null
                            onSelectRadio(radios[radioIndex + 1])
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_round_fast_forward_24),
                                contentDescription = "Avançar",
                                modifier = Modifier
                                    .size(iconSize / 2)
                                    .graphicsLayer(alpha = forwardButtonAlpha.value)
                            )
                        }
                    }
                } else {
                    MotivLoader(
                        modifier = Modifier
                            .padding(16.dp)
                            .size(64.dp)
                            .gradientFill(currentRadioBrush)
                    )
                }
                WaveAnimation(
                    brush = currentRadioBrush,
                    Modifier.alpha(expandedAlphaAnimation.value)
                ) {
                    onExpand()
                }
            }
        }
    }


    LaunchedEffect(Unit) {
        radioViewModel.getAllData()
    }

    LaunchedEffect(state) {
        if (state is ViewModelBaseState.DataListRetrievedState) {
            val radios = state.dataList as List<Radio>
            if (playingRadio == null) {
                if (radios.isNotEmpty()) {
                    onSelectRadio(radios.random())
                }
            }
        }
    }
}

@Composable
fun WaveAnimation(brush: Brush, modifier: Modifier, onClick: () -> Unit) {


    val wavesAnimation by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.waves)
    )

    val waveProgress by animateLottieCompositionAsState(
        wavesAnimation,
        speed = 1f,
        isPlaying = true,
        restartOnPlay = false,
        iterations = LottieConstants.IterateForever
    )

    LottieAnimation(
        wavesAnimation,
        waveProgress,
        contentScale = ContentScale.FillBounds,
        modifier = modifier
            .fillMaxWidth()
            .height(32.dp)
            .gradientFill(brush = brush)
            .clickable {
                onClick()
            }
    )
}