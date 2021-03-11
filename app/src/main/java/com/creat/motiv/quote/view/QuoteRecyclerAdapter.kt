package com.creat.motiv.quote.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.creat.motiv.R
import com.creat.motiv.databinding.ProfileQuoteCardBinding
import com.creat.motiv.databinding.QuoteAdvertiseLayoutBinding
import com.creat.motiv.databinding.QuotesCardBinding
import com.creat.motiv.quote.beans.AD_QUOTE
import com.creat.motiv.quote.beans.PROFILE_QUOTE
import com.creat.motiv.quote.beans.Quote
import com.creat.motiv.utilities.Tools
import com.creat.motiv.profile.view.binders.ProfilePageBinder
import com.creat.motiv.quote.view.binder.QuoteCardBinder
import java.util.*
import kotlin.collections.ArrayList


class QuoteRecyclerAdapter(var quoteList: ArrayList<Quote>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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

    fun addData(quotes: List<Quote>, clearFirst: Boolean = false) {

        quoteList.addAll(quotes)

        var swapIndex = Random().nextInt(quoteList.size)
        if (swapIndex == 0) {
            swapIndex = Random().nextInt(quoteList.size)
        }

        val actualQuote = quoteList[swapIndex]
        if (actualQuote.isUserQuote()) {
            quoteList.add(swapIndex, Quote.advertiseQuote())
        }

        notifyDataSetChanged()
    }

    fun checkIfQuotesExists(id: String): Boolean {
        quoteList.forEach {
            return it.id == id
        }
        return false
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when {
            getItemViewType(position) == quoteView -> {
                (holder as QuoteViewHolder).run {
                    if (quoteList[position].id != AD_QUOTE) {
                        bind(quoteList[position])
                    }
                }
            }
            getItemViewType(position) == profileView -> {
                (holder as ProfileViewHolder).run {
                    bind(quoteList[position].userID)
                }
            }
            else -> {
                holder as AdViewHolder
            }
        }
    }

    override fun getItemCount(): Int = quoteList.size

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

    inner class AdViewHolder(advertiseBind: QuoteAdvertiseLayoutBinding) : RecyclerView.ViewHolder(advertiseBind.root) {
        init {
            Log.i(javaClass.simpleName, "Criando card de anúncio!")
            Tools.loadAd(advertiseBind.root.context, advertiseBind)
        }
    }

}