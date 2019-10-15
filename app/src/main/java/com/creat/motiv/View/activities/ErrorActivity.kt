package com.creat.motiv.View.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ProgressBar

import androidx.appcompat.app.AppCompatActivity
import com.creat.motiv.R

class ErrorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error)
        val loading = findViewById<ProgressBar>(R.id.loading)
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
        this.finish()


    }
}
