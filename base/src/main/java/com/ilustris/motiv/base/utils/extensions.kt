package com.ilustris.motiv.base.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.github.mmin18.widget.RealtimeBlurView
import com.ilustris.motiv.base.beans.TextAlignment
import com.google.android.material.snackbar.Snackbar
import com.ilustris.animations.fadeIn
import com.ilustris.animations.fadeOut
import com.ilustris.animations.slideDown
import com.ilustris.animations.slideUp
import com.ilustris.motiv.base.R
import com.silent.ilustriscore.core.utilities.gone
import com.silent.ilustriscore.core.utilities.visible


fun Context.hideBackButton() {
    if (this is AppCompatActivity) {
        val activity: AppCompatActivity = this
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }
}

fun Activity.getToolbar(): Toolbar? = findViewById(R.id.motiv_toolbar)

fun Activity.setMotivTitle(title: String) {
    try {
        val titleTextView: TextView? = findViewById(R.id.motivTitle)
        titleTextView?.text = title
    } catch (e: Exception) {
        Log.e("Motiv title", "setTitle: cannot change app title $e")
    }
}

fun Context.showSupportActionBar() {
    if (this is AppCompatActivity) {
        val activity: AppCompatActivity = this
        activity.supportActionBar?.show()
        activity.findViewById<RelativeLayout?>(R.id.titleView)?.run {
            visible()
        }
    }
}

fun Context.hideSupportActionBar() {
    if (this is AppCompatActivity) {
        val activity: AppCompatActivity = this
        activity.supportActionBar?.hide()
        activity.findViewById<RelativeLayout?>(R.id.titleView)?.run {
            gone()
        }
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

