package com.creat.motiv.view.activities

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import cat.ereza.customactivityoncrash.config.CaocConfig


class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        CaocConfig.Builder.create()
                .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //default: CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM
                .trackActivities(true) //default: false
                .errorActivity(ErrorActivity::class.java)
                .apply()
        // Thread.setDefaultUncaughtExceptionHandler { thread, e -> handleUncaughtException(e) }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    private fun handleUncaughtException(e: Throwable) {
        e.message?.let { Log.println(Log.ERROR, "Erro", it) }
        val intent = Intent(applicationContext, ErrorActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

}
