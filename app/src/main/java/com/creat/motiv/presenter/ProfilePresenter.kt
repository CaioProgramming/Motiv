package com.creat.motiv.presenter

import com.creat.motiv.model.UserModel
import com.creat.motiv.model.beans.User
import com.creat.motiv.view.BaseView


class ProfilePresenter(override val view: BaseView<User>, val user: User? = null) : BasePresenter<User>() {




    init {
        user?.let {
            view.showData(it)
        }
    }

    override val model = UserModel(this)


    override fun onSingleData(data: User) {
        view.showData(data)
    }


}