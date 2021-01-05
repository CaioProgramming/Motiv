package com.creat.motiv.view.binders


import android.app.Activity
import android.content.Context
import android.graphics.Color
import com.bumptech.glide.Glide
import com.creat.motiv.R
import com.creat.motiv.databinding.UserPicviewBinding
import com.creat.motiv.model.beans.User
import com.creat.motiv.presenter.UserPresenter
import com.creat.motiv.utilities.Alert
import com.creat.motiv.utilities.DialogStyles
import com.creat.motiv.view.BaseView

class UserPicBind(
        val quotesLikes: List<String>,
        val uid: String,
        override val context: Context,
        override val viewBind: UserPicviewBinding) : BaseView<User>() {

    init {
        initView()
    }

    override fun onLoading() {}

    override fun onLoadFinish() {}


    override fun showData(data: User) {
        super.showData(data)
        viewBind.userpic.run {
            Glide.with(context).load(data.picurl).into(this)
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

    override fun presenter() = UserPresenter(this)

    override fun initView() {
        presenter().getUser(uid)
    }

}