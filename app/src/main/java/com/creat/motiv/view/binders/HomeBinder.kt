package com.creat.motiv.view.binders

import android.content.Context
import androidx.appcompat.widget.SearchView
import com.creat.motiv.databinding.FragmentHomeBinding
import com.creat.motiv.model.beans.Quote
import com.creat.motiv.presenter.QuotePresenter
import com.creat.motiv.view.BaseView

class HomeBinder(
        override val context: Context, override val viewBind: FragmentHomeBinding) : BaseView<Quote>(), SearchView.OnQueryTextListener {

    override fun presenter() = QuotePresenter(this)
    var quotesListBinder: QuotesListBinder = QuotesListBinder(context, viewBind.quotesView)


    init {
        initView()
    }

    override fun initView() {
        viewBind.homeSearch.setOnQueryTextListener(this)
    }


    override fun onQueryTextSubmit(p0: String): Boolean {
        quotesListBinder.searchData(p0)
        return false
    }

    override fun onQueryTextChange(p0: String): Boolean {
        quotesListBinder.searchData(p0)
        return false
    }
}