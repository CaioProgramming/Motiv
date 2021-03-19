package com.creat.motiv.profile.icon.view

import android.app.Dialog
import android.view.View.GONE
import com.creat.motiv.databinding.ProfilepicselectBinding
import com.ilustris.motiv.base.beans.Pics
import com.ilustris.motiv.base.presenter.PicsPresenter
import com.ilustris.motiv.base.adapters.RecyclerPicAdapter
import com.ilustris.animations.fadeIn
import com.ilustris.animations.fadeOut
import com.silent.ilustriscore.core.view.BaseView

class ProfilePicSelectBinder(
        override val viewBind: ProfilepicselectBinding,
        val picSelected: (Pics) -> Unit) : BaseView<Pics>() {


    override val presenter = PicsPresenter(this)


    override fun initView() {
        viewBind.dialogTitle.text = "Selecionar ícone de perfil"
        presenter.loadData()
    }

    override fun onLoading() {
        super.onLoading()
        viewBind.loading.fadeIn()
    }



    override fun onLoadFinish() {
        super.onLoadFinish()
        viewBind.loading.fadeOut()

        if (viewBind.mainView.visibility == GONE) {
            viewBind.mainView.fadeIn()
        }
    }

    override fun showListData(list: List<Pics>) {
        super.showListData(list)
        val pics: ArrayList<Pics> = ArrayList(list.sortedBy { it.id })

        viewBind.picsrecycler.adapter = RecyclerPicAdapter(onSelectPick = picSelected, pictureList = pics)
    }


}