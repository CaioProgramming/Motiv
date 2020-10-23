package com.creat.motiv.view.binders

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import com.bumptech.glide.Glide
import com.creat.motiv.databinding.ProfileTopViewBinding
import com.creat.motiv.model.beans.User
import com.creat.motiv.presenter.UserPresenter
import com.creat.motiv.utils.Alert
import com.creat.motiv.utils.DialogStyles
import com.creat.motiv.view.BaseView
import com.creat.motiv.view.activities.UserActivity

class ProfileTopBinder(val uid: String,
                       val user: User?,
                       override val viewBind: ProfileTopViewBinding,
                       override val context: Context) : BaseView<User>() {


    override fun presenter() = UserPresenter(this)


    private fun showUserProfile(user: User) {
        if (user.uid != presenter().currentUser()?.uid) {
            val i = Intent(context, UserActivity::class.java)
            val bundle = Bundle()
            bundle.putSerializable("USER", user)
            i.putExtra("USER", bundle)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(context as Activity,
                    viewBind.profilepic as View, "profilepic")
            context.startActivity(i, options.toBundle())
        }

    }

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