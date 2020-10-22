package com.creat.motiv.view

import android.util.Log
import com.creat.motiv.contract.ViewContract
import com.creat.motiv.model.Beans.BaseBean

abstract class BaseView<T> : ViewContract<T> where  T : BaseBean {



    init {
        initView()
    }


    override fun showListData(list: List<T>) {
        Log.d(javaClass.name, "showListData: $list")
    }

    override fun showData(data: T) {
        Log.d(javaClass.name, "showData: $data")
    }

}