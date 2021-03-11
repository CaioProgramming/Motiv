package com.creat.motiv.profile.view.binders


import android.app.Activity
import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.creat.motiv.R
import com.creat.motiv.databinding.UserPicviewBinding
import com.ilustris.motiv.base.presenter.UserPresenter
import com.creat.motiv.utilities.Alert
import com.ilustris.motiv.base.DialogStyles
import com.ilustris.motiv.base.beans.User
import com.silent.ilustriscore.core.view.BaseView

class UserPicBind(
        private val quotesLikes: List<String>,
        val uid: String,
        override val context: Context,
        override val viewBind: UserPicviewBinding) : BaseView<User>() {



    override fun onLoading() {}

    override fun onLoadFinish() {}


    override fun showData(data: User) {
        super.showData(data)
        viewBind.userpic.run {
            if (context is AppCompatActivity) {
                val act = context as AppCompatActivity
                if (!act.isDestroyed) {
                    Glide.with(act).load(data.picurl).into(this)
                }
            }
            if (data.admin) {
                borderColor = context.resources.getColor(R.color.material_yellow600)
                borderWidth = 2
            } else {
                borderColor = Color.TRANSPARENT
                borderWidth = 0
            }
            setOnClickListener {
                Alert(context as Activity, DialogStyles.BOTTOM_NO_BORDER).showLikes(quotesLikes)
            }
        }
    }

    override val presenter = UserPresenter(this)

    override fun initView() {
        presenter.getUser(uid)
    }

}