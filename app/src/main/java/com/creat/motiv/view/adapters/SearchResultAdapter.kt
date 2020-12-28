package com.creat.motiv.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.creat.motiv.R
import com.creat.motiv.databinding.QuoteHeaderViewBinding
import com.creat.motiv.utilities.gone
import com.creat.motiv.view.binders.QuotesListBinder
import com.creat.motiv.view.binders.UsersListBinder

class SearchResultAdapter(val context: Context, var query: String, val noResultCallback: () -> Unit) : RecyclerView.Adapter<SearchResultAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val quoteHeaderViewBinding: QuoteHeaderViewBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.quote_header_view, parent, false)
        return MyViewHolder(quoteHeaderViewBinding)
    }

    var usersNotFound = false
    var authorsNotFound = false
    var quotesNotFound = false

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.quotescardBinding.title.text = when (position) {
            0 -> "Usuários"
            1 -> "Autores"
            else -> "Posts"
        }
        when (position) {
            0 -> UsersListBinder(context,
                    holder.quotescardBinding.quotesRecycler,
                    useinit = false,
                    noResultCallback = {
                        holder.quotescardBinding.title.gone()
                        usersNotFound = true
                    }
            ).run {
                searchData(query)
            }
            1 -> QuotesListBinder(context,
                    holder.quotescardBinding.quotesRecycler,
                    useinit = false,
                    showEmptyResult = false,
                    noResultCallback = {
                        holder.quotescardBinding.title.gone()
                        authorsNotFound = true
                    }
            ).run {
                this.searchAuthor(query)
            }
            2 -> QuotesListBinder(context,
                    holder.quotescardBinding.quotesRecycler,
                    false,
                    noResultCallback = {
                        holder.quotescardBinding.title.gone()
                        quotesNotFound = true
                    }).run {
                searchData(query)
            }
        }

        notFoundCall()

    }

    private fun notFoundCall() {
        if (usersNotFound && authorsNotFound && quotesNotFound) {
            noResultCallback.invoke()
        }
    }

    override fun getItemCount(): Int {
        return 3
    }

    inner class MyViewHolder(val quotescardBinding: QuoteHeaderViewBinding) : RecyclerView.ViewHolder(quotescardBinding.root)
}