package com.creat.motiv.quote.view.binder

import android.view.View
import com.creat.motiv.R
import com.creat.motiv.databinding.QuoteRecyclerBinding
import com.ilustris.motiv.base.presenter.QuotePresenter
import com.creat.motiv.quote.view.adapter.QuoteRecyclerAdapter
import com.ilustris.animations.slideUp
import com.ilustris.motiv.base.beans.Quote
import com.silent.ilustriscore.core.model.DTOMessage
import com.silent.ilustriscore.core.utilities.OperationType
import com.silent.ilustriscore.core.utilities.gone
import com.silent.ilustriscore.core.utilities.visible
import com.silent.ilustriscore.core.view.BaseView
import java.util.*
import kotlin.collections.ArrayList


class QuotesListBinder(override val viewBind: QuoteRecyclerBinding, useInit: Boolean = true) : BaseView<Quote>(useInit) {


    override val presenter = QuotePresenter(this)
    private var quoteRecyclerAdapter: QuoteRecyclerAdapter? = null
    var showProfile: String? = null

    init {
        if (useInit) {
            initView()
        }
    }

    override fun initView() {
        presenter.loadData()
    }

    override fun showListData(list: List<Quote>) {
        if (list.isEmpty()) {
            setupRecyclerView(listOf(Quote.noResultsQuote()))
        } else {
            setupRecyclerView(list)
            viewBind.quotesrecyclerview.visible()
        }
    }

    override fun getCallBack(dtoMessage: DTOMessage) {
        super.getCallBack(dtoMessage)
        if (dtoMessage.operationType == OperationType.DATA_SAVED) {
            initView()
        }
    }

    fun addSearchQuote() {
        setupRecyclerView(listOf(Quote.searchQuote()))
    }

    fun addSplashQuote() {
        setupRecyclerView(listOf(Quote.splashQuote().apply {
            val dateNow = Calendar.getInstance().time
            val calendar = GregorianCalendar()
            calendar.time = dateNow
            author = String.format(context.getString(R.string.company), calendar.get(Calendar.YEAR))
        }))
    }


    override fun onLoadFinish() {
        super.onLoadFinish()
        if (viewBind.quotesrecyclerview.visibility == View.GONE) {
            viewBind.quotesrecyclerview.slideUp()
        }
    }

    override fun onLoading() {
        super.onLoading()
        viewBind.quotesrecyclerview.gone()
    }

    fun searchData(query: String) {
        presenter.queryData(query, "quote")
    }

    fun searchAuthor(query: String) {
        presenter.queryData(query, "author")
    }

    fun getUserQuotes(uid: String) {
        presenter.findPreciseData(uid, "userID")
    }

    fun getFavorites(uid: String) {
        presenter.loadFavorites(uid)
    }

    private fun setupRecyclerView(list: List<Quote>) {
        val quotesList: ArrayList<Quote> = ArrayList(list)

        showProfile?.let {
            if (quoteRecyclerAdapter == null) {
                val profileQuote = Quote.profileQuote().apply { userID = it }
                quotesList.add(0, profileQuote)
                val favoriteQuote = Quote.favoritesQuote()
                quotesList.add(favoriteQuote)
                presenter.loadFavorites(it)
            }

        }
        viewBind.quotesrecyclerview.run {
            if (quoteRecyclerAdapter == null) {
                quoteRecyclerAdapter = QuoteRecyclerAdapter(quotesList)
                adapter = quoteRecyclerAdapter
            } else {
                quoteRecyclerAdapter?.addData(quotesList)
            }
        }

    }


}