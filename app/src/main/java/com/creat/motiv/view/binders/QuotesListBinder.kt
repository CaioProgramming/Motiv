package com.creat.motiv.view.binders

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.creat.motiv.databinding.QuoteRecyclerBinding
import com.creat.motiv.model.DTOMessage
import com.creat.motiv.model.beans.Quote
import com.creat.motiv.presenter.QuotePresenter
import com.creat.motiv.utilities.*
import com.creat.motiv.view.BaseView
import com.creat.motiv.view.adapters.QuoteRecyclerAdapter


class QuotesListBinder(override val context: Context, override val viewBind: QuoteRecyclerBinding,
                       useinit: Boolean = true,
                       val showEmptyResult: Boolean = true,
                       val noResultCallback: (() -> Unit)? = null
) : BaseView<Quote>(useinit) {


    override fun presenter() = QuotePresenter(this)
    private val quoteRecyclerAdapter = QuoteRecyclerAdapter(context = context)
    private val linearLayoutManager = LinearLayoutManager(context, VERTICAL, false)

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
            setupRecyclerView(list)
        }
    }

    private fun setupRecyclerView(list: List<Quote>) {
        viewBind.quotesrecyclerview.run {
            adapter = quoteRecyclerAdapter
            layoutManager = linearLayoutManager
            quoteRecyclerAdapter.addData(list.sortedByDescending { quote ->
                quote.likes.size
            })
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