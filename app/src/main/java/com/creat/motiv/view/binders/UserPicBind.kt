package com.creat.motiv.view.binders


import android.content.Context
import com.bumptech.glide.Glide
import com.creat.motiv.databinding.UserPicviewBinding
import com.creat.motiv.model.beans.User
import com.creat.motiv.presenter.UserPresenter
import com.creat.motiv.view.BaseView

class UserPicBind(
        val uid: String,
        override val context: Context,
        override val viewBind: UserPicviewBinding) : BaseView<User>() {

    init {
        initView()
    }

    override fun onLoading() {
        viewBind.picShimmer.startShimmer()
    }

    override fun onLoadFinish() {
        viewBind.picShimmer.stopShimmer()
        viewBind.picShimmer.hideShimmer()
    }

    override fun showData(data: User) {
        super.showData(data)
        viewBind.run {
            Glide.with(context).load(data.picurl).into(userpic)
        }
    }

    override fun presenter() = UserPresenter(this)

    override fun initView() {
        presenter().getUser(uid)
    }

}