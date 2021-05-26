package com.creat.motiv.profile.view.binders

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.creat.motiv.R
import com.creat.motiv.databinding.ProfileQuoteCardBinding
import com.creat.motiv.profile.cover.CoverPickerDialog
import com.creat.motiv.profile.icon.view.IconPickerDialog
import com.creat.motiv.utilities.Alert
import com.ilustris.animations.fadeIn
import com.ilustris.motiv.base.presenter.UserPresenter
import com.ilustris.motiv.base.utils.DialogStyles
import com.ilustris.motiv.base.beans.User
import com.ilustris.motiv.base.utils.loadGif
import com.ilustris.motiv.base.utils.loadImage
import com.silent.ilustriscore.core.model.DataException
import com.silent.ilustriscore.core.model.ErrorType
import com.silent.ilustriscore.core.view.BaseView

class ProfilePageBinder(override val viewBind: ProfileQuoteCardBinding, val uid: String) : BaseView<User>() {

    override val presenter = UserPresenter(this)

    override fun initView() {
        presenter.getUser(uid)
    }

    override fun showData(data: User) {
        super.showData(data)
        viewBind.run {
            profilepic.loadImage(data.picurl)
            if (data.admin) {
                viewBind.profilepic.borderColor = ContextCompat.getColor(context, R.color.adminBorder)
                viewBind.profilepic.borderWidth = 5
            }
            userBackground.loadGif(data.cover)
            username.text = data.name
            if (data.uid == presenter.user?.uid) {
                userBackground.setOnClickListener {
                    CoverPickerDialog(context) { cover ->
                        data.cover = cover.url
                        presenter.updateData(data)
                    }.buildDialog()

                }
                profilepic.setOnClickListener {
                    IconPickerDialog(context) { pic ->
                        presenter.changeProfilePic(pic)
                    }.buildDialog()

                }
            }
            quoteCard.fadeIn()
        }
    }

    override fun error(dataException: DataException) {
        super.error(dataException)
        if (dataException.code == ErrorType.NOT_FOUND) {
            presenter.saveUser()
        }
    }
}