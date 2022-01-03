package com.creat.motiv.quote.view.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.creat.motiv.R
import com.creat.motiv.databinding.ProfileQuoteCardBinding
import com.creat.motiv.databinding.QuoteAdvertiseLayoutBinding
import com.creat.motiv.databinding.UsersPageCardBinding
import com.creat.motiv.profile.view.binders.ProfilePageBinder
import com.creat.motiv.profile.view.binders.UserPageBinder
import com.ilustris.motiv.base.utils.AD_GIF
import com.creat.motiv.utilities.AdvertiseHelper
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.ilustris.animations.*
import com.ilustris.motiv.base.beans.*
import com.ilustris.motiv.base.databinding.QuotesCardBinding
import com.ilustris.motiv.base.utils.loadGif
import com.silent.ilustriscore.core.utilities.gone
import java.util.*
import kotlin.collections.ArrayList

private const val QUOTE_VIEW = 0
private const val ADVERTISE_QUOTE = 1
private const val USER_PROFILE_QUOTE = 2
private const val VIEW_USERS_QUOTE = 3

class QuoteRecyclerAdapter(val quoteList: ArrayList<QuoteData>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var viewList = quoteList.toList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val context = parent.context
        return when (viewType) {
            QUOTE_VIEW -> {
                val quotesCardBinding: QuotesCardBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(context),
                    R.layout.quotes_card,
                    parent,
                    false
                )
                QuoteViewHolder(quotesCardBinding)
            }
            USER_PROFILE_QUOTE -> {
                val profileQuoteCardBinding: ProfileQuoteCardBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.profile_quote_card, parent, false)
                ProfileViewHolder(profileQuoteCardBinding)
            }
            VIEW_USERS_QUOTE -> {
                val userCardBinding: UsersPageCardBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.users_page_card, parent, false)
                ViewUsersHolder(userCardBinding)
            }
            else -> {
                val advertiseLayoutBinding: QuoteAdvertiseLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.quote_advertise_layout, parent, false)
                AdViewHolder(advertiseLayoutBinding)
            }
        }
    }

    override fun getItemViewType(position: Int): Int = when (quoteList[position].id) {
        AD_QUOTE -> ADVERTISE_QUOTE
        PROFILE_QUOTE -> USER_PROFILE_QUOTE
        USERS_QUOTE -> VIEW_USERS_QUOTE
        else -> QUOTE_VIEW
    }

    fun updateData(quotes: List<Quote>) {
        val firstQuote = quoteList[0]
        quoteList.clear()
        quoteList.addAll(quotes)
        if (!firstQuote.isUserQuote()) quoteList.add(0, firstQuote)
        var swapIndex = Random().nextInt(quoteList.size)
        if (swapIndex == 0) {
            swapIndex = Random().nextInt(quoteList.size)
        }

        val actualQuote = quoteList[swapIndex]
        if (actualQuote.isUserQuote()) {
            quoteList.add(swapIndex, Quote.advertiseQuote())
        }

        if (quoteList[0].id == SPLASH_QUOTE) {
            quoteList.add(quoteList.size / 2, Quote.usersQuote())
            if (quoteList.size > 2) {
                quoteList[0].quote = "Motiv\n${quoteList.size} publicações"
            }
        }

        viewList = quoteList.toList()
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
            getItemViewType(position) == QUOTE_VIEW -> {
                (holder as QuoteViewHolder).run {
                    if (viewList[position].id != AD_QUOTE) {
                        bind(viewList[position])
                    }
                }
            }
            getItemViewType(position) == USER_PROFILE_QUOTE -> {
                (holder as ProfileViewHolder).run {
                    bind(viewList[position].userID)
                }
            }
            getItemViewType(position) == VIEW_USERS_QUOTE -> {
                (holder as ViewUsersHolder).run {
                    bind()
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

    inner class ViewUsersHolder(val usersQuoteCardBinding: UsersPageCardBinding) : RecyclerView.ViewHolder(usersQuoteCardBinding.root) {
        fun bind() {
            UserPageBinder(usersQuoteCardBinding).initView()
        }
    }

    inner class AdViewHolder(val advertiseBind: QuoteAdvertiseLayoutBinding) : RecyclerView.ViewHolder(advertiseBind.root) {
        val context = advertiseBind.root.context

        init {
            Log.i(javaClass.simpleName, "Criando card de anúncio!")
            advertiseBind.advertiseGif.loadGif(AD_GIF)
            AdvertiseHelper(context, context.resources.getString(R.string.feed_advertisement_id)).loadAd(::setupAd) {
                advertiseBind.loading.text = "Ocorreu um erro ao carregar\n:("
                advertiseBind.loading.popIn()
            }
        }

        private fun setupAd(ad: UnifiedNativeAd) {
            advertiseBind.run {
                if (ad.icon != null) {
                    Glide.with(context).load(ad.icon.drawable).into(userpic)
                } else {
                    userpic.gone()
                }

                adView.run {
                    setNativeAd(ad)
                    headlineView = adHeadline
                    advertiserView = userContainer
                    adChoicesView = adChoices
                    bodyView = adBody
                    starRatingView = appRating
                    callToActionView = adCard
                    iconView = userpic
                    mediaView = adAppMedia

                }

                username.setTextColor(ContextCompat.getColor(context, R.color.md_white))
                adAppMedia.apply {
                    setMediaContent(ad.mediaContent)
                    setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                }
                appRating.isEnabled = false
                advertise = ad
                adView.slideInBottom()
                loading.popOut()
                if (ad.images.isNotEmpty()) {
                    if (ad.images.size > 1) {
                        Glide.with(context).load(ad.images[Random().nextInt(ad.images.size - 1)].uri).into(advertiseGif)
                    } else {
                        Glide.with(context).load(ad.images[0].uri).into(advertiseGif)
                    }

                }
            }
        }
    }

}