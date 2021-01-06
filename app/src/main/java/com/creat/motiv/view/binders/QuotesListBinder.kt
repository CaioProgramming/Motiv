package com.creat.motiv.view.binders

import android.content.Context
import android.view.View
import com.creat.motiv.databinding.QuoteRecyclerBinding
import com.creat.motiv.model.beans.Quote
import com.creat.motiv.presenter.QuotePresenter
import com.creat.motiv.utilities.fadeIn
import com.creat.motiv.utilities.fadeOut
import com.creat.motiv.utilities.gone
import com.creat.motiv.utilities.slideInBottom
import com.creat.motiv.view.adapters.QuoteRecyclerAdapter
import com.ilustriscore.core.base.BaseView
import com.ilustriscore.core.base.DTOMessage
import com.ilustriscore.core.utilities.OperationType


class QuotesListBinder(override val context: Context, override val viewBind: QuoteRecyclerBinding,
                       useinit: Boolean = true,
                       val showEmptyResult: Boolean = true,
                       val noResultCallback: (() -> Unit)? = null
) : BaseView<Quote>(useinit) {


    override fun presenter() = QuotePresenter(this)
    private var quoteRecyclerAdapter: QuoteRecyclerAdapter? = null

    init {
        if (useInit) {
            initView()
        }
    }

    override fun onLoadFinish() {
        super.onLoadFinish()
        viewBind.loading.gone()
        if (viewBind.quotesrecyclerview.visibility == View.GONE) {
            viewBind.quotesrecyclerview.slideInBottom()
        }
    }

    override fun onLoading() {
        super.onLoading()
        viewBind.loading.fadeIn()
        viewBind.quotesrecyclerview.gone()
    }

    fun searchData(query: String) {
        presenter().queryData(query, "quote")
    }

    fun searchAuthor(query: String) {
        presenter().queryData(query, "author")
    }

    fun getUserQuotes(uid: String) {
        presenter().findPreciseData(uid, "userID")
    }

    fun getFavorites(uid: String) {
        presenter().loadFavorites(uid)
    }

    override fun showListData(list: List<Quote>) {
        if (list.isEmpty()) {
            viewBind.quotesrecyclerview.fadeOut()
            if (showEmptyResult) {
                viewBind.notFoundInclude.emptyList.fadeIn()
            }
            noResultCallback?.invoke()
        } else {
            if (quoteRecyclerAdapter == null) {
                setupRecyclerView(list)
            } else {
                quoteRecyclerAdapter!!.addData(list)
            }
        }
    }

    private fun setupRecyclerView(list: List<Quote>) {
        viewBind.quotesrecyclerview.run {
            quoteRecyclerAdapter = QuoteRecyclerAdapter(ArrayList((list.sortedByDescending { quote ->
                quote.likes.size
            })), context)
            adapter = quoteRecyclerAdapter
        }
    }

    override fun initView() {
        presenter().loadData()
    }

    override fun getCallBack(dtoMessage: DTOMessage) {
        super.getCallBack(dtoMessage)
        if (dtoMessage.operationType == OperationType.DATA_SAVED) {
            initView()
        }
    }

}