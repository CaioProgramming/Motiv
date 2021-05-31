package com.ilustris.motiv.base.presenter

import com.ilustris.motiv.base.model.UserModel
import com.ilustris.motiv.base.beans.Pics
import com.ilustris.motiv.base.beans.User
import com.silent.ilustriscore.core.model.DTOMessage
import com.silent.ilustriscore.core.model.DataException
import com.silent.ilustriscore.core.model.ErrorType
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

    fun saveUser() {
        user?.let {
            it.getIdToken(true).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result.token?.let { uToken ->
                        val user = User.fromFirebase(it).apply {
                            token = uToken
                            picurl = ""
                        }
                        model.addData(user, user.uid)
                    }
                }
            }
        }
    }

    override val model: UserModel = UserModel(this)
}