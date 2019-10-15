package com.creat.motiv.View.activities

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication

class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        Thread.setDefaultUncaughtExceptionHandler { thread, e -> handleUncaughtException(thread, e) }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    private fun handleUncaughtException(thread: Thread, e: Throwable) {
        Log.println(Log.ERROR, "Erro", e.message)
        val intent = Intent(applicationContext, ErrorActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)

    }

}
