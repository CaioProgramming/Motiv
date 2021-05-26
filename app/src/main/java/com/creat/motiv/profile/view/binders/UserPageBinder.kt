package com.creat.motiv.profile.view.binders

import com.creat.motiv.databinding.UsersPageCardBinding
import com.creat.motiv.profile.view.adapters.UserRecyclerAdapter
import com.ilustris.motiv.base.beans.Style
import com.ilustris.motiv.base.presenter.UserPresenter
import com.ilustris.motiv.base.beans.User
import com.ilustris.motiv.base.utils.loadGif
import com.silent.ilustriscore.core.model.DataException
import com.silent.ilustriscore.core.model.ErrorType
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
                adapter = UserRecyclerAdapter(list.filter { it.uid != user.uid }
                    .sortedByDescending { it.admin })

            }
        }
        val viewUserStyle = Style.usersStyle
        viewBind.viewUsersBackground.loadGif(viewUserStyle.backgroundURL)

    }

    override fun error(dataException: DataException) {
        super.error(dataException)
        if (dataException.code == ErrorType.NOT_FOUND) {
            presenter.saveUser()
        }
    }

}