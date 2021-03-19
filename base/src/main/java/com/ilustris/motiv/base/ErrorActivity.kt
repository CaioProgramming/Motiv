package com.ilustris.motiv.base

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class ErrorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error)
        val errorBackground: ImageView = findViewById(R.id.errorBackground)
        Glide.with(this).asGif().centerCrop().load("https://media.giphy.com/media/xT9Igqq02d80wIqUpy/giphy.gif").into(errorBackground)
    }
}
