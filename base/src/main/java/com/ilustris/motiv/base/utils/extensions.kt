package com.ilustris.motiv.base.utils

import android.content.Context
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.google.android.material.snackbar.Snackbar
import com.ilustris.motiv.base.R


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

