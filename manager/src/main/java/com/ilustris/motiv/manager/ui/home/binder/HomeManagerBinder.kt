package com.ilustris.motiv.manager.ui.home.binder

import android.view.View
import com.ilustris.animations.fadeIn
import com.ilustris.motiv.base.presenter.QuotePresenter
import com.ilustris.motiv.base.beans.Quote
import com.ilustris.motiv.manager.databinding.FragmentManagerHomeBinding
import com.ilustris.motiv.manager.ui.home.QuoteManagerAdapter
import com.silent.ilustriscore.core.model.DTOMessage
import com.silent.ilustriscore.core.utilities.OperationType
import com.silent.ilustriscore.core.utilities.gone
import com.silent.ilustriscore.core.utilities.visible
import com.silent.ilustriscore.core.view.BaseView
import kotlin.collections.ArrayList


class HomeManagerBinder(override val viewBind: FragmentManagerHomeBinding) : BaseView<Quote>() {


    override val presenter = QuotePresenter(this)
    private var quoteRecyclerAdapter: QuoteManagerAdapter? = null


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


    override fun onLoadFinish() {
        super.onLoadFinish()
        if (viewBind.quotesrecyclerview.visibility == View.GONE) {
            viewBind.quotesrecyclerview.fadeIn()
        }
    }

    override fun onLoading() {
        super.onLoading()
        viewBind.quotesrecyclerview.gone()
    }


    private fun setupRecyclerView(list: List<Quote>) {
        val quotesList: ArrayList<Quote> = ArrayList(list.sortedBy {
            it.isReport
        })
        quotesList.add(0, Quote.adminQuote())

        viewBind.quotesrecyclerview.run {
            if (quoteRecyclerAdapter == null) {
                quoteRecyclerAdapter = QuoteManagerAdapter(quotesList)
                adapter = quoteRecyclerAdapter
            } else {
                quoteRecyclerAdapter?.addData(quotesList)
            }
        }

    }


}