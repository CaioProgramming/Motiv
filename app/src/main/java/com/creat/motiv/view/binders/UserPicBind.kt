package com.creat.motiv.view.binders


import android.content.Context
import android.graphics.Color
import com.bumptech.glide.Glide
import com.creat.motiv.databinding.UserPicviewBinding
import com.creat.motiv.model.beans.User
import com.creat.motiv.presenter.UserPresenter
import com.creat.motiv.view.fragments.LikersFragment
import com.ilustriscore.core.base.BaseView

class UserPicBind(
        val quotesLikes: List<String>,
        val uid: String,
        override val context: Context,
        override val viewBind: UserPicviewBinding) : BaseView<User>() {

    init {
        initView()
    }

    override fun onLoading() {}

    override fun onLoadFinish() {}


    override fun showData(data: User) {
        super.showData(data)
        viewBind.userpic.run {
            Glide.with(context).load(data.picurl).into(this)
            if (data.admin) {
                borderColor = context.resources.getColor(com.ilustriscore.R.color.gold)
                borderWidth = 2
            } else {
                borderColor = Color.TRANSPARENT
                borderWidth = 0
            }
            setOnClickListener {
                LikersFragment.build(context, quotesLikes)
            }
        }
    }

    override fun presenter() = UserPresenter(this)

    override fun initView() {
        presenter().getUser(uid)
    }

}