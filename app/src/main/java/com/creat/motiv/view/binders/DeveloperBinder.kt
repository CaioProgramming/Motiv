package com.creat.motiv.view.binders

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.creat.motiv.databinding.QuoteRecyclerBinding
import com.creat.motiv.model.beans.Developer
import com.creat.motiv.presenter.DeveloperPresenter
import com.creat.motiv.utilities.fadeIn
import com.creat.motiv.utilities.fadeOut
import com.creat.motiv.utilities.gone
import com.creat.motiv.view.adapters.RecyclerDeveloperAdapter
import com.ilustriscore.core.base.BaseView

class DeveloperBinder(
        override val context: Context,
        override val viewBind: QuoteRecyclerBinding) : BaseView<Developer>() {

    override fun presenter() = DeveloperPresenter(this)

    init {
        initView()
    }

    override fun onLoading() {
        viewBind.quotesrecyclerview.gone()
    }

    override fun onLoadFinish() = viewBind.loading.fadeOut()

    override fun showListData(list: List<Developer>) {
        super.showListData(list)
        viewBind.quotesrecyclerview.run {
            layoutManager = GridLayoutManager(context, 2, VERTICAL, false)
            adapter = RecyclerDeveloperAdapter(list, context)
        }
        viewBind.quotesrecyclerview.fadeIn()
    }


    override fun initView() {
        presenter().loadData()
    }

}