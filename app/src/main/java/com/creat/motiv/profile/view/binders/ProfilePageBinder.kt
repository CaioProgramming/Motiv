package com.creat.motiv.profile.view.binders

import android.app.Activity
import com.bumptech.glide.Glide
import com.creat.motiv.databinding.ProfileQuoteCardBinding
import com.creat.motiv.utilities.Alert
import com.ilustris.motiv.base.presenter.UserPresenter
import com.ilustris.motiv.base.DialogStyles
import com.ilustris.motiv.base.beans.User
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

            if (data.uid == presenter.user?.uid) {
                userBackground.setOnClickListener {
                    Alert(context as Activity, DialogStyles.BOTTOM_NO_BORDER).coverPicker {
                        data.cover = it
                        presenter.updateData(data)
                    }
                }
                profilepic.setOnClickListener {
                    Alert(context as Activity, DialogStyles.BOTTOM_NO_BORDER).picturePicker(data.admin) {
                        presenter.changeProfilePic(it)
                    }
                }
            }
        }
    }

}