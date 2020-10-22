package com.creat.motiv.presenter

import com.creat.motiv.model.Beans.User
import com.creat.motiv.model.UserModel
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

    override fun onError() {
        TODO("Not yet implemented")
    }

    override fun onSuccess() {
        TODO("Not yet implemented")
    }

}