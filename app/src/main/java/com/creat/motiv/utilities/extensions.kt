package com.creat.motiv.utilities

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.util.TypedValue
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
        activity.supportActionBar.setDisplayHomeAsUpEnabled(false)
    }
}

fun Context.showSupportActionBar() {
    if (this is AppCompatActivity) {
        val activity: AppCompatActivity = this
        activity.supportActionBar.show()
    }
}

fun AppCompatActivity.changeNavColor(color: Int) {
    window.navigationBarColor = color
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




fun TextView.autoSizeText() {
    val length = text.toString().length
    val txtSize = when {
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
    setTextSize(TypedValue.COMPLEX_UNIT_PX, txtSize)
}

fun snackmessage(context: Context, message: String, backcolor: Int = Color.BLACK, textColor: Int = Color.WHITE) {
    if (context is Activity) {
        val contextView = context.findViewById<View>(R.id.mainContainer)
        Snackbar.make(contextView, message, Snackbar.LENGTH_LONG)
                .setTextColor(textColor)
                .setBackgroundTint(backcolor)
                .show()
    }


}
