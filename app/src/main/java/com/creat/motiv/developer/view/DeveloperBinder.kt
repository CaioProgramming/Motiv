package com.creat.motiv.developer.view

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.creat.motiv.databinding.QuoteHeaderViewBinding
import com.ilustris.motiv.base.beans.Developer
import com.creat.motiv.developer.presenter.DeveloperPresenter
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
            layoutManager = GridLayoutManager(
                context,
                if (list.size > 4) 2 else 1,
                RecyclerView.HORIZONTAL,
                false
            )
            fadeIn()
        }
    }


    override val presenter = DeveloperPresenter(this)

}