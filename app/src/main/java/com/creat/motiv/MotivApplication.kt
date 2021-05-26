package com.creat.motiv

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.ilustris.motiv.base.Tools
import com.ilustris.motiv.base.utils.delayedFunction


class MotivApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this)
        CaocConfig.Builder.create()
            .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT)
            .trackActivities(true)
            .errorActivity(ErrorActivity::class.java).apply()
        RequestConfiguration.Builder().setTestDeviceIds(Tools.TEST_DEVICES)
        delayedFunction(3000) {
            handleCache()
        }
        // Thread.setDefaultUncaughtExceptionHandler { thread, e -> handleUncaughtException(e) }

    }

    override fun onTerminate() {
        super.onTerminate()
        handleCache()
    }

    private fun handleCache() {
        var size: Long = 0
        val files = cacheDir.listFiles()
        files?.run {
            forEach {
                size += it.length()
            }
        }
        if (size >= 20000) {
            cacheDir.deleteRecursively()
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }


}
