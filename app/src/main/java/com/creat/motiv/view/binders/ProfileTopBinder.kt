package com.creat.motiv.view.binders

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.creat.motiv.R
import com.creat.motiv.databinding.ProfileTopViewBinding
import com.creat.motiv.model.beans.User
import com.creat.motiv.presenter.UserPresenter
import com.creat.motiv.utilities.Alert
import com.creat.motiv.utilities.gone
import com.creat.motiv.view.BaseView
import com.creat.motiv.view.activities.SettingsActivity

class ProfileTopBinder(val uid: String,
                       val user: User? = null,
                       val isSettings: Boolean = false,
                       override val viewBind: ProfileTopViewBinding, override val context: Context) : BaseView<User>() {


    override fun presenter() = UserPresenter(this)


    override fun initView() {
        if (uid == presenter().currentUser?.uid) {
            viewBind.followButton.text = context.getString(R.string.edit_profile)
            viewBind.messageButton.gone()
            viewBind.followButton.setOnClickListener {
                val settings = Intent(context, SettingsActivity::class.java)
                context.startActivity(settings)
            }
        } else {
            viewBind.followButton.text = context.getString(R.string.follow_user)
            viewBind.followButton.gone()
        }

        if (isSettings) {
            viewBind.followButton.gone()
        }
        if (user != null) {
            showData(user)
        } else {
            val firebaseUser = presenter().currentUser
            presenter().getUser(firebaseUser!!.uid)
        }
    }


    override fun showData(data: User) {
        super.showData(data)
        viewBind.run {
            userData = data
            profilepic.setOnClickListener {
                if (user?.uid == presenter().currentUser?.uid || isSettings) {
                    Alert(context as Activity).picturePicker(presenter(), data.admin)
                }
            }
            Glide.with(context).load(data.picurl).error(ContextCompat.getDrawable(context, R.drawable.ic__41_floppy_disk)).into(profilepic)
            if (data.admin) {
                profilepic.borderColor = context.resources.getColor(R.color.material_yellow600)
                profilepic.borderWidth = 10

            } else {
                profilepic.borderColor = Color.TRANSPARENT
                profilepic.borderWidth = 0
            }
        }
    }

    init {
        initView()
    }
}