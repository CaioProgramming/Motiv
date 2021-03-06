package com.creat.motiv.view.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.creat.motiv.R
import com.creat.motiv.databinding.QuoteAdvertiseLayoutBinding
import com.creat.motiv.databinding.QuotesCardBinding
import com.creat.motiv.model.beans.Quote
import com.creat.motiv.utilities.AD_QUOTE
import com.creat.motiv.utilities.Tools
import com.creat.motiv.view.binders.QuoteCardBinder
import java.util.*
import kotlin.collections.ArrayList


class QuoteRecyclerAdapter(var quoteList: ArrayList<Quote>,
                           val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val QUOTE_VIEW = 0
    private val AD_VIEW = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == QUOTE_VIEW) {
            val quotescardBinding: QuotesCardBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.quotes_card, parent, false)
            QuoteViewHolder(quotescardBinding)
        } else {
            val advertiseLayoutBinding: QuoteAdvertiseLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.quote_advertise_layout, parent, false)
            AdViewHolder(advertiseLayoutBinding)
        }
    }

    override fun getItemViewType(position: Int): Int = if (quoteList[position].id == AD_QUOTE) AD_VIEW else QUOTE_VIEW

    fun addData(quotes: List<Quote>) {
        quoteList = ArrayList(quotes)

        var swapIndex = Random().nextInt(quoteList.size)
        if (swapIndex == 0) {
            swapIndex = Random().nextInt(quoteList.size)
        }
        val quote = quoteList[swapIndex]

        quoteList.add(swapIndex, Quote.advertise_quote())

        notifyDataSetChanged()
    }

    fun checkIfQuotesExists(id: String): Boolean {
        quoteList.forEach {
            return it.id == id
        }
        return false
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == QUOTE_VIEW) {
            (holder as QuoteViewHolder).run {
                if (quoteList[position].id != AD_QUOTE) QuoteCardBinder(quoteList[position], context, quotescardBinding)
            }
        } else {
            holder as AdViewHolder
        }
    }

    override fun getItemCount(): Int = quoteList.size

    inner class QuoteViewHolder(val quotescardBinding: QuotesCardBinding) : RecyclerView.ViewHolder(quotescardBinding.root)

    inner class AdViewHolder(advertiseBind: QuoteAdvertiseLayoutBinding) : RecyclerView.ViewHolder(advertiseBind.root) {
        init {
            Log.i(javaClass.simpleName, "Criando card de anúncio!")
            Tools.loadAd(context, advertiseBind)
        }
    }

}