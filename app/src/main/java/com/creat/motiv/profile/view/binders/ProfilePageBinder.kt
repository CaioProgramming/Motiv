package com.creat.motiv.profile.view.binders

import com.bumptech.glide.Glide
import com.creat.motiv.databinding.ProfileQuoteCardBinding
import com.creat.motiv.profile.presenter.UserPresenter
import com.creat.motiv.profile.model.beans.User
import com.silent.ilustriscore.core.view.BaseView

class ProfilePageBinder(override val viewBind: ProfileQuoteCardBinding, val uid: String) : BaseView<User>() {

    override val presenter = UserPresenter(this)

    override fun initView() {
        presenter.getUser(uid)
    }

    override fun showData(data: User) {
        super.showData(data)
        viewBind.run {
            Glide.with(context).load(data.picurl).into(profilepic)
            Glide.with(context).asGif().centerCrop().load(data.cover).into(userBackground)
            username.text = data.name
        }
    }

}