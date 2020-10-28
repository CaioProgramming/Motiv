package com.creat.motiv.view.binders

import android.app.Activity
import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.creat.motiv.databinding.QuoteRecyclerBinding
import com.creat.motiv.model.beans.Quote
import com.creat.motiv.presenter.QuotePresenter
import com.creat.motiv.utilities.*
import com.creat.motiv.view.BaseView
import com.creat.motiv.view.adapters.RecyclerAdapter

class QuotesListBinder(override val context: Context, override val viewBind: QuoteRecyclerBinding, useinit: Boolean = true) : BaseView<Quote>(useinit) {


    override fun presenter() = QuotePresenter(this)
    val quoteadapter = RecyclerAdapter(ArrayList(), context)

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
        viewBind.loading.fadeIn().subscribe()
    }

    override fun onLoadFinish() {
        viewBind.loading.fadeOut().subscribe()
    }


    fun searchData(query: String) {
        if (query.isBlank()) {
            Alert(context as Activity).snackmessage(message = "Não pesquisou nada")
            presenter().loadData()
        } else {
            presenter().findQuote(query)
        }
    }

    fun getUserQuotes(uid: String) {
        presenter().queryData(uid, "userID")
    }

    fun getFavorites(uid: String) {
        presenter().loadFavorites(uid)
    }


    override fun showListData(list: List<Quote>) {
        if (list.isEmpty()) {
            viewBind.quotesrecyclerview.gone()
            viewBind.emptyList.fadeIn()
        } else {
            viewBind.emptyList.fadeOut()
            viewBind.quotesrecyclerview.visible()
        }
        quoteadapter.quoteList = list.reversed()
        quoteadapter.notifyDataSetChanged()
        onLoadFinish()
    }

    override fun initView() {
        presenter().loadData()
    }

}