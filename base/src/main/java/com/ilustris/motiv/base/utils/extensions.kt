package com.ilustris.motiv.base.utils

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.ilustris.animations.fadeIn
import com.ilustris.motiv.base.R
import com.ilustris.motiv.base.beans.FontStyle
import com.ilustris.motiv.base.beans.TextAlignment
import com.ilustris.ui.extensions.visible


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

fun Context.hideSupportActionBar() {
    if (this is AppCompatActivity) {
        val activity: AppCompatActivity = this
        activity.supportActionBar?.hide()
    }
}

fun FontStyle.getTypefaceStyle(): Int {
    return when (this) {
        FontStyle.REGULAR -> Typeface.NORMAL
        FontStyle.BOLD -> Typeface.BOLD
        FontStyle.ITALIC -> Typeface.ITALIC
    }
}


fun ImageView.loadGif(url: String, onLoadComplete: (() -> Unit)? = null) {
    Log.i(javaClass.simpleName, "loadGif: loading gif $url ")
    try {
        Glide.with(context)
            .asGif()
            .centerCrop()
            .load(url)
            .error(R.drawable.motiv_gradient)
                .addListener(object : RequestListener<GifDrawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<GifDrawable>?, isFirstResource: Boolean): Boolean {
                        setImageResource(R.drawable.motiv_gradient)
                        visible()
                        return false
                    }

                    override fun onResourceReady(resource: GifDrawable?, model: Any?, target: Target<GifDrawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        Log.i(javaClass.simpleName, "onResourceReady: gif($url) loaded")
                        setImageDrawable(resource)
                        if (visibility == View.GONE) fadeIn()
                        onLoadComplete?.invoke()
                        return false
                    }
                }).into(this)
    } catch (e: Exception) {
        e.printStackTrace()
        setImageResource(R.drawable.motiv_gradient)
    }

}

fun ImageView.loadImage(url: String) {
    try {
        context?.let {
            Glide.with(it).load(url).error(R.drawable.ic_neptune).into(this)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        setImageResource(R.drawable.ic_neptune)
    }
}

fun Context.activity(): Activity? {
    return try {
        this as Activity?
    } catch (e: Exception) {
        null
    }
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
