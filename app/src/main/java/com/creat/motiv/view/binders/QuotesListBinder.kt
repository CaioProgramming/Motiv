package com.creat.motiv.view.binders

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.creat.motiv.databinding.QuoteRecyclerBinding
import com.creat.motiv.model.Beans.Quote
import com.creat.motiv.presenter.QuotePresenter
import com.creat.motiv.view.BaseView
import com.creat.motiv.view.adapters.RecyclerAdapter

class QuotesListBinder(override val context: Context, override val viewBind: QuoteRecyclerBinding, val favorites: Boolean = false) : BaseView<Quote>() {


    override val presenter = QuotePresenter(this)


    override fun onLoading() {
        viewBind.notfound.animate()
    }

    override fun onLoadFinish() {
        viewBind.notfound.clearAnimation()
    }


    override fun showListData(list: List<Quote>) {
        viewBind.quotesrecyclerview.run {
            adapter = RecyclerAdapter(presenter, list, context)
            layoutManager = LinearLayoutManager(context, VERTICAL, false)
            setHasFixedSize(true)
            viewBind.notfound.visibility = View.GONE
        }
    }

    override fun initView() {
        if (!favorites) {
            presenter.loadData()
        } else {
            presenter.loadFavorites()
        }
    }

}