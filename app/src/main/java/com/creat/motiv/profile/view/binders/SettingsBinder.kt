package com.creat.motiv.profile.view.binders

import android.app.Activity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.creat.motiv.R
import com.creat.motiv.databinding.FragmentSettingsBinding
import com.ilustris.motiv.base.presenter.UserPresenter
import com.creat.motiv.utilities.*
import com.google.firebase.auth.FirebaseAuth
import com.ilustris.animations.fadeIn
import com.ilustris.animations.fadeOut
import com.ilustris.motiv.base.DialogStyles
import com.ilustris.motiv.base.beans.User
import com.silent.ilustriscore.core.view.BaseView


class SettingsBinder(
        val uid: String,
        override val viewBind: FragmentSettingsBinding) : BaseView<User>() {

    override val presenter: UserPresenter = UserPresenter(this)

    init {
        initView()
    }

    override fun initView() {
        viewBind.settingsContainer.left
        presenter.getUser(uid)
    }

    override fun showData(data: User) {
        super.showData(data)
        viewBind.run {

            provider.text = presenter.user?.email ?: "-"
            userID.text = data.uid
            userNameEditText.run {
                setText(data.name)
                addTextChangedListener {
                    if (this.text.isNotEmpty() && this.text.toString() != data.name) {
                        if (saveButton.visibility == View.GONE) {
                            saveButton.fadeIn()
                        }
                    } else {
                        saveButton.fadeOut()
                    }
                }
                saveButton.setOnClickListener {
                    presenter.changeUserName(this.text.toString())
                }
            }
            userpic.run {
                Glide.with(context).load(data.picurl).into(this)
                setOnClickListener {
                    Alert(context as Activity, DialogStyles.BOTTOM_NO_BORDER).picturePicker(data.admin) {
                        presenter.changeProfilePic(it)
                    }
                }
            }
            userBackground.run {
                Glide.with(context).asGif().centerCrop().load(data.cover).into(this)
                setOnClickListener {
                    Alert(context as Activity, DialogStyles.BOTTOM_NO_BORDER).coverPicker {
                        data.cover = it
                        presenter.updateData(data)
                    }
                }
            }


            singOutButton.setOnClickListener {
                FirebaseAuth.getInstance().signOut()
                Alert(context as Activity).showAlert(
                        message = "Você se desconectou, faça login novamente para usar o app",
                        buttonMessage = "Ok",
                        okClick = {
                            (context as AppCompatActivity?)?.finish()
                        },
                        icon = R.drawable.ic_astronaut
                )
            }
        }
    }

}