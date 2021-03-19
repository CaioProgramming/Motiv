package com.ilustris.motiv.base.binder

import android.graphics.Color
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.ilustris.animations.fadeIn
import com.ilustris.motiv.base.presenter.UserPresenter
import com.ilustris.motiv.base.R
import com.ilustris.motiv.base.Routes
import com.ilustris.motiv.base.beans.User
import com.ilustris.motiv.base.databinding.UserQuoteCardViewBinding
import com.silent.ilustriscore.core.view.BaseView

class UserViewBinder(
        val userID: String,
        override val viewBind: UserQuoteCardViewBinding) : BaseView<User>() {

    override val presenter = UserPresenter(this)


    override fun showData(data: User) {
        viewBind.run {
            userData = data
            Glide.with(context.applicationContext).load(data.picurl).error(ContextCompat.getDrawable(context, R.drawable.avatarnotfound)).into(userpic)
            userContainer.setOnClickListener {
                showUserProfile(data)
            }
            if (data.admin) {
                userpic.borderColor = context.resources.getColor(R.color.material_yellow600)
                userpic.borderWidth = 5

            } else {
                userpic.borderColor = Color.TRANSPARENT
                userpic.borderWidth = 0
            }
            userContainer.fadeIn()
        }
    }

    private fun showUserProfile(user: User) {
        if (user.uid != presenter.user?.uid) {
            Routes(context).openUserProfile(user, viewBind.userpic)

        }

    }

    fun setDate(date: String) {
        viewBind.quoteDate.text = date
    }

    init {
        initView()
    }


    override fun initView() {
        presenter.getUser(userID)
    }

}