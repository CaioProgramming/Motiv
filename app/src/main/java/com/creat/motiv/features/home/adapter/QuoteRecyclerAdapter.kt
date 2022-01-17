package com.creat.motiv.features.home.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.creat.motiv.R
import com.creat.motiv.databinding.ProfileQuoteCardBinding
import com.creat.motiv.databinding.QuoteAdvertiseLayoutBinding
import com.creat.motiv.databinding.UsersPageCardBinding
import com.creat.motiv.features.profile.users.UserListRecyclerAdapter
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.ilustris.animations.*
import com.ilustris.motiv.base.beans.*
import com.ilustris.motiv.base.databinding.QuotesCardBinding
import com.ilustris.motiv.base.utils.*
import com.silent.ilustriscore.core.utilities.delayedFunction
import com.silent.ilustriscore.core.utilities.gone
import java.util.*

private const val QUOTE_VIEW = 0
private const val ADVERTISE_QUOTE = 1
private const val USER_PROFILE_QUOTE = 2
private const val VIEW_USERS_QUOTE = 3

enum class QuoteAction {
    OPTIONS, LIKE, USER
}

class QuoteRecyclerAdapter(
    private val quoteAdapterList: ArrayList<QuoteAdapterData>,
    private val onSelectQuote: (QuoteAdapterData, QuoteAction) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun refreshData(quoteAdapterData: QuoteAdapterData) {
        quoteAdapterList.add(quoteAdapterData)
        notifyItemInserted(itemCount)
    }

    fun loadOnNextPage(quoteAdapterData: QuoteAdapterData, position: Int) {
        quoteAdapterList.add(position + 1, quoteAdapterData)
        notifyItemInserted(position + 1)
    }

    fun getQuote(position: Int) = quoteAdapterList[position]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val context = parent.context
        return when (viewType) {
            QUOTE_VIEW -> {
                QuoteViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.quotes_card, parent, false)
                )
            }
            USER_PROFILE_QUOTE -> {
                ProfileViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.profile_quote_card, parent, false)
                )
            }
            VIEW_USERS_QUOTE -> {
                ViewUsersHolder(
                    LayoutInflater.from(context).inflate(R.layout.users_page_card, parent, false)
                )
            }
            else -> {
                AdViewHolder(
                    LayoutInflater.from(context)
                        .inflate(R.layout.quote_advertise_layout, parent, false)
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int = when (quoteAdapterList[position].quote.id) {
        AD_QUOTE -> ADVERTISE_QUOTE
        PROFILE_QUOTE -> USER_PROFILE_QUOTE
        USERS_QUOTE -> VIEW_USERS_QUOTE
        else -> QUOTE_VIEW
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is QuoteViewHolder -> holder.bind()
            is ProfileViewHolder -> holder.bind()
            is ViewUsersHolder -> holder.bind()
            is AdViewHolder -> holder.setupAd()

        }
    }

    override fun getItemCount(): Int = quoteAdapterList.size

    inner class QuoteViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind() {
            QuotesCardBinding.bind(view).run {
                setupData()
            }
        }

        private fun QuotesCardBinding.setupUser(user: User) {
            userpic.loadImage(user.picurl)
            username.text = user.name
        }

        private fun QuotesCardBinding.setupStyle(style: Style) {
            val context = root.context
            quoteBack.loadGif(style.backgroundURL) {
                quoteBack.fadeIn()
            }
            quoteTextView.defineTextAlignment(style.textAlignment)
            authorTextView.defineTextAlignment(style.textAlignment)
            quoteTextView.setTextColor(Color.parseColor(style.textColor))
            authorTextView.setTextColor(Color.parseColor(style.textColor))
            quoteTextView.setShadowLayer(
                style.shadowStyle.radius,
                style.shadowStyle.dx,
                style.shadowStyle.dy,
                Color.parseColor(style.shadowStyle.shadowColor)
            )

            FontUtils.getTypeFace(context, style.font)?.let {
                quoteTextView.setTypeface(it, style.fontStyle.getTypefaceStyle())
                authorTextView.setTypeface(it, style.fontStyle.getTypefaceStyle())

            }
        }

        private fun QuotesCardBinding.setupData() {
            val quoteData = quoteAdapterList[bindingAdapterPosition]
            quoteTextView.text = quoteData.quote.quote
            authorTextView.text = quoteData.quote.author
            like.isChecked = quoteData.quote.likes.contains(quoteData.currentUser?.uid)
            optionsButton.setOnClickListener {
                onSelectQuote(quoteData, QuoteAction.OPTIONS)
            }
            like.setOnCheckedChangeListener { buttonView, isChecked ->
                onSelectQuote(quoteData, QuoteAction.LIKE)
            }
            setupStyle(quoteData.style)
            setupUser(quoteData.user)
            quoteDate.text = TextUtils.data(quoteData.quote.data)
            userTop.fadeIn()
            quoteTextView.popIn()
            authorTextView.popIn()
            userTop.setOnClickListener {
                onSelectQuote(quoteData, QuoteAction.USER)
            }
            likersRecycler.adapter = quoteData.likers?.let {
                LikersAdapter(it) {
                    onSelectQuote(quoteData, QuoteAction.USER)
                }
            }
            if (!quoteData.quote.isUserQuote()) {
                userTop.gone()
            }
        }
    }


    inner class ProfileViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind() {
            ProfileQuoteCardBinding.bind(itemView).run {
                val quoteAdapterData = quoteAdapterList[bindingAdapterPosition]
                userBackground.loadGif(quoteAdapterData.user.cover)
                username.text = quoteAdapterData.user.name
                profilepic.loadImage(quoteAdapterData.user.picurl)
            }
        }
    }

    inner class ViewUsersHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind() {
            UsersPageCardBinding.bind(itemView).run {
                val quoteAdapterData = quoteAdapterList[bindingAdapterPosition]
                usersRecycler.adapter = quoteAdapterData.users?.let {
                    UserListRecyclerAdapter(it) { selectedUser ->
                        quoteAdapterData.user = selectedUser
                        onSelectQuote(quoteAdapterData, QuoteAction.USER)
                    }
                }
            }
        }
    }

    inner class AdViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val context = view.context

        fun setupAd() {
            QuoteAdvertiseLayoutBinding.bind(itemView).run {
                val quoteAdapterData = quoteAdapterList[bindingAdapterPosition]
                advertiseImage.loadGif(AD_GIF)
                delayedFunction(3000) {
                    quoteAdapterData.advertise?.let { setupAd(it) }
                }

            }
        }

        private fun QuoteAdvertiseLayoutBinding.setupAd(ad: UnifiedNativeAd) {
            Glide.with(context).load(ad.icon.drawable).into(userTop.userpic)
            adView.run {
                headlineView = adHeadline
                advertiserView = userTop.root
                adChoicesView = adChoices
                bodyView = adBody
                starRatingView = appRating
                callToActionView = adCard
                iconView = userTop.userpic
                mediaView = adMediaView

            }

            adMediaView.apply {
                setMediaContent(ad.mediaContent)
                setImageScaleType(ImageView.ScaleType.CENTER_CROP)
            }
            appRating.isEnabled = false
            appRating.numStars = ad.starRating.toInt()
            adHeadline.text = ad.headline
            adView.slideInBottom()
            loading.fadeOut()
            if (ad.images.isNotEmpty()) {
                if (ad.images.size > 1) {
                    Glide.with(context).load(ad.images[Random().nextInt(ad.images.size - 1)].uri)
                        .into(advertiseImage)
                } else {
                    Glide.with(context).load(ad.images[0].uri).into(advertiseImage)
                }

            }
        }
    }


}
