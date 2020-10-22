package com.creat.motiv.contract

import android.content.Context
import androidx.databinding.ViewDataBinding
import com.creat.motiv.model.Beans.BaseBean
import com.creat.motiv.presenter.BasePresenter


/**
 * @Author Kotlin MVP Plugin
 * @Date 2019/10/15
 * @Description input description
 **/
interface ViewContract<T> where  T : BaseBean {

    val context: Context
    val viewBind: ViewDataBinding
    val presenter: BasePresenter<T>
    fun onLoading()
    fun onLoadFinish()
    fun initView()
    fun showListData(list: List<T>)
    fun showData(data: T)


}
