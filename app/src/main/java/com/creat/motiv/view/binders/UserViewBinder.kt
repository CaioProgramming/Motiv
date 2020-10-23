package com.creat.motiv.view.binders

import android.content.Context
import com.bumptech.glide.Glide
import com.creat.motiv.databinding.UserQuoteCardViewBinding
import com.creat.motiv.model.beans.User
import com.creat.motiv.presenter.UserPresenter
import com.creat.motiv.view.BaseView

class UserViewBinder(
        val userID: String,
        override val context: Context, override val viewBind: UserQuoteCardViewBinding) : BaseView<User>() {

    override fun presenter() = UserPresenter(this)

    override fun onLoading() {
        viewBind.userShimmer.startShimmer()
    }


    override fun showData(data: User) {
        viewBind.run {
            userData = data
            Glide.with(context).load(data.picurl).into(viewBind.userpic)
        }
    }

    init {
        initView()
    }

    override fun onLoadFinish() {
        viewBind.userShimmer.run {
            stopShimmer()
            hideShimmer()
        }
    }

    override fun initView() {
        presenter().getUser(userID)
    }

}