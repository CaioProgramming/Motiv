package com.creat.motiv.features.about

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.creat.motiv.R
import com.creat.motiv.databinding.FragmentAboutBinding
import com.creat.motiv.features.about.data.AboutData
import com.creat.motiv.view.adapters.AboutAdapter
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.ilustris.animations.fadeIn
import com.ilustris.animations.fadeOut
import com.ilustris.animations.slideInBottom
import com.ilustris.animations.slideInRight
import com.ilustris.motiv.base.utils.*
import com.silent.ilustriscore.core.model.ViewModelBaseState
import com.silent.ilustriscore.core.utilities.showSnackBar
import java.util.ArrayList

class AboutFragment : Fragment() {
    var aboutBinding: FragmentAboutBinding? = null
    private var rewardedAd: RewardedAd? = null
    private val aboutViewModel = AboutViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        aboutBinding = FragmentAboutBinding.inflate(inflater, container, false)
        return aboutBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        aboutBinding?.setupView()
        observeViewModel()
        aboutViewModel.getAboutScreen()
    }

    private fun observeViewModel() {
        aboutViewModel.aboutViewState.observe(this, {
            when (it) {
                is AboutViewModel.AboutViewState.AboutDataRetrieved -> setupAbout(it.aboutData)
            }
        })
        aboutViewModel.viewModelState.observe(this, {
            when (it) {
                ViewModelBaseState.LoadingState -> TODO()
                is ViewModelBaseState.ErrorState -> {
                    view?.showSnackBar("Ocorreu um erro inesperado ao carregar")
                }
            }
        })
    }

    private fun setupAbout(aboutData: ArrayList<AboutData>) {
        aboutBinding?.aboutRecycler?.run {
            adapter = AboutAdapter(aboutData)
            slideInBottom()
        }
    }

    private fun requestAd() {
        val request = AdRequest.Builder().build()
        RewardedAd.load(requireContext(), getString(R.string.video_advertisement_id), request,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    aboutBinding?.supportDevs?.fadeOut()
                }

                override fun onAdLoaded(rAd: RewardedAd) {
                    super.onAdLoaded(rewardedAd)
                    rewardedAd = rAd
                    aboutBinding?.supportDevs?.slideInRight()
                }
            })
    }

    private fun FragmentAboutBinding.setupView() {
        companyText.setOnClickListener {
            val uri = Uri.parse("https://play.google.com/store/apps/dev?id=8106172357045720296")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
        instagramText.apply {
            typeface = FontUtils.getTypeFace(requireContext(), 16)
            followUsCard.setOnClickListener {
                val uri = Uri.parse("https://www.instagram.com/motivbr/")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }
        }
        supportDevs.setOnClickListener {
            rewardedAd?.show(requireActivity()) {
                view?.showSnackBar(
                    "Obrigado por nos ajudar, você faz com que o motiv possa crescer a cada dia!",
                    resources.getColor(R.color.colorPrimaryDark),
                    Color.WHITE
                )
            }
        }
        requestAd()
    }

    override fun onDestroy() {
        super.onDestroy()
        aboutBinding = null
    }

}


