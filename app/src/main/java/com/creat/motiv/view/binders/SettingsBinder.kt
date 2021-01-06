package com.creat.motiv.view.binders

import android.app.Activity
import android.content.Context
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.creat.motiv.R
import com.creat.motiv.databinding.FragmentSettingsBinding
import com.creat.motiv.model.beans.User
import com.creat.motiv.presenter.UserPresenter
import com.creat.motiv.utilities.fadeIn
import com.creat.motiv.utilities.fadeOut
import com.creat.motiv.utilities.getSupportFragmentManager
import com.creat.motiv.view.fragments.IconPickerFragment
import com.google.firebase.auth.FirebaseAuth
import com.ilustriscore.core.base.BaseView
import com.ilustriscore.core.view.dialog.VerticalDialog


class SettingsBinder(
        val uid: String,
        override val context: Context,
        override val viewBind: FragmentSettingsBinding) : BaseView<User>() {

    override fun presenter(): UserPresenter = UserPresenter(this)

    init {
        initView()
    }

    override fun initView() {
        presenter().getUser(uid)
    }

    override fun showData(data: User) {
        super.showData(data)
        viewBind.run {
            changeIconPic.setOnClickListener {
                context.getSupportFragmentManager()?.let {
                    IconPickerFragment.build(it, presenter(), data.admin)
                }
            }
            provider.text = presenter().currentUser?.email
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
                context.getSupportFragmentManager()?.let {
                    VerticalDialog.build(R.id.rootblur, it, "Você se desconectou, faça login novamente para usar o app", okClick = {
                        (context as Activity).finish()
                    }, cancelClick = {
                        (context as Activity).finish()
                    })

                }
            }
        }
    }

}