package com.creat.motiv.view.binders

import android.app.Dialog
import android.content.Context
import android.view.View.GONE
import com.creat.motiv.databinding.ProfilepicselectBinding
import com.creat.motiv.model.beans.Pics
import com.creat.motiv.presenter.PicsPresenter
import com.creat.motiv.utilities.fadeIn
import com.creat.motiv.utilities.fadeOut
import com.creat.motiv.view.BaseView
import com.creat.motiv.view.adapters.RecyclerPicAdapter

class ProfilePicSelectBinder(val dialog: Dialog? = null,
                             override val context: Context,
                             val admin: Boolean,
                             override val viewBind: ProfilepicselectBinding, val picSelected: (Pics) -> Unit) : BaseView<Pics>() {

    init {
        initView()
    }

    override fun presenter() = PicsPresenter(this)


    override fun initView() {
        presenter().loadData()
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