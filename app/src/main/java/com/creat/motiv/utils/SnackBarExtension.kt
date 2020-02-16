package com.creat.motiv.utils

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.ViewCompat
import com.creat.motiv.R
import com.google.android.material.snackbar.Snackbar

fun Snackbar.config(context: Context){
    val params = this.view.layoutParams as ViewGroup.MarginLayoutParams
    params.setMargins(12, 12, 12, 12)
    this.view.layoutParams = params
    this.view.background = context.getDrawable(R.drawable.bg_snackbar)
    ViewCompat.setElevation(this.view, 6f)
}