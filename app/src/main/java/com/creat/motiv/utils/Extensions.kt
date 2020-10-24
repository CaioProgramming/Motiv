package com.creat.motiv.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import com.creat.motiv.R
import com.github.mmin18.widget.RealtimeBlurView
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Completable
import io.reactivex.subjects.CompletableSubject
import java.lang.String.format

fun Snackbar.config(context: Context) {
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

fun View.fadeIn(duration: Long = 500): Completable {
    val animationSubject = CompletableSubject.create()
    return animationSubject.doOnSubscribe {
        visible()
        ViewCompat.animate(this)
                .setDuration(duration)
                .alpha(1f)
                .withEndAction {
                    animationSubject.onComplete()
                }
    }
}

fun View.fadeOut(duration: Long = 500): Completable {
    val animationSubject = CompletableSubject.create()
    return animationSubject.doOnSubscribe {
        ViewCompat.animate(this)
                .setDuration(duration)
                .alpha(0f)
                .withEndAction {
                    gone()
                    animationSubject.onComplete()
                }
    }
}

fun toHex(intColor: Int): String {
    return format("#%06X", 0xFFFFFF and intColor)
}

fun blurView(context: Context) {
    if (context is Activity) {
        val blur: RealtimeBlurView? = context.findViewById(R.id.rootblur)
        blur?.fadeIn()?.subscribe()
    } else {
        Log.e("Blur view", "blurView: context isn't an activity")
    }
}

fun unblurView(context: Context) {
    if (context is Activity) {
        val blur: RealtimeBlurView? = context.findViewById(R.id.rootblur)
        blur?.fadeOut()?.subscribe()
    } else {
        Log.e("Unblur view", "blurView: context isn't an activity")
    }
}
