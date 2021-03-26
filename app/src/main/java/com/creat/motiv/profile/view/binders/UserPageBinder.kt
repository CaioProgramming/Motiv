package com.creat.motiv.profile.view.binders

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.creat.motiv.R
import com.creat.motiv.databinding.ProfileQuoteCardBinding
import com.creat.motiv.databinding.UsersPageCardBinding
import com.creat.motiv.profile.cover.CoverPickerDialog
import com.creat.motiv.profile.icon.view.IconPickerDialog
import com.creat.motiv.profile.view.adapters.UserRecyclerAdapter
import com.creat.motiv.utilities.Alert
import com.ilustris.motiv.base.beans.QuoteStyle
import com.ilustris.motiv.base.presenter.UserPresenter
import com.ilustris.motiv.base.utils.DialogStyles
import com.ilustris.motiv.base.beans.User
import com.ilustris.motiv.base.utils.FontUtils
import com.ilustris.motiv.base.utils.loadGif
import com.ilustris.motiv.base.utils.loadImage
import com.silent.ilustriscore.core.view.BaseView

class UserPageBinder(override val viewBind: UsersPageCardBinding) : BaseView<User>() {

    override val presenter = UserPresenter(this)

    override fun initView() {
        presenter.loadData()
    }

    override fun showListData(list: List<User>) {
        super.showListData(list)
        presenter.user?.let { user ->
            viewBind.usersRecycler.run {
                adapter = UserRecyclerAdapter(list.filter { it.uid != user.uid }.sortedByDescending { it.admin })

            }
        }

        val viewUserStyle = QuoteStyle.usersStyle
        viewBind.viewUsersBackground.loadGif(viewUserStyle.backgroundURL)

    }


}