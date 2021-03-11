package com.creat.motiv.view.binders

import androidx.appcompat.widget.SearchView
import com.creat.motiv.databinding.FragmentHomeBinding
import com.creat.motiv.model.beans.Quote
import com.creat.motiv.quote.presenter.QuotePresenter
import com.creat.motiv.quote.view.binder.QuotesListBinder
import com.silent.ilustriscore.core.view.BaseView

class HomeBinder(override val viewBind: FragmentHomeBinding) : BaseView<Quote>(), SearchView.OnQueryTextListener {

    override val presenter = QuotePresenter(this)
    var quotesListBinder: QuotesListBinder = QuotesListBinder(viewBind.quotesView)


    init {
        initView()
    }

    override fun initView() {
        if (useInit) {
            quotesListBinder.initView()
        }
    }


    override fun onQueryTextSubmit(p0: String): Boolean {
        quotesListBinder.searchData(p0)
        return false
    }

    override fun onQueryTextChange(p0: String): Boolean {

        return false
    }
}