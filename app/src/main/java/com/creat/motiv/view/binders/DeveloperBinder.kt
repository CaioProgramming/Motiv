package com.creat.motiv.view.binders

import com.creat.motiv.databinding.QuoteHeaderViewBinding
import com.creat.motiv.model.beans.Developer
import com.creat.motiv.presenter.DeveloperPresenter
import com.creat.motiv.view.adapters.RecyclerDeveloperAdapter
import com.ilustris.animations.fadeIn
import com.silent.ilustriscore.core.utilities.gone
import com.silent.ilustriscore.core.view.BaseView

class DeveloperBinder(override val viewBind: QuoteHeaderViewBinding) : BaseView<Developer>() {

    override fun initView() {
        presenter.loadData()
    }


    override fun onLoading() {
        viewBind.recyclerView.gone()
    }

    override fun onLoadFinish() {
    }

    override fun showListData(list: List<Developer>) {
        super.showListData(list)
        viewBind.recyclerView.run {
            adapter = RecyclerDeveloperAdapter(list, context)
            fadeIn()
        }
    }


    override val presenter = DeveloperPresenter(this)

}