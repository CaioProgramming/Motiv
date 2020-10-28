package com.creat.motiv.view.binders

import android.app.Activity
import android.content.Context
import com.bumptech.glide.Glide
import com.creat.motiv.databinding.ProfileTopViewBinding
import com.creat.motiv.model.beans.User
import com.creat.motiv.presenter.UserPresenter
import com.creat.motiv.utilities.Alert
import com.creat.motiv.utilities.DialogStyles
import com.creat.motiv.view.BaseView

class ProfileTopBinder(val uid: String,
                       val user: User?,
                       override val viewBind: ProfileTopViewBinding,
                       override val context: Context) : BaseView<User>() {


    override fun presenter() = UserPresenter(this)




    override fun onLoading() {
        TODO("Not yet implemented")
    }

    override fun onLoadFinish() {
        TODO("Not yet implemented")
    }

    override fun initView() {
        if (user != null) {
            showData(user)
        } else {
            val firebaseUser = presenter().currentUser()
            if (firebaseUser?.uid == uid) {
                firebaseUser.getIdToken(true).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val userPresenter = UserPresenter(this)
                        userPresenter.onSingleData(
                                User(
                                        name = firebaseUser.displayName ?: "",
                                        picurl = firebaseUser.photoUrl?.path ?: "",
                                        uid = uid,
                                        token = it.result.token ?: ""
                                )
                        )
                    }
                }
            } else {
                presenter().getUser(uid)
            }
        }
    }


    override fun showData(data: User) {
        viewBind.run {
            userData = data
            profilepic.setOnClickListener {
                if (user?.uid == presenter().currentUser()?.uid) {
                    Alert(context as Activity, DialogStyles.BOTTOM_NO_BORDER).picturePicker(presenter())
                }
            }
            Glide.with(context).load(data.picurl).into(profilepic)
        }
    }

    init {
        initView()
    }
}