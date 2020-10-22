package com.creat.motiv.presenter

import com.creat.motiv.model.Beans.Pics
import com.creat.motiv.model.Beans.User
import com.creat.motiv.model.UserModel
import com.creat.motiv.view.BaseView

class UserPresenter(override val view: BaseView<User>) : BasePresenter<User>() {

    fun getUser(uid: String) {
        model.getSingleData(uid)
    }

    fun changeProfilePic(pics: Pics) {
        model.updateUserPic(pics)
    }

    override fun onDataRetrieve(data: List<User>) {
        TODO("Not yet implemented")
    }

    override fun onSingleData(data: User) {
        view.showData(data)
    }

    override fun onError() {
        TODO("Not yet implemented")
    }

    override fun onSuccess() {
        TODO("Not yet implemented")
    }

    override val model: UserModel = UserModel(this)
}