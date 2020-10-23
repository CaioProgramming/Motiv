package com.creat.motiv.utils

import android.content.Context
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import com.creat.motiv.R
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
