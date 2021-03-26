package com.creat.motiv.quote.view.binder

import android.view.View
import com.creat.motiv.R
import com.creat.motiv.databinding.QuoteRecyclerBinding
import com.creat.motiv.quote.view.adapter.QuoteRecyclerAdapter
import com.ilustris.animations.fadeIn
import com.ilustris.animations.slideInBottom
import com.ilustris.motiv.base.presenter.QuotePresenter
import com.ilustris.motiv.base.beans.Quote
import com.ilustris.motiv.base.beans.SPLASH_QUOTE
import com.silent.ilustriscore.core.model.DTOMessage
import com.silent.ilustriscore.core.utilities.OperationType
import com.silent.ilustriscore.core.view.BaseView
import java.util.*
import kotlin.collections.ArrayList


class QuotesListBinder(override val viewBind: QuoteRecyclerBinding) : BaseView<Quote>() {


    override val presenter = QuotePresenter(this)
    private var quoteRecyclerAdapter: QuoteRecyclerAdapter? = null
    private var showProfile: String? = null


    override fun initView() {
        presenter.loadData()
    }

    override fun showListData(list: List<Quote>) {
        if (list.isEmpty()) {
            setupRecyclerView(listOf(Quote.noResultsQuote()))
        } else {
            setupRecyclerView(list.sortedByDescending { it.data })
            if (viewBind.quotesrecyclerview.visibility == View.GONE) {
                viewBind.quotesrecyclerview.slideInBottom()
            }


        }
    }

    private fun slideNextQuote() {
        quoteRecyclerAdapter?.let {
            viewBind.quotesrecyclerview.postDelayed({
                if (it.quoteList.size > 1 && it.quoteList[0].id == SPLASH_QUOTE && viewBind.quotesrecyclerview.currentItem == 0) {
                    viewBind.quotesrecyclerview.setCurrentItem(1, true)
                }
            }, 5000)
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
        presenter.loadData()
    }

    fun addSplashQuote() {
        setupRecyclerView(listOf(Quote.splashQuote().apply {
            val dateNow = Calendar.getInstance().time
            val calendar = GregorianCalendar()
            calendar.time = dateNow
            author = String.format(context.getString(R.string.company), calendar.get(Calendar.YEAR))
        }))
    }

    fun searchData(query: String) {
        quoteRecyclerAdapter?.filter(query)
    }

    fun getUserQuotes(uid: String) {
        showProfile = uid
        presenter.findPreciseData(uid, "userID")
    }

    fun getFavorites(uid: String) {
        presenter.loadFavorites(uid) { quotes ->
            val favorites = ArrayList<Quote>()
            favorites.addAll(quotes.sortedByDescending { it.data })
            setupRecyclerView(favorites.toList())
        }
    }

    private fun setupRecyclerView(list: List<Quote>) {
        val quotesList: ArrayList<Quote> = ArrayList(list.sortedByDescending { it.data })

        showProfile?.let {
            if (quoteRecyclerAdapter == null) {
                val profileQuote = Quote.profileQuote().apply { userID = it }
                quotesList.add(0, profileQuote)
            }

        }
        viewBind.quotesrecyclerview.run {
            if (quoteRecyclerAdapter == null) {
                quoteRecyclerAdapter = QuoteRecyclerAdapter(quotesList)
                adapter = quoteRecyclerAdapter
            } else {
                quoteRecyclerAdapter?.updateData(quotesList)
            }
            if (visibility == View.GONE) slideInBottom()
        }
        slideNextQuote()
        onLoadFinish()
    }


}