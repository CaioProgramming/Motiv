package com.creat.motiv.view.binders

import android.content.Context
import androidx.appcompat.widget.SearchView
import com.creat.motiv.databinding.FragmentHomeBinding
import com.creat.motiv.model.Beans.Quote
import com.creat.motiv.presenter.QuotePresenter
import com.creat.motiv.view.BaseView
import com.creat.motiv.view.adapters.RecyclerAdapter

class HomeBinder(
        override val context: Context, override val viewBind: FragmentHomeBinding) : BaseView<Quote>(), SearchView.OnQueryTextListener {

    override val presenter = QuotePresenter(this)
    var quotesRecycler = RecyclerAdapter(presenter, emptyList(), context)

    override fun onLoading() {
        TODO("Not yet implemented")
    }

    override fun onLoadFinish() {
        TODO("Not yet implemented")
    }

    override fun initView() {
        presenter.loadData()
        viewBind.homeSearch.setOnQueryTextListener(this)
    }

    override fun showData(data: Quote) {

    }

    override fun showListData(list: List<Quote>) {
        viewBind.composesrecycler.run {
            quotesRecycler.quoteList = list
            quotesRecycler.notifyDataSetChanged()
        }
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        TODO("Not yet implemented")
    }
}