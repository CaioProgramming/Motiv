package com.creat.motiv.view.binders

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import com.bumptech.glide.Glide
import com.creat.motiv.databinding.UserViewBinding
import com.creat.motiv.model.Beans.User
import com.creat.motiv.presenter.UserPresenter
import com.creat.motiv.utils.Tools.fadeIn
import com.creat.motiv.view.BaseView
import com.creat.motiv.view.activities.UserActivity

class UserTopBinder(val uid: String,
                    override val viewBind: UserViewBinding,
                    override val context: Context) : BaseView<User>() {


    override val presenter = UserPresenter(this)


    private fun showUserProfile(user: User) {
        val i = Intent(context, UserActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable("USER", user)
        i.putExtra("USER", bundle)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(context as Activity,
                viewBind.userpic as View, "profilepic")
        context.startActivity(i, options.toBundle())

    }

    override fun onLoading() {
        TODO("Not yet implemented")
    }

    override fun onLoadFinish() {
        TODO("Not yet implemented")
    }

    override fun initView() {
        if (presenter.currentUser?.uid == uid) {
            presenter.currentUser.getIdToken(true).addOnCompleteListener {
                if (it.isSuccessful) {
                    val userPresenter = UserPresenter(this)
                    userPresenter.onSingleData(
                            User(
                                    name = presenter.currentUser.displayName ?: "",
                                    picurl = presenter.currentUser.photoUrl?.path ?: "",
                                    uid = uid,
                                    token = it.result.token ?: ""
                            )
                    )
                }
            }
        } else {
            presenter.getUser(uid)
        }
    }


    override fun showData(data: User) {
        viewBind.user = data
        viewBind.userpic.setOnClickListener {
            showUserProfile(data)
        }
        Glide.with(context).load(data.picurl).into(viewBind.userpic)
    }

    fun animateView() {
        fadeIn(viewBind.userpic, 500).andThen(fadeIn(viewBind.username, 1000)).subscribe()
    }

}