package com.creat.motiv.profile.presenter

import com.creat.motiv.profile.model.UserModel
import com.creat.motiv.profile.model.beans.Pics
import com.creat.motiv.profile.model.beans.User
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