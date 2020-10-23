package com.creat.motiv.view.binders

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.creat.motiv.databinding.QuoteRecyclerBinding
import com.creat.motiv.model.beans.Quote
import com.creat.motiv.presenter.QuotePresenter
import com.creat.motiv.view.BaseView
import com.creat.motiv.view.adapters.RecyclerAdapter

class QuotesListBinder(override val context: Context, override val viewBind: QuoteRecyclerBinding, val favorites: Boolean = false) : BaseView<Quote>() {


    override fun presenter() = QuotePresenter(this)
    var searchQuery: String? = null
    var currentField = 0
    val fields = listOf("quote", "author", "username")

    init {
        initView()
    }

    override fun onLoading() {
        viewBind.notfound.animate()
    }

    override fun onLoadFinish() {
        viewBind.notfound.clearAnimation()
    }


    fun searchData(query: String) {
        searchQuery = query
        searchQuery?.let { presenter().queryData(it, fields[currentField]) }
    }


    override fun showListData(list: List<Quote>) {
        if (list.isNotEmpty()) {
            viewBind.quotesrecyclerview.run {
                adapter = RecyclerAdapter(list.reversed(), context)
                layoutManager = GridLayoutManager(context, 1, VERTICAL, false)
                setHasFixedSize(true)
                viewBind.notfound.visibility = View.GONE
            }
        } else {
            if (searchQuery != null && currentField != fields.lastIndex) {
                currentField++
                searchQuery?.let {
                    searchData(it)
                }
            }
        }
    }

    override fun initView() {
        if (!favorites) {
            presenter().loadData()
        } else {
            presenter().loadFavorites()
        }
    }

}