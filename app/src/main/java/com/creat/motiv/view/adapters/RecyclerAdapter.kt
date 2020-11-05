package com.creat.motiv.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.creat.motiv.R
import com.creat.motiv.databinding.QuotescardBinding
import com.creat.motiv.model.beans.Quote
import com.creat.motiv.utilities.repeatFade
import com.creat.motiv.view.binders.QuoteCardBinder


class RecyclerAdapter(var quoteList: List<Quote> = emptyList(),
                      val context: Context) : RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {

    private var insideList = quoteList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val quotescardBinding: QuotescardBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.quotescard, parent, false)
        return MyViewHolder(quotescardBinding)
    }


    fun addData(quotes: List<Quote>) {
        quoteList = quotes
        insideList = quoteList.sortedByDescending {
            it.data
        }
        notifyDataSetChanged()
    }

    fun searchQuote(query: String) {
        if (query.isBlank()) {
            addData(quoteList)
        } else {
            val newList = quoteList.filter {
                it.quote.contains(query, true) || it.author.contains(query, true)
            }
            insideList = newList
        }
        notifyDataSetChanged()

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        if (insideList.isNotEmpty()) {
            QuoteCardBinder(quoteList[position], context, holder.quotescardBinding)
        } else {
            holder.quotescardBinding.quoteCard.repeatFade()
        }

    }

    override fun getItemCount(): Int = insideList.size


    class MyViewHolder(val quotescardBinding: QuotescardBinding) : RecyclerView.ViewHolder(quotescardBinding.root)
}