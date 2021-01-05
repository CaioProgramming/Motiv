package com.creat.motiv.view.binders

import android.app.Activity
import android.content.Context
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.creat.motiv.R
import com.creat.motiv.databinding.ProfileTopViewBinding
import com.creat.motiv.model.beans.User
import com.creat.motiv.presenter.UserPresenter
import com.creat.motiv.utilities.Alert
import com.creat.motiv.utilities.DialogStyles
import com.creat.motiv.utilities.gone
import com.creat.motiv.view.BaseView


class ProfileTopBinder(val uid: String,
                       val user: User? = null,
                       val isSettings: Boolean = false,
                       override val viewBind: ProfileTopViewBinding, override val context: Context) : BaseView<User>() {


    override fun presenter() = UserPresenter(this)


    override fun initView() {
        onLoading()
        viewBind.followButton.gone()
        viewBind.messageButton.gone()
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
            Glide.with(context).load(data.picurl).error(ContextCompat.getDrawable(context, R.drawable.ic__41_floppy_disk)).into(profilepic)
            if (data.uid == presenter().currentUser!!.uid) {
                profilepic.setOnClickListener {
                    Alert(context as Activity, DialogStyles.BOTTOM_NO_BORDER).picturePicker(presenter(), data.admin)
                }
            }
            profilepic.borderColor = if (data.admin) {
                ContextCompat.getColor(context, R.color.material_yellow600)
            } else {
                ContextCompat.getColor(context, R.color.colorPrimaryDark)
            }
            if (data.admin) {
                backgroundAnimation.setColorFilter(ContextCompat.getColor(context, R.color.material_yellow600))
            } else {
                backgroundAnimation.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimaryDark))
            }
            if (data.uid == presenter().currentUser?.uid) {
                viewBind.followButton.gone()
            } else {
                viewBind.followButton.text = context.getString(R.string.follow_user)
            }
        }
    }

    init {
        initView()
    }
}