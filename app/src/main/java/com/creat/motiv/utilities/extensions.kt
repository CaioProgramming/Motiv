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
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }
}

fun Context.showSupportActionBar() {
    if (this is AppCompatActivity) {
        val activity: AppCompatActivity = this
        activity.supportActionBar?.show()
    }
}

fun Context.hideSupporActionBar() {
    if (this is AppCompatActivity) {
        val activity: AppCompatActivity = this
        activity.supportActionBar?.hide()
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


fun TextView.autoSizeText(maxSize: Int) {
    val size = context.resources.getDimension(maxSize)
    val length = text.toString().length
    val txtSize = when {
        length <= 40 -> size

        length <= 80 -> size - 10

        length >= 90 -> size - 15

        length >= 150 -> size - 20

        else -> size - 25
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
