package com.creat.motiv.view.binders

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.creat.motiv.databinding.FragmentProfileBinding
import com.creat.motiv.model.beans.User
import com.creat.motiv.presenter.UserPresenter
import com.creat.motiv.view.adapters.QuotesProfileAdapter
import com.ilustriscore.core.base.BaseView

class ProfileBinder(override val context: Context,
                    override val viewBind: FragmentProfileBinding,
                    val user: User? = null) : BaseView<User>() {

    override fun presenter() = UserPresenter(this)


    override fun initView() {
        if (user == null) {
            presenter().getUser()
        } else {
            showData(user)
        }
    }

    override fun showData(data: User) {
        onLoadFinish()
        viewBind.run {
            toolbar.title = data.name
            ProfileTopBinder(uid = data.uid, user = data, viewBind = viewBind.profileTopView, context = context)
            profileRecycler.run {
                layoutManager = LinearLayoutManager(context, VERTICAL, false)
                adapter = QuotesProfileAdapter(context, data.uid)
            }
            if (context is AppCompatActivity) {
                val activity = context
                activity.supportActionBar?.title = data.name
            }
        }
    }

    init {
        initView()
    }
}