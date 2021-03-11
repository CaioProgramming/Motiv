package com.creat.motiv.profile.cover.view

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.creat.motiv.databinding.ProfilepicselectBinding
import com.ilustris.motiv.base.beans.Cover
import com.ilustris.motiv.base.model.CoversPresenter
import com.ilustris.animations.fadeIn
import com.ilustris.animations.fadeOut
import com.silent.ilustriscore.core.utilities.gone
import com.silent.ilustriscore.core.view.BaseView

class CoversBinder(override val viewBind: ProfilepicselectBinding, private val onSelectCover: (Cover) -> Unit) : BaseView<Cover>() {
    override val presenter = CoversPresenter(this)

    init {
        initView()
    }

    override fun initView() {
        viewBind.dialogTitle.text = "Selecione uma Capa de perfil"
        viewBind.closeButton.gone()
        presenter.loadData()
    }

    override fun showListData(list: List<Cover>) {
        super.showListData(list)
        viewBind.picsrecycler.run {
            adapter = RecyclerCoverAdapter(list, onSelectCover)
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
    }

    override fun onLoading() {
        super.onLoading()
        viewBind.loading.fadeIn()
    }

    override fun onLoadFinish() {
        super.onLoadFinish()
        viewBind.loading.fadeOut()
        viewBind.picsrecycler.fadeIn()
    }

}