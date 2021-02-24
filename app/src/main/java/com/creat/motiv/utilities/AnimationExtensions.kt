package com.creat.motiv.utilities

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.creat.motiv.R
import com.github.mmin18.widget.RealtimeBlurView

fun View.fadeIn() {
    visible()
    val fadein = AnimationUtils.loadAnimation(context, R.anim.fade_in)
    startAnimation(fadein)
}

fun View.fadeOut() {
    val fadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out)
    fadeOut.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(p0: Animation?) {
        }

        override fun onAnimationEnd(p0: Animation?) {
            gone()
        }

        override fun onAnimationRepeat(p0: Animation?) {
        }

    })
    startAnimation(fadeOut)
}

fun View.repeatFade() {
    val fadeRepeat = AnimationUtils.loadAnimation(context, R.anim.fade_in_repeat)
    startAnimation(fadeRepeat)
}

fun View.bounce() {
    val bounce = AnimationUtils.loadAnimation(context, R.anim.bounce)
    startAnimation(bounce)
}

fun View.popIn() {
    visible()
    val popIn = AnimationUtils.loadAnimation(context, R.anim.pop_in)
    startAnimation(popIn)
}

fun View.popOut() {
    val popOut = AnimationUtils.loadAnimation(context, R.anim.pop_out)
    popOut.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(p0: Animation?) {
        }

        override fun onAnimationEnd(p0: Animation?) {
            gone()
        }

        override fun onAnimationRepeat(p0: Animation?) {
        }

    })
    startAnimation(popOut)
}

fun View.repeatBounce() {
    val bounceRepeat = AnimationUtils.loadAnimation(context, R.anim.bounce_repeat)
    startAnimation(bounceRepeat)
}

fun View.slideInBottom() {
    visible()
    val slideInBottom = AnimationUtils.loadAnimation(context, R.anim.slide_in_bottom)
    startAnimation(slideInBottom)
}

fun blurView(context: Context) {
    if (context is Activity) {
        val blur: RealtimeBlurView? = context.findViewById(R.id.rootblur)

        blur.visible()
    } else {
        Log.e("Blur view", "blurView: context isn't an activity")
    }
}

fun unblurView(context: Context) {
    if (context is Activity) {
        val blur: RealtimeBlurView? = context.findViewById(R.id.rootblur)
        blur.gone()
    } else {
        Log.e("Unblur view", "blurView: context isn't an activity")
    }
}

