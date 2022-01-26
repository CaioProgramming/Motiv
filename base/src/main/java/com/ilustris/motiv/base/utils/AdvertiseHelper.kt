package com.ilustris.motiv.base.utils

import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.ilustris.motiv.base.R

class AdvertiseHelper(private val context: Context) {

    fun loadAd(onAdLoaded: (UnifiedNativeAd) -> Unit, onAdError: () -> Unit) {
        val advertiseID = context.resources.getString(R.string.feed_advertisement_id)
        val adLoader = AdLoader.Builder(context, advertiseID)
        adLoader.forUnifiedNativeAd(onAdLoaded).withAdListener(object : AdListener() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.e("AdMob", "onAdFailedToLoad: ${adError.message}")
                onAdError.invoke()
            }
        }).withNativeAdOptions(NativeAdOptions.Builder().build())
        val loader = adLoader.build()
        val adRequest = AdRequest.Builder().build()
        loader.loadAd(adRequest)
    }

}