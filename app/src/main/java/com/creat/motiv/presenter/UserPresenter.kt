package com.creat.motiv.presenter

import com.creat.motiv.model.UserModel
import com.creat.motiv.model.beans.Pics
import com.creat.motiv.model.beans.User
import com.creat.motiv.view.BaseView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserPresenter(override val view: BaseView<User>) : BasePresenter<User>() {

    fun getUser(uid: String = currentUser!!.uid) {
        GlobalScope.launch {
            model.getSingleData(uid)
        }
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