package com.creat.motiv

import android.content.Context
import android.util.Log
import com.bumptech.glide.Glide
import com.creat.motiv.databinding.QuoteAdvertiseLayoutBinding
import com.creat.motiv.model.beans.User
import com.creat.motiv.utilities.fadeOut
import com.creat.motiv.utilities.gone
import com.creat.motiv.utilities.popIn
import com.creat.motiv.utilities.slideInBottom
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.ads.formats.UnifiedNativeAd

object AdUtils {
    fun loadCardAdvertise(context: Context, quoteAdvertiseLayoutBinding: QuoteAdvertiseLayoutBinding) {
        val adLoader = AdLoader.Builder(context, context.resources.getString(R.string.feed_advertisement_id))
        adLoader.forUnifiedNativeAd { ad: UnifiedNativeAd ->
            if (ad.icon != null) {
                Glide.with(context).load(ad.icon.uri).into(quoteAdvertiseLayoutBinding.userTop.userpic)
            } else {
                quoteAdvertiseLayoutBinding.userTop.userpic.gone()
            }
            quoteAdvertiseLayoutBinding.adView.run {
                setNativeAd(ad)
                headlineView = quoteAdvertiseLayoutBinding.adHeadline
                advertiserView = quoteAdvertiseLayoutBinding.userTop.userContainer
                adChoicesView = quoteAdvertiseLayoutBinding.adChoices
                bodyView = quoteAdvertiseLayoutBinding.adBody
                starRatingView = quoteAdvertiseLayoutBinding.appRating
                callToActionView = quoteAdvertiseLayoutBinding.adCard
                iconView = quoteAdvertiseLayoutBinding.userTop.userpic
                mediaView = quoteAdvertiseLayoutBinding.adAppMedia

            }

            quoteAdvertiseLayoutBinding.userTop.userData = User(name = ad.advertiser
                    ?: "Anunciante não identificado")
            quoteAdvertiseLayoutBinding.adAppMedia.apply {
                setMediaContent(ad.mediaContent)
            }
            quoteAdvertiseLayoutBinding.appRating.isEnabled = false
            quoteAdvertiseLayoutBinding.advertise = ad
            if (!ad.headline.contains("Test ad") && !ad.body.contains("Test ad") && !ad.advertiser.contains("Test ad")) {
                quoteAdvertiseLayoutBinding.adCard.slideInBottom()
            }
            quoteAdvertiseLayoutBinding.loading.fadeOut()

        }.withAdListener(object : AdListener() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.e("AdMob", "onAdFailedToLoad: ${adError.message}")
                quoteAdvertiseLayoutBinding.adCard.gone()
                quoteAdvertiseLayoutBinding.loading.popIn()
            }
        }).withNativeAdOptions(NativeAdOptions.Builder().build())
        val loader = adLoader.build()
        val adrequest = AdRequest.Builder().build()
        loader.loadAd(adrequest)
    }
}