package com.creat.motiv.profile.view.binders


import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.creat.motiv.R
import com.creat.motiv.profile.view.LikesDialog
import com.ilustris.motiv.base.presenter.UserPresenter
import com.ilustris.motiv.base.beans.User
import com.ilustris.motiv.base.databinding.UserPicviewBinding
import com.ilustris.motiv.base.utils.activity
import com.silent.ilustriscore.core.view.BaseView

class UserPicBind(
        private val quotesLikes: List<String>,
        private val uid: String,
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
                borderColor = ContextCompat.getColor(context, R.color.adminBorder)
                borderWidth = 2
            } else {
                borderColor = Color.TRANSPARENT
                borderWidth = 0
            }
            setOnClickListener {
                context.activity()?.let { activity -> LikesDialog(activity, quotesLikes).buildDialog() }
            }
        }
    }

    override val presenter = UserPresenter(this)

    override fun initView() {
        presenter.getUser(uid)
    }

}