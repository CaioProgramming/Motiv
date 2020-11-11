package com.creat.motiv.view

import android.util.Log
import com.creat.motiv.contract.ViewContract
import com.creat.motiv.model.DTOMessage
import com.creat.motiv.model.beans.BaseBean
import com.creat.motiv.utilities.MessageType

abstract class BaseView<T>(val useInit: Boolean = true) : ViewContract<T> where  T : BaseBean {


    override fun getCallBack(dtoMessage: DTOMessage) {
        val priority = when (dtoMessage.type) {
            MessageType.ERROR -> Log.ERROR
            MessageType.SUCCESS -> Log.DEBUG
            MessageType.WARNING -> Log.WARN
            MessageType.INFO -> Log.INFO
        }
        Log.println(priority, javaClass.simpleName, dtoMessage.message)
    }

    override fun onLoading() {
        Log.i(javaClass.simpleName, "onLoading called")
    }

    override fun onLoadFinish() {
        Log.i(javaClass.simpleName, "onLoadFinish called")
    }

    override fun showListData(list: List<T>) {
        Log.i(javaClass.simpleName, "showListData: $list")
        onLoadFinish()
    }

    override fun showData(data: T) {
        Log.i(javaClass.simpleName, "showData: $data")
        onLoadFinish()
    }

    override fun success(message: String) {
        Log.d(javaClass.simpleName, "success: $message")
    }

    override fun error(message: String) {
        Log.e(javaClass.simpleName, "error: $message")
    }

}