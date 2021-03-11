package com.ilustris.motiv.base.presenter

import com.ilustris.motiv.base.model.UserModel
import com.ilustris.motiv.base.beans.Pics
import com.ilustris.motiv.base.beans.User
import com.silent.ilustriscore.core.presenter.BasePresenter
import com.silent.ilustriscore.core.view.BaseView

class UserPresenter(override val view: BaseView<User>) : BasePresenter<User>() {

    fun getUser(uid: String? = user!!.uid) {
        uid?.let { model.getSingleData(it) }
    }

    fun changeProfilePic(pics: Pics) {
        view.onLoading()
        model.updateUserPic(pics)
    }

    fun changeUserName(newName: String) {
        view.onLoading()
        model.updateUserName(newName)
    }


    override val model: UserModel = UserModel(this)
}