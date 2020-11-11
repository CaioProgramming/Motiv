package com.creat.motiv.view.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler

import androidx.appcompat.app.AppCompatActivity
import androidx.multidex.BuildConfig
import com.creat.motiv.R

class ErrorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error)
        if (!BuildConfig.DEBUG) {
            val handler = Handler()
            handler.postDelayed({
                val i = Intent(this, Splash::class.java)
                startActivity(i)
                this.finish()
            }, 10000)
        }


    }
}
