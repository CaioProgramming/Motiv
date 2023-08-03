package com.ilustris.motiv.base.utils

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import com.ilustris.motiv.base.data.model.AnimationOptions


fun AnimationOptions.getEnterAnimation(): EnterTransition {
    return when (this) {
        AnimationOptions.TYPE -> fadeIn(tween(1000))
        AnimationOptions.FADE -> fadeIn(tween(500))
        AnimationOptions.SCALE -> scaleIn(tween(1000))
    }
}

fun AnimationOptions.getExitAnimation(): ExitTransition {
    return when (this) {
        AnimationOptions.TYPE -> fadeOut(tween(1200))
        AnimationOptions.FADE -> fadeOut(tween(1300))
        AnimationOptions.SCALE -> fadeOut(tween(1400))
    }
}

fun AnimationOptions.getDelay(): Float {
    return when (this) {
        AnimationOptions.TYPE -> 25f
        AnimationOptions.FADE -> 100f
        AnimationOptions.SCALE -> 300f
    }
}