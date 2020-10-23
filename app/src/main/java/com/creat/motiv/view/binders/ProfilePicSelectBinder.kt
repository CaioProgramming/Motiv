package com.creat.motiv.view.binders

import android.content.Context
import com.creat.motiv.databinding.ProfilepicselectBinding
import com.creat.motiv.model.beans.Pics
import com.creat.motiv.presenter.PicsPresenter
import com.creat.motiv.view.BaseView
import com.creat.motiv.view.adapters.RecyclerPicAdapter

class ProfilePicSelectBinder(override val context: Context,
                             override val viewBind: ProfilepicselectBinding, val picSelected: (Pics) -> Unit) : BaseView<Pics>() {

    init {
        initView()
    }

    override fun presenter() = PicsPresenter(this)

    override fun onLoading() {
        TODO("Not yet implemented")
    }

    override fun onLoadFinish() {
        TODO("Not yet implemented")
    }

    override fun initView() {
        presenter().loadData()
    }

    override fun showListData(list: List<Pics>) {
        viewBind.picsrecycler.apply {
            adapter = RecyclerPicAdapter(list, context, picSelected)
        }

    }
}