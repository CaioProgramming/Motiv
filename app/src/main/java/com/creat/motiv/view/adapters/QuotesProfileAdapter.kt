package com.creat.motiv.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.creat.motiv.R
import com.creat.motiv.databinding.QuoteHeaderViewBinding
import com.creat.motiv.view.binders.QuotesListBinder

class QuotesProfileAdapter(val context: Context, val uid: String) : RecyclerView.Adapter<QuotesProfileAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val quoteHeaderViewBinding: QuoteHeaderViewBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.quote_header_view, parent, false)
        return MyViewHolder(quoteHeaderViewBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.quotescardBinding.title.text = if (position == 0) "Posts" else "Favoritos"
        QuotesListBinder(context, holder.quotescardBinding.quotesRecycler, false).run {
            if (position == 0) {
                getUserQuotes(uid)
            } else {
                getFavorites(uid)
            }
        }
    }

    override fun getItemCount(): Int {
        return 2
    }


    class MyViewHolder(val quotescardBinding: QuoteHeaderViewBinding) : RecyclerView.ViewHolder(quotescardBinding.root)


}