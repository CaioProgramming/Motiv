package com.creat.motiv.utilities

import android.app.Activity
import android.content.Context
import android.util.Log
import com.creat.motiv.R
import com.github.mmin18.widget.RealtimeBlurView
import com.ilustris.animations.fadeIn
import com.ilustris.animations.fadeOut


fun blurView(context: Context) {
    if (context is Activity) {
        val blur: RealtimeBlurView? = context.findViewById(R.id.rootblur)

        blur?.fadeIn()
    } else {
        Log.e("Blur view", "blurView: context isn't an activity")
    }
}

fun unblurView(context: Context) {
    if (context is Activity) {
        val blur: RealtimeBlurView? = context.findViewById(R.id.rootblur)
        blur?.fadeOut()
    } else {
        Log.e("Unblur view", "blurView: context isn't an activity")
    }
}

