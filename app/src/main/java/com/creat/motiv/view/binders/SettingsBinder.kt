package com.creat.motiv.view.binders

import android.app.Activity
import android.content.Context
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.creat.motiv.R
import com.creat.motiv.databinding.FragmentSettingsBinding
import com.creat.motiv.model.beans.User
import com.creat.motiv.presenter.UserPresenter
import com.creat.motiv.utilities.*
import com.creat.motiv.view.BaseView
import com.google.firebase.auth.FirebaseAuth


class SettingsBinder(
        val uid: String,
        override val context: Context,
        override val viewBind: FragmentSettingsBinding) : BaseView<User>() {

    override fun presenter(): UserPresenter = UserPresenter(this)

    init {
        initView()
    }

    override fun initView() {
        viewBind.settingsContainer.left
        presenter().getUser(uid)
    }

    override fun showData(data: User) {
        super.showData(data)
        viewBind.run {
            changeIconPic.setOnClickListener {
                Alert(context as Activity, DialogStyles.BOTTOM_NO_BORDER).picturePicker(presenter(), data.admin)
            }
            provider.text = presenter().currentUser.email
            userID.text = data.uid
            userNameEditText.run {
                setText(data.name)
                addTextChangedListener {
                    if (this.text.isNotEmpty()) {
                        saveButton.fadeIn()
                    } else {
                        saveButton.fadeOut()
                    }
                }
                saveButton.setOnClickListener {
                    presenter().changeUserName(this.text.toString())
                }
            }
            userpic.run {
                Glide.with(context).load(data.picurl).into(this)
                setOnClickListener {
                    changeIconPic.callOnClick()
                }
            }

            singOutButton.setOnClickListener {
                FirebaseAuth.getInstance().signOut()
                Alert(context as Activity).showAlert(
                        message = "Você se desconectou, faça login novamente para usar o app",
                        buttonMessage = "Ok",
                        okClick = {
                            context.finish()
                        },
                        icon = R.drawable.ic_astronaut
                )
            }
        }
    }

}