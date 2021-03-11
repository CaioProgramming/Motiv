package com.creat.motiv.profile.view.binders

import android.content.Context
import com.creat.motiv.databinding.FragmentProfileBinding
import com.ilustris.motiv.base.presenter.UserPresenter
import com.creat.motiv.quote.view.binder.QuotesListBinder
import com.ilustris.motiv.base.beans.User
import com.silent.ilustriscore.core.view.BaseView

class ProfileBinder(override val context: Context,
                    override val viewBind: FragmentProfileBinding,
                    val user: User? = null) : BaseView<User>() {

    override val presenter = UserPresenter(this)
    var quotesListBinder: QuotesListBinder = QuotesListBinder(viewBind.quotesView, useInit = false)


    override fun initView() {
        if (user == null) {
            presenter.getUser()
        } else {
            showData(user)
        }
    }

    override fun showData(data: User) {
        onLoadFinish()
        viewBind.run {
            quotesListBinder.showProfile = data.uid
            quotesListBinder.getUserQuotes(data.uid)
        }
    }

    init {
        initView()
    }
}