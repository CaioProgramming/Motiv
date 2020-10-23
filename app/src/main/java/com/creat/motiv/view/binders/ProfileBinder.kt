package com.creat.motiv.view.binders

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.creat.motiv.databinding.FragmentProfileBinding
import com.creat.motiv.model.beans.User
import com.creat.motiv.presenter.UserPresenter
import com.creat.motiv.view.BaseView
import com.creat.motiv.view.adapters.QuotesProfileAdapter

class ProfileBinder(override val context: Context,
                    override val viewBind: FragmentProfileBinding,
                    val user: User? = null) : BaseView<User>() {

    override fun presenter() = UserPresenter(this)

    override fun onLoading() {
        TODO("Not yet implemented")
    }

    override fun onLoadFinish() {
        TODO("Not yet implemented")
    }

    override fun initView() {
        if (user == null) presenter().getUser(presenter().currentUser()!!.uid) else showData(user)
    }

    override fun showData(data: User) {
        viewBind.run {
            ProfileTopBinder(data.uid, data, viewBind.profileTopView, context)
            setupRecycler()
        }

    }

    init {
        initView()
    }

    fun FragmentProfileBinding.setupRecycler() {
        profileRecycler.run {
            layoutManager = LinearLayoutManager(context, VERTICAL, false)
            adapter = QuotesProfileAdapter(context)
        }
    }


}