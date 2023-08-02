package com.ilustris.motiv.base.utils

import android.content.Context
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.creat.motiv.base.R
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.ilustris.motiv.base.ErrorActivity


const val RC_SIGN_IN = 1
const val WEB_URL = "https://www."
const val AD_GIF = "https://media.giphy.com/media/nwHqjGXnV5xhm/giphy.gif"
const val NEW_PIC = "NEW_PIC"
const val GIPHY_KEY = "spg4bqTWAgxiLjsh4VEnQ1Embqpg3dmk"
const val INITIALACT = "com.creat.motiv.MainActivity"

fun setupResources(context: Context) {

    MobileAds.initialize(context)
    CaocConfig.Builder.create()
        .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT)
        .trackActivities(true)
        .errorActivity(ErrorActivity::class.java).apply()
    val testDevices = context.resources.getStringArray(R.array.devices)
    RequestConfiguration.Builder().setTestDeviceIds(testDevices.toList()).build().apply {
        MobileAds.setRequestConfiguration(this)
    }
}