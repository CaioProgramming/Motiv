package com.creat.motiv.view.binders


import android.content.Context
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.creat.motiv.R
import com.creat.motiv.databinding.UserPicviewBinding
import com.creat.motiv.model.beans.User
import com.creat.motiv.presenter.UserPresenter
import com.creat.motiv.view.BaseView

class UserPicBind(
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
        viewBind.run {
            Glide.with(context).load(data.picurl).error(ContextCompat.getDrawable(context, R.drawable.ic__41_floppy_disk)).into(userpic)
        }
    }

    override fun presenter() = UserPresenter(this)

    override fun initView() {
        presenter().getUser(uid)
    }

}