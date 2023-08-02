package com.creat.motiv

import android.app.Application
import com.ilustris.motiv.base.utils.setupResources
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MotivApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setupResources(this)
    }
}
