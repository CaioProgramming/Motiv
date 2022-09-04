package com.ilustris.motiv.base.utils

import android.content.Context
import android.util.Log
import com.github.mmin18.widget.RealtimeBlurView
import com.ilustris.animations.fadeIn
import com.ilustris.animations.fadeOut
import com.ilustris.motiv.base.R


fun blurView(context: Context) {
    try {
        context.activity()?.let {
            val blur: RealtimeBlurView? = it.findViewById(R.id.rootblur)
            blur?.fadeIn()
        }
    } catch (e: Exception) {
        Log.e("Blur view", "blurView: cannot blur view ${e.message}")

    }

}

fun unBlurView(context: Context) {
    try {
        context.activity()?.let {
            val blur: RealtimeBlurView? = it.findViewById(R.id.rootblur)
            blur?.fadeOut()
        }
    } catch (e: Exception) {
        Log.e("Unblur view", "unblurView: cannot blur view ${e.message}")

    }
}


