package com.creat.motiv.utilities

import android.content.Context
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.creat.motiv.R
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.ads.formats.UnifiedNativeAd

class AdvertiseHelper(private val context: Context, private val advertiseID: String) {

    fun loadAd(onAdLoaded: (UnifiedNativeAd) -> Unit, onAdError: () -> Unit) {
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