package com.creat.motiv.view.binders

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.bumptech.glide.Glide
import com.creat.motiv.databinding.ProfileTopViewBinding
import com.creat.motiv.model.beans.User
import com.creat.motiv.presenter.UserPresenter
import com.creat.motiv.utilities.Alert
import com.creat.motiv.utilities.DialogStyles
import com.creat.motiv.utilities.gone
import com.creat.motiv.view.BaseView
import com.creat.motiv.view.activities.SettingsActivity

class ProfileTopBinder(val uid: String,
                       val user: User? = null,
                       val isSettings: Boolean = false,
                       override val viewBind: ProfileTopViewBinding,
                       override val context: Context) : BaseView<User>() {


    override fun presenter() = UserPresenter(this)


    override fun initView() {
        if (uid == presenter().currentUser()?.uid) {
            viewBind.followButton.gone()
            viewBind.messageButton.gone()
        } else {
            viewBind.settingsButton.gone()
        }

        if (isSettings) {
            viewBind.settingsButton.gone()
        }

        viewBind.settingsButton.setOnClickListener {
            val settings = Intent(context, SettingsActivity::class.java)
            context.startActivity(settings)
        }
        if (user != null) {
            showData(user)
        } else {
            val firebaseUser = presenter().currentUser()
            presenter().getUser(firebaseUser!!.uid)
        }
    }


    override fun showData(data: User) {
        super.showData(data)
        viewBind.run {
            userData = data
            profilepic.setOnClickListener {
                if (user?.uid == presenter().currentUser()?.uid) {
                    Alert(context as Activity, DialogStyles.BOTTOM_NO_BORDER).picturePicker(presenter(), data.admin)
                }
            }
            Glide.with(context).load(data.picurl).into(profilepic)
        }
    }

    init {
        initView()
    }
}