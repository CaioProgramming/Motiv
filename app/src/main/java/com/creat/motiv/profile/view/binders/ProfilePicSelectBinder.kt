package com.creat.motiv.profile.view.binders

import android.app.Dialog
import android.view.View.GONE
import com.creat.motiv.databinding.ProfilepicselectBinding
import com.creat.motiv.profile.model.beans.Pics
import com.creat.motiv.profile.presenter.PicsPresenter
import com.creat.motiv.profile.view.adapters.RecyclerPicAdapter
import com.ilustris.animations.fadeIn
import com.ilustris.animations.fadeOut
import com.silent.ilustriscore.core.view.BaseView

class ProfilePicSelectBinder(val dialog: Dialog? = null,
                             val admin: Boolean,
                             override val viewBind: ProfilepicselectBinding,
                             val picSelected: (Pics) -> Unit) : BaseView<Pics>() {


    override val presenter = PicsPresenter(this)


    override fun initView() {
        presenter.loadData()
    }

    override fun onLoading() {
        super.onLoading()
        viewBind.loading.fadeIn()
    }



    override fun onLoadFinish() {
        super.onLoadFinish()
        viewBind.loading.fadeOut()
        viewBind.closeButton.setOnClickListener {
            dialog?.dismiss()
        }
        if (viewBind.mainView.visibility == GONE) {
            viewBind.mainView.fadeIn()
        }
    }

    override fun showListData(list: List<Pics>) {
        super.showListData(list)
        val pics: ArrayList<Pics> = ArrayList(list)
        if (admin) {
            pics.add(0, Pics.addPic())
        }
        viewBind.picsrecycler.adapter = RecyclerPicAdapter(context = context, onSelectPick = picSelected, isAdmin = admin, pictureList = pics)
    }


}