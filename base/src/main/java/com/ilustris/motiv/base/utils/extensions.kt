package com.ilustris.motiv.base.utils

import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.ilustris.motiv.base.beans.TextAlignment
import com.ilustris.motiv.base.beans.TextSize
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

fun TextView.defineTextSize(textSize: TextSize) {
        val maxSize = when (textSize) {
            TextSize.DEFAULT -> R.dimen.default_quote_size
            TextSize.BIG -> R.dimen.big_quote_size
            TextSize.SMALL -> R.dimen.low_quote_size
            TextSize.EXTRASMALL -> R.dimen.min_quote_size
        }
        autoSizeText(maxSize)
}

fun TextView.defineTextAlignment(textAlign: TextAlignment) {
    when(textAlign) {
        TextAlignment.CENTER -> {
            textAlignment = View.TEXT_ALIGNMENT_CENTER
            gravity = Gravity.CENTER
        }
        TextAlignment.START -> {
            textAlignment = View.TEXT_ALIGNMENT_TEXT_START
            gravity = Gravity.START
        }
        TextAlignment.END -> {
            textAlignment = View.TEXT_ALIGNMENT_TEXT_END
            gravity = Gravity.END
        }
    }
}

