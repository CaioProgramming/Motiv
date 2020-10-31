package com.creat.motiv.view.binders

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import com.creat.motiv.databinding.ProfilepicselectBinding
import com.creat.motiv.model.beans.Pics
import com.creat.motiv.presenter.PicsPresenter
import com.creat.motiv.view.BaseView
import com.creat.motiv.view.adapters.RecyclerPicAdapter

class ProfilePicSelectBinder(override val context: Context,
                             val admin: Boolean,
                             override val viewBind: ProfilepicselectBinding, val picSelected: (Pics) -> Unit) : BaseView<Pics>() {

    init {
        initView()
    }

    override fun presenter() = PicsPresenter(this)


    override fun initView() {
        presenter().loadData()
    }


    override fun showListData(list: List<Pics>) {
        super.showListData(list)
        viewBind.picsrecycler.layoutManager = GridLayoutManager(context, 2, HORIZONTAL, false)
        viewBind.picsrecycler.adapter = RecyclerPicAdapter(context = context,
                onSelectPick = picSelected,
                isAdmin = admin,
                pictureList = ArrayList(list))
    }
}