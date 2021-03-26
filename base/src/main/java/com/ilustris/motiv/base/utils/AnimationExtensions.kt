package com.ilustris.motiv.base.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.TextView
import com.github.mmin18.widget.RealtimeBlurView
import com.ilustris.animations.*
import com.ilustris.motiv.base.R
import com.silent.ilustriscore.core.utilities.gone
import com.silent.ilustriscore.core.utilities.visible


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


