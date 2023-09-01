package com.ilustris.motiv.base.service

import android.content.Context
import android.util.Log
import com.creat.motiv.base.R
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.silent.ilustriscore.core.contract.DataError
import com.silent.ilustriscore.core.contract.ServiceResult
import javax.inject.Inject

class AdService @Inject constructor(private val context: Context) {

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

    fun getRewardedAd(adCallBackResult: (ServiceResult<DataError, RewardedAd>) -> Unit) {
        val request = AdManagerAdRequest.Builder().build()
        RewardedAd.load(context, context.getString(R.string.video_advertisement_id), request,
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(p0: RewardedAd) {
                    super.onAdLoaded(p0)
                    adCallBackResult(ServiceResult.Success(p0))
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    Log.e(javaClass.simpleName, "onAdFailedToLoad: Ad failed ${p0.message}")
                    adCallBackResult(ServiceResult.Error(DataError.Unknown(p0.message)))
                }
            })
    }

}