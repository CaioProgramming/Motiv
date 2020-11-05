package com.creat.motiv.view.binders

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import androidx.recyclerview.widget.SimpleItemAnimator
import com.creat.motiv.databinding.QuoteRecyclerBinding
import com.creat.motiv.model.beans.Quote
import com.creat.motiv.presenter.QuotePresenter
import com.creat.motiv.utilities.fadeIn
import com.creat.motiv.utilities.fadeOut
import com.creat.motiv.utilities.gone
import com.creat.motiv.utilities.repeatBounce
import com.creat.motiv.view.BaseView
import com.creat.motiv.view.adapters.RecyclerAdapter


class QuotesListBinder(override val context: Context, override val viewBind: QuoteRecyclerBinding, useinit: Boolean = true) : BaseView<Quote>(useinit) {


    override fun presenter() = QuotePresenter(this)
    val quoteadapter = RecyclerAdapter(context = context)

    init {
        viewBind.quotesrecyclerview.run {
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            adapter = quoteadapter
            layoutManager = LinearLayoutManager(context, VERTICAL, false)
            setHasFixedSize(true)
        }
        if (useInit) {
            initView()
        }
    }


    fun searchData(query: String) {
        quoteadapter.searchQuote(query)
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
            viewBind.notFoundInclude.emptyList.fadeIn()
            viewBind.notFoundInclude.emptyImage.repeatBounce()
        } else {
            viewBind.notFoundInclude.emptyList.fadeOut()
            if (quoteadapter.quoteList.isEmpty()) {
                viewBind.quotesrecyclerview.fadeIn()
            }
            viewBind.notFoundInclude.emptyImage.clearAnimation()
            quoteadapter.addData(list)
        }
    }

    override fun initView() {
        presenter().loadData()
    }

}