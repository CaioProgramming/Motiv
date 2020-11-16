package com.creat.motiv.utilities

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.Typeface
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.creat.motiv.BuildConfig
import com.creat.motiv.databinding.QuoteAdvertiseLayoutBinding
import com.creat.motiv.model.beans.User
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.ads.formats.UnifiedNativeAd
import java.util.*


object Tools {
    val TEST_DEVICES = listOf("0B05DB678D8A74AECB7F8E90C0AC97B5")

    fun setLightStatusBar(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var flags = activity.window.decorView.systemUiVisibility
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            activity.window.decorView.systemUiVisibility = flags
            var navigationflag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                flags or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            } else {
                Log.println(Log.INFO, "Navigation Style", "Device is lower than android O")
            }
            activity.window.decorView.systemUiVisibility = navigationflag

        }
    }


    fun darkMode(activity: Activity): Boolean {
        return when (activity.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            Configuration.UI_MODE_NIGHT_NO -> false
            Configuration.UI_MODE_NIGHT_UNDEFINED -> false
            else -> false
        }
    }

    private val offlineStatusMessage = arrayOf("Sem internet é? Acontece né vá aproveitar a vida",
            "Parece que você está desconectado. Quando você se reconectar eu mostro umas frases",
            "Bom... Parece que você tá sem internet, então acho que não tem oque fazer...")

    val empyquotes = listOf("Você não vai escrever nada? Tá achando que é festa?",
            "O vazio da sua existência não necessariamente precisa ser o vazio do bloco de texto, escreva algo!",
            "Você não quer ver o feed e ver um texto vazio né? Então por favor escreve algo aí",
            "Ah qual é qual a necessidade de não escrever nada?")


    fun fonts(context: Context): List<Typeface> {
        return listOf(Typeface.createFromAsset(context.assets, "fonts/Arvo-Regular_201.ttf"),
                Typeface.createFromAsset(context.assets, "fonts/Audrey-Normal.otf"),
                Typeface.createFromAsset(context.assets, "fonts/Cornerstone.ttf"),
                Typeface.createFromAsset(context.assets, "fonts/times.ttf"),
                Typeface.createFromAsset(context.assets, "fonts/MightypeScript.otf"),
                Typeface.createFromAsset(context.assets, "fonts/AmaticSC-Regular.ttf"),
                Typeface.createFromAsset(context.assets, "fonts/Amiko-Regular.ttf"),
                Typeface.createFromAsset(context.assets, "fonts/BlackHanSans-Regular.ttf"),
                Typeface.createFromAsset(context.assets, "fonts/Cabin-Regular.ttf"),
                Typeface.createFromAsset(context.assets, "fonts/Cinzel-Regular.ttf"),
                Typeface.createFromAsset(context.assets, "fonts/CinzelDecorative-Regular.ttf"),
                Typeface.createFromAsset(context.assets, "fonts/Farsan-Regular.ttf"),
                Typeface.createFromAsset(context.assets, "fonts/FingerPaint-Regular.ttf"),
                Typeface.createFromAsset(context.assets, "fonts/FredokaOne-Regular.ttf"),
                Typeface.createFromAsset(context.assets, "fonts/Inconsolata-Regular.ttf"),
                Typeface.createFromAsset(context.assets, "fonts/Lalezar-Regular.ttf"),
                Typeface.createFromAsset(context.assets, "fonts/Lobster-Regular.ttf"),
                Typeface.createFromAsset(context.assets, "fonts/Mogra-Regular.ttf"),
                Typeface.createFromAsset(context.assets, "fonts/Nunito-Regular.ttf"),
                Typeface.createFromAsset(context.assets, "fonts/NunitoSans-Regular.ttf"),
                Typeface.createFromAsset(context.assets, "fonts/Pacifico-Regular.ttf"),
                Typeface.createFromAsset(context.assets, "fonts/Quicksand-Regular.ttf"),
                Typeface.createFromAsset(context.assets, "fonts/Rakkas-Regular.ttf"),
                Typeface.createFromAsset(context.assets, "fonts/Ranga-Regular.ttf"),
                Typeface.createFromAsset(context.assets, "fonts/Rasa-Regular.ttf"))
    }

    fun emptyquote(): String {
        val x = Random()
        return empyquotes[x.nextInt(empyquotes.size)]
    }


    fun offlinemessage(): String {
        val x = Random()
        return offlineStatusMessage[x.nextInt(offlineStatusMessage.size)]
    }


    fun loadAd(context: Context, quoteAdvertiseLayoutBinding: QuoteAdvertiseLayoutBinding) {
        val adLoader = AdLoader.Builder(context, if (BuildConfig.DEBUG) TEST_ADS_ID else APP_AD_ID)
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
                    ?: "Anúnciante não identificado")
            quoteAdvertiseLayoutBinding.adAppMedia.run {
                setMediaContent(ad.mediaContent)
                setImageScaleType(ImageView.ScaleType.CENTER_CROP)
            }
            quoteAdvertiseLayoutBinding.appRating.isEnabled = false
            quoteAdvertiseLayoutBinding.advertise = ad
            quoteAdvertiseLayoutBinding.adCard.fadeIn()

        }.withAdListener(object : AdListener() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.e("AdMob", "onAdFailedToLoad: ${adError.message}")
                quoteAdvertiseLayoutBinding.adCard.gone()
            }
        }).withNativeAdOptions(NativeAdOptions.Builder().build())
        val loader = adLoader.build()
        val adrequest = AdRequest.Builder().build()
        loader.loadAd(adrequest)
    }


}
