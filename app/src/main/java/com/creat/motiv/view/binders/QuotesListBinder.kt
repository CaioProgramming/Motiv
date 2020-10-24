package com.creat.motiv.view.binders

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.creat.motiv.databinding.QuoteRecyclerBinding
import com.creat.motiv.model.beans.Quote
import com.creat.motiv.presenter.QuotePresenter
import com.creat.motiv.utils.gone
import com.creat.motiv.utils.visible
import com.creat.motiv.view.BaseView
import com.creat.motiv.view.adapters.RecyclerAdapter

class QuotesListBinder(override val context: Context, override val viewBind: QuoteRecyclerBinding, useinit: Boolean = true) : BaseView<Quote>(useinit) {


    override fun presenter() = QuotePresenter(this)
    val quoteadapter = RecyclerAdapter(ArrayList(), context)
    var searchQuery: String? = null
    var currentField = 0
    val fields = listOf("quote", "author", "username")

    init {
        viewBind.quotesrecyclerview.run {
            adapter = quoteadapter
            layoutManager = GridLayoutManager(context, 1, VERTICAL, false)
            setHasFixedSize(true)
        }
        if (useInit) {
            initView()
        }
    }

    override fun onLoading() {
        viewBind.loading.visible()
    }

    override fun onLoadFinish() {
        viewBind.loading.gone()
    }


    fun searchData(query: String) {
        searchQuery = query
        searchQuery?.let { presenter().queryData(it, fields[currentField]) }
    }

    fun getUserQuotes(uid: String) {
        presenter().queryData(uid, "userID")
    }

    fun getFavorites(uid: String) {
        presenter().loadFavorites(uid)
    }


    override fun showListData(list: List<Quote>) {
        quoteadapter.quoteList = list.reversed()
        quoteadapter.notifyItemRangeChanged(0, list.size)
        if (searchQuery != null && list.isEmpty()) {
            if (currentField != fields.lastIndex) {
                currentField++
                searchQuery?.let {
                    searchData(it)
                }
            }
        }
    }

    override fun initView() {
        presenter().loadData()
    }

}