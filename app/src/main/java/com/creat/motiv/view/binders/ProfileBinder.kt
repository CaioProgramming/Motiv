package com.creat.motiv.view.binders

import android.app.Activity
import android.content.Context
import com.bumptech.glide.Glide
import com.creat.motiv.databinding.FragmentProfileBinding
import com.creat.motiv.model.Beans.User
import com.creat.motiv.presenter.UserPresenter
import com.creat.motiv.utils.Alert
import com.creat.motiv.utils.DIALOG_STYLES
import com.creat.motiv.view.BaseView
import com.creat.motiv.view.adapters.QuotePagerAdapter

class ProfileBinder(override val context: Context, override val viewBind: FragmentProfileBinding, val user: User? = null) : BaseView<User>() {

    override val presenter = UserPresenter(this)

    override fun onLoading() {
        TODO("Not yet implemented")
    }

    override fun onLoadFinish() {
        TODO("Not yet implemented")
    }

    override fun initView() {
        user?.let {
            showData(it)
        }
    }

    override fun showData(data: User) {
        Glide.with(context).load(data.picurl).into(viewBind.profilepic)
        viewBind.profilepic.setOnClickListener {
            if (presenter.currentUser!!.uid.equals(data.uid)) {
                Alert(context as Activity, DIALOG_STYLES.BOTTOM_NO_BORDER).picturePicker(presenter)
            }
        }

        viewBind.quotespager.run {
            adapter = QuotePagerAdapter(context)
        }
    }


}