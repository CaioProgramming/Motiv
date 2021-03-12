package com.ilustris.motiv.manager.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ilustris.motiv.base.beans.AD_QUOTE
import com.ilustris.motiv.base.beans.Quote
import com.ilustris.motiv.base.databinding.QuotesCardBinding
import com.ilustris.motiv.manager.R
import com.ilustris.motiv.manager.ui.home.binder.QuoteManagerCardBinder
import java.util.*
import kotlin.collections.ArrayList


class QuoteManagerAdapter(var quoteList: ArrayList<Quote>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val context = parent.context
        val quotesCardBinding: QuotesCardBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.quotes_card, parent, false)
        return QuoteViewHolder(quotesCardBinding)
    }


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

        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as QuoteViewHolder).run {
            if (quoteList[position].id != AD_QUOTE) {
                bind(quoteList[position])
            }
        }
    }

    override fun getItemCount(): Int = quoteList.size

    inner class QuoteViewHolder(private val quotescardBinding: QuotesCardBinding) : RecyclerView.ViewHolder(quotescardBinding.root) {
        fun bind(quote: Quote) {
            QuoteManagerCardBinder(quote, quotescardBinding)
        }
    }

}