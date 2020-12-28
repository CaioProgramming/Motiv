package com.creat.motiv.view.binders

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.creat.motiv.databinding.QuoteRecyclerBinding
import com.creat.motiv.model.DTOMessage
import com.creat.motiv.model.beans.User
import com.creat.motiv.presenter.UserPresenter
import com.creat.motiv.utilities.*
import com.creat.motiv.view.BaseView
import com.creat.motiv.view.adapters.UserRecyclerAdapter


class UsersListBinder(override val context: Context,
                      override val viewBind: QuoteRecyclerBinding,
                      useinit: Boolean = true, val noResultCallback: (() -> Unit)? = null) : BaseView<User>(useinit) {


    override fun presenter() = UserPresenter(this)

    init {
        if (useInit) {
            initView()
        }
    }


    override fun onLoadFinish() {
        super.onLoadFinish()
        viewBind.loading.gone()
        if (viewBind.quotesrecyclerview.visibility == View.GONE) {
            viewBind.quotesrecyclerview.slideInBottom()
        }
    }

    override fun onLoading() {
        super.onLoading()
        viewBind.loading.fadeIn()
        viewBind.quotesrecyclerview.gone()
    }

    fun searchData(query: String) {
        presenter().queryData(query, "name")
    }


    override fun showListData(list: List<User>) {
        if (list.isEmpty()) {
            viewBind.quotesrecyclerview.fadeOut()
            noResultCallback?.invoke()
        } else {
            setupRecyclerView(list)
        }
    }

    private fun setupRecyclerView(list: List<User>) {
        viewBind.quotesrecyclerview.run {
            adapter = UserRecyclerAdapter(list)
            layoutManager = LinearLayoutManager(context, VERTICAL, false)
        }
    }

    override fun initView() {
        presenter().loadData()
    }

    override fun getCallBack(dtoMessage: DTOMessage) {
        super.getCallBack(dtoMessage)
        if (dtoMessage.operationType == OperationType.DATA_SAVED) {
            initView()
        }
    }

}