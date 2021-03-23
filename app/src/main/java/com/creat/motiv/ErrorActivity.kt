package com.creat.motiv

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.creat.motiv.view.activities.MainActivity
import com.ilustris.motiv.base.R

class ErrorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error)
        val errorBackground: ImageView = findViewById(R.id.errorBackground)
        Glide.with(this).asGif().centerCrop().load("https://media.giphy.com/media/xT9Igqq02d80wIqUpy/giphy.gif").into(errorBackground)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
    }
}