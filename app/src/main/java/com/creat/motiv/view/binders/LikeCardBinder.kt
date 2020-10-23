package com.creat.motiv.view.binders

import android.content.Context
import com.creat.motiv.databinding.LikeLayoutBinding
import com.creat.motiv.model.beans.Likes
import com.creat.motiv.presenter.LikesPresenter
import com.creat.motiv.view.BaseView

class LikeCardBinder(val quoteID: String,
                     override val context: Context,
                     override val viewBind: LikeLayoutBinding) : BaseView<Likes>() {

    override fun presenter() = LikesPresenter(this, quoteID)

    override fun showListData(list: List<Likes>) {
        viewBind.like.isChecked = list.any { like -> like.userid == presenter().currentUser()!!.uid }
        viewBind.like.setOnClickListener {
            if (!viewBind.like.isChecked) {
                presenter().likeQuote()
            } else {
                presenter().deslikeQuote()
            }
        }
    }


    init {
        initView()
    }


    override fun initView() {
        presenter().loadData()

    }

}