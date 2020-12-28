package com.creat.motiv.utilities

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.creat.motiv.R
import com.github.mmin18.widget.RealtimeBlurView
import com.google.android.material.snackbar.Snackbar
import java.lang.String.format


fun Context.hideBackButton() {
    if (this is AppCompatActivity) {
        val activity: AppCompatActivity = this
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }
}

fun Context.showSupportActionBar() {
    if (this is AppCompatActivity) {
        val activity: AppCompatActivity = this
        activity.supportActionBar?.show()
    }
}

fun Snackbar.config() {
    val params = this.view.layoutParams as ViewGroup.MarginLayoutParams
    params.setMargins(12, 12, 12, 12)
    this.view.layoutParams = params
    this.view.background = context.getDrawable(R.drawable.bg_snackbar)
    ViewCompat.setElevation(this.view, 6f)
}

fun View.gone() {
    visibility = GONE
}

fun View.visible() {
    visibility = VISIBLE
}

fun View.invisible() {
    visibility = INVISIBLE
}

fun View.repeatBounce() {
    val bounceRepeat = AnimationUtils.loadAnimation(context, R.anim.bounce_repeat)
    startAnimation(bounceRepeat)
}


fun TextView.autoSizeText() {
    val length = text.toString().length
    this.textSize = when {
        length <= 40 -> {
            context.resources.getDimension(R.dimen.big_quote_size)
        }
        length <= 80 -> {
            context.resources.getDimension(R.dimen.default_quote_size)

        }
        length >= 90 -> {
            context.resources.getDimension(R.dimen.medium_quote_size)

        }
        length >= 150 -> {
            context.resources.getDimension(R.dimen.low_quote_size)

        }
        else -> context.resources.getDimension(R.dimen.min_quote_size)
    }
}

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

fun View.slideInBottom() {
    visible()
    val slideInBottom = AnimationUtils.loadAnimation(context, R.anim.slide_in_bottom)
    startAnimation(slideInBottom)
}

fun toHex(intColor: Int): String {
    return format("#%06X", 0xFFFFFF and intColor)
}

fun blurView(context: Context) {
    if (context is Activity) {
        val blur: RealtimeBlurView? = context.findViewById(R.id.rootblur)

        blur?.visible()
    } else {
        Log.e("Blur view", "blurView: context isn't an activity")
    }
}

fun unblurView(context: Context) {
    if (context is Activity) {
        val blur: RealtimeBlurView? = context.findViewById(R.id.rootblur)
        blur?.gone()
    } else {
        Log.e("Unblur view", "blurView: context isn't an activity")
    }
}

fun snackmessage(context: Context, message: String, backcolor: Int = Color.BLACK, textColor: Int = Color.WHITE) {
    if (context is Activity) {
        val contextView = context.findViewById<View>(R.id.mainContainer)
        Snackbar.make(contextView, message, Snackbar.LENGTH_SHORT)
                .setTextColor(textColor)
                .setBackgroundTint(backcolor)
                .show()
    }


}
