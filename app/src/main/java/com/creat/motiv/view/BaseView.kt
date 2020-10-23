package com.creat.motiv.view

import android.util.Log
import com.creat.motiv.contract.ViewContract
import com.creat.motiv.model.beans.BaseBean

abstract class BaseView<T>(val useInit: Boolean = true) : ViewContract<T> where  T : BaseBean {


    override fun onLoading() {
        Log.i(javaClass.simpleName, "onLoading called")
    }

    override fun onLoadFinish() {
        Log.i(javaClass.simpleName, "onLoadFinish called")
    }

    override fun showListData(list: List<T>) {
        Log.i(javaClass.simpleName, "showListData: $list")
    }

    override fun showData(data: T) {
        Log.i(javaClass.simpleName, "showData: $data")
    }

    override fun success(message: String) {
        Log.d(javaClass.simpleName, "success: $message")
    }

    override fun error(message: String) {
        Log.e(javaClass.simpleName, "error: $message")
    }

}