package com.creat.motiv.quote.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.creat.motiv.R
import com.creat.motiv.databinding.ProfileQuoteCardBinding
import com.creat.motiv.databinding.QuoteAdvertiseLayoutBinding
import com.creat.motiv.profile.view.binders.ProfilePageBinder
import com.creat.motiv.quote.view.binder.QuoteCardBinder
import com.creat.motiv.utilities.AD_GIF
import com.creat.motiv.utilities.AdvertiseHelper
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.ilustris.animations.*
import com.ilustris.motiv.base.beans.AD_QUOTE
import com.ilustris.motiv.base.beans.PROFILE_QUOTE
import com.ilustris.motiv.base.beans.Quote
import com.ilustris.motiv.base.beans.User
import com.ilustris.motiv.base.databinding.QuotesCardBinding
import com.silent.ilustriscore.core.utilities.gone
import java.util.*
import kotlin.collections.ArrayList


class QuoteRecyclerAdapter(val quoteList: ArrayList<Quote>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var viewList = quoteList.toList()
    private val quoteView = 0
    private val advertiseView = 1
    private val profileView = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val context = parent.context
        return when (viewType) {
            quoteView -> {
                val quotesCardBinding: QuotesCardBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.quotes_card, parent, false)
                QuoteViewHolder(quotesCardBinding)
            }
            profileView -> {
                val profileQuoteCardBinding: ProfileQuoteCardBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.profile_quote_card, parent, false)
                ProfileViewHolder(profileQuoteCardBinding)
            }
            else -> {
                val advertiseLayoutBinding: QuoteAdvertiseLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.quote_advertise_layout, parent, false)
                AdViewHolder(advertiseLayoutBinding)
            }
        }
    }

    override fun getItemViewType(position: Int): Int = if (quoteList[position].id == AD_QUOTE) advertiseView else if (quoteList[position].id == PROFILE_QUOTE) profileView else quoteView

    fun addData(quotes: List<Quote>) {

        quoteList.addAll(quotes)

        var swapIndex = Random().nextInt(quoteList.size)
        if (swapIndex == 0) {
            swapIndex = Random().nextInt(quoteList.size)
        }

        val actualQuote = quoteList[swapIndex]
        if (actualQuote.isUserQuote()) {
            quoteList.add(swapIndex, Quote.advertiseQuote())
        }
        viewList = quoteList
        notifyDataSetChanged()
    }

    fun filter(query: String) {
        viewList = quoteList.filter {
            it.quote.contains(query, true) || it.author.contains(query, true)
        }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when {
            getItemViewType(position) == quoteView -> {
                (holder as QuoteViewHolder).run {
                    if (viewList[position].id != AD_QUOTE) {
                        bind(viewList[position])
                    }
                }
            }
            getItemViewType(position) == profileView -> {
                (holder as ProfileViewHolder).run {
                    bind(viewList[position].userID)
                }
            }
            else -> {
                holder as AdViewHolder
            }
        }
    }

    override fun getItemCount(): Int = viewList.size

    inner class QuoteViewHolder(private val quotescardBinding: QuotesCardBinding) : RecyclerView.ViewHolder(quotescardBinding.root) {
        fun bind(quote: Quote) {
            QuoteCardBinder(quote, quotescardBinding)
        }
    }

    inner class ProfileViewHolder(val profileQuoteCardBinding: ProfileQuoteCardBinding) : RecyclerView.ViewHolder(profileQuoteCardBinding.root) {
        fun bind(uid: String) {
            ProfilePageBinder(profileQuoteCardBinding, uid).initView()
        }
    }

    inner class AdViewHolder(val advertiseBind: QuoteAdvertiseLayoutBinding) : RecyclerView.ViewHolder(advertiseBind.root) {
        val context = advertiseBind.root.context

        init {
            Log.i(javaClass.simpleName, "Criando card de anúncio!")
            Glide.with(context).asGif().centerCrop().load(AD_GIF).into(advertiseBind.advertiseGif)
            advertiseBind.loading.repeatFade()
            AdvertiseHelper(context, context.resources.getString(R.string.feed_advertisement_id)).loadAd(::setupAd) {
                advertiseBind.loading.text = "Ocorreu um erro ao carregar\n:("
            }
        }

        private fun setupAd(ad: UnifiedNativeAd) {
            advertiseBind.run {
                if (ad.icon != null) {
                    Glide.with(context).load(ad.icon.drawable).into(userTop.userpic)
                } else {
                    userTop.userpic.gone()
                }

                adView.run {
                    setNativeAd(ad)
                    headlineView = adHeadline
                    advertiserView = userTop.userContainer
                    adChoicesView = adChoices
                    bodyView = adBody
                    starRatingView = appRating
                    callToActionView = adCard
                    iconView = userTop.userpic
                    mediaView = adAppMedia

                }

                userTop.userData = User(name = ad.advertiser ?: "Anunciante não identificado")
                adAppMedia.apply {
                    setMediaContent(ad.mediaContent)
                    setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                }
                appRating.isEnabled = false
                advertise = ad
                adView.slideInBottom()
                loading.popOut()
            }
        }
    }

}