package com.creat.motiv.utilities

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.Typeface
import android.os.Build
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.creat.motiv.BuildConfig
import com.creat.motiv.databinding.QuoteAdvertiseLayoutBinding
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.ads.formats.NativeAdOptions.NATIVE_MEDIA_ASPECT_RATIO_PORTRAIT
import com.google.android.gms.ads.formats.UnifiedNativeAd
import java.util.*


object Tools {

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

    private val offlinefrases = arrayOf("Sem internet é? Acontece né vá aproveitar a vida", "Parece que você está desconectado. Quando você se reconectar eu mostro umas frases", "Bom... Parece que você tá sem internet, então acho que não tem oque fazer...")



    fun fonts(context: Context): ArrayList<Typeface> {
        val fontsarchieves = arrayOf(Typeface.createFromAsset(context.assets, "fonts/Arvo-Regular_201.ttf"), Typeface.createFromAsset(context.assets, "fonts/Audrey-Normal.otf"), Typeface.createFromAsset(context.assets, "fonts/Cornerstone.ttf"), Typeface.createFromAsset(context.assets, "fonts/times.ttf"), Typeface.createFromAsset(context.assets, "fonts/MightypeScript.otf"), Typeface.createFromAsset(context.assets, "fonts/AmaticSC-Regular.ttf"), Typeface.createFromAsset(context.assets, "fonts/Amiko-Regular.ttf"), Typeface.createFromAsset(context.assets, "fonts/BlackHanSans-Regular.ttf"), Typeface.createFromAsset(context.assets, "fonts/Cabin-Regular.ttf"), Typeface.createFromAsset(context.assets, "fonts/Cinzel-Regular.ttf"), Typeface.createFromAsset(context.assets, "fonts/CinzelDecorative-Regular.ttf"), Typeface.createFromAsset(context.assets, "fonts/Farsan-Regular.ttf"), Typeface.createFromAsset(context.assets, "fonts/FingerPaint-Regular.ttf"), Typeface.createFromAsset(context.assets, "fonts/FredokaOne-Regular.ttf"), Typeface.createFromAsset(context.assets, "fonts/Inconsolata-Regular.ttf"), Typeface.createFromAsset(context.assets, "fonts/Lalezar-Regular.ttf"), Typeface.createFromAsset(context.assets, "fonts/Lobster-Regular.ttf"), Typeface.createFromAsset(context.assets, "fonts/Mogra-Regular.ttf"), Typeface.createFromAsset(context.assets, "fonts/Nunito-Regular.ttf"), Typeface.createFromAsset(context.assets, "fonts/NunitoSans-Regular.ttf"), Typeface.createFromAsset(context.assets, "fonts/Pacifico-Regular.ttf"), Typeface.createFromAsset(context.assets, "fonts/Quicksand-Regular.ttf"), Typeface.createFromAsset(context.assets, "fonts/Rakkas-Regular.ttf"), Typeface.createFromAsset(context.assets, "fonts/Ranga-Regular.ttf"), Typeface.createFromAsset(context.assets, "fonts/Rasa-Regular.ttf"))


        return ArrayList(Arrays.asList(*fontsarchieves))
    }

    fun emptyquote(): String {
        val empyquotes = arrayOf("Você não vai escrever nada? Tá achando que é festa?",
                "O vazio da sua existência não necessariamente precisa ser o vazio do bloco de texto, escreva algo!",
                "Você não quer ver o feed e ver um texto vazio né? Então por favor escreve algo aí",
                "Ah qual é qual a necessidade de não escrever nada?")
        val x = Random()
        return empyquotes[x.nextInt(empyquotes.size)]
    }


    fun offlinemessage(): String {
        val q: Int
        val x = Random()
        q = x.nextInt(offlinefrases.size)
        return offlinefrases[q]
    }


    fun loadAd(context: Context, quoteAdvertiseLayoutBinding: QuoteAdvertiseLayoutBinding) {
        val adLoader = AdLoader.Builder(context, if (BuildConfig.DEBUG) TEST_ADS_ID else APP_AD_ID)
        adLoader.forUnifiedNativeAd { ad: UnifiedNativeAd ->
            if (context is Activity && context.isDestroyed) {
                ad.destroy()
                return@forUnifiedNativeAd
            }
            quoteAdvertiseLayoutBinding.advertise = ad
            if (ad.icon != null) {
                Glide.with(context).load(ad.icon.uri).into(quoteAdvertiseLayoutBinding.adAppIcon)
            } else {
                quoteAdvertiseLayoutBinding.adIconCard.gone()
            }
            Glide.with(context).load(ad.images[0].uri).into(quoteAdvertiseLayoutBinding.adAppImage)
            quoteAdvertiseLayoutBinding.appRating.isEnabled = false
            quoteAdvertiseLayoutBinding.adView.run {
                headlineView = quoteAdvertiseLayoutBinding.adHeadline
                advertiserView = quoteAdvertiseLayoutBinding.adAdvertiser
                adChoicesView = quoteAdvertiseLayoutBinding.adChoices
                bodyView = quoteAdvertiseLayoutBinding.adBody
                starRatingView = quoteAdvertiseLayoutBinding.appRating
                callToActionView = quoteAdvertiseLayoutBinding.adActionButton
                iconView = quoteAdvertiseLayoutBinding.adAppIcon
                imageView = quoteAdvertiseLayoutBinding.adAppImage
            }
            quoteAdvertiseLayoutBinding.adCard.fadeIn()

        }.withAdListener(object : AdListener() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.e("AdMob", "onAdFailedToLoad: ${adError.message}")
                quoteAdvertiseLayoutBinding.adCard.gone()
            }
        })
                .withNativeAdOptions(NativeAdOptions.Builder().setMediaAspectRatio(NATIVE_MEDIA_ASPECT_RATIO_PORTRAIT).build())
        val loader = adLoader.build()
        val adrequest = AdRequest.Builder().build()
        loader.loadAd(adrequest)
    }


}
