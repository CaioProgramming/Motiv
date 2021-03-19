package com.creat.motiv.view.activities

import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.creat.motiv.R
import com.creat.motiv.view.adapters.AboutAdapter
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.google.android.gms.ads.reward.RewardedVideoAdListener
import com.ilustris.motiv.base.beans.QuoteStyle
import com.ilustris.motiv.base.utils.FontUtils
import com.ilustris.motiv.base.utils.loadGif
import com.silent.ilustriscore.core.utilities.showSnackBar
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.fragment_settings.*

class AboutActivity : AppCompatActivity(R.layout.activity_about) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(about_toolbar)
        aboutRecycler.adapter = AboutAdapter(this)
        support_devs.setOnClickListener {
            showAd()
        }
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            about_toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
        }
        val style = QuoteStyle.companyStyle
        style.run {
            companyText.run {
                setTextColor(Color.parseColor(style.textColor))
                typeface = FontUtils.getTypeFace(context, font)
            }
            companyBackground.loadGif(style.backgroundURL)
        }

    }


    private fun showAd() {

        val rewardedVideoAd: RewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this)
        rewardedVideoAd.rewardedVideoAdListener = object : RewardedVideoAdListener {
            override fun onRewardedVideoAdLoaded() {
                rewardedVideoAd.show()
            }

            override fun onRewardedVideoAdOpened() {

            }

            override fun onRewardedVideoStarted() {

            }

            override fun onRewardedVideoAdClosed() {

            }

            override fun onRewarded(p0: RewardItem?) {
            }


            override fun onRewardedVideoAdLeftApplication() {
            }

            override fun onRewardedVideoAdFailedToLoad(i: Int) {
                showSnackBar(this@AboutActivity, "Ocorreu um erro carregando o vídeo \uD83D\uDE22", resources.getColor(R.color.material_red500), Color.WHITE, mainContainer)
            }

            override fun onRewardedVideoCompleted() {
                showSnackBar(this@AboutActivity, "Obrigado por nos ajudar, esses anúncios fazem uma diferença enorme para nós!", resources.getColor(R.color.colorPrimary), Color.WHITE, mainContainer)

            }
        }
        loadRewardedVideoAd(rewardedVideoAd)

    }

    private fun loadRewardedVideoAd(rewardedVideoAd: RewardedVideoAd) {
        rewardedVideoAd.loadAd(getString(R.string.video_advertisement_id), AdRequest.Builder().build())
    }


}