package com.creat.motiv.presenter

import com.creat.motiv.model.UserModel
import com.creat.motiv.model.beans.Pics
import com.creat.motiv.model.beans.User
import com.creat.motiv.view.BaseView

class UserPresenter(override val view: BaseView<User>) : BasePresenter<User>() {

    fun getUser(uid: String) {
        currentUser()?.let {
            if (it.uid == uid) {
                view.onLoadFinish()
                onSingleData(User.fromFirebase(it))
                return
            }
        }
        model.getSingleData(uid)
    }

    override fun onSingleData(data: User) {
        view.onLoadFinish()
        view.showData(data)
    }

    fun changeProfilePic(pics: Pics) {

        model.updateUserPic(pics)
    }

    fun changeUserName(newName: String) {
        view.onLoading()
        model.updateUserName(newName)
        view.onLoadFinish()
    }


    override val model: UserModel = UserModel(this)
}