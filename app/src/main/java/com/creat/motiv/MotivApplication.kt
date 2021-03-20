package com.creat.motiv

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.ilustris.motiv.base.Tools


class MotivApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this)
        CaocConfig.Builder.create()
                .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT)
                .trackActivities(true)
                .errorActivity(ErrorActivity::class.java).apply()
        RequestConfiguration.Builder().setTestDeviceIds(Tools.TEST_DEVICES)
        // Thread.setDefaultUncaughtExceptionHandler { thread, e -> handleUncaughtException(e) }

    }


    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }


}
