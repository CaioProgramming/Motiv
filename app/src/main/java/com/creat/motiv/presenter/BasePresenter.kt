package com.creat.motiv.presenter

import android.util.Log
import com.creat.motiv.contract.PresenterContract
import com.creat.motiv.model.DTOMessage
import com.creat.motiv.model.beans.BaseBean
import com.creat.motiv.utils.MessageType
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


abstract class BasePresenter<T> : PresenterContract<T> where T : BaseBean {

    fun currentUser(): FirebaseUser? = FirebaseAuth.getInstance().currentUser


    fun loadData() {
        model.getAllData()
    }

    protected fun loadSingleData(key: String) {
        model.getSingleData(key)
    }

    fun saveData(data: T, forcedID: String? = null) {
        view.onLoading()
        if (forcedID == null) {
            model.addData(data)
        } else {
            model.addData(data, forcedID)
        }
        view.onLoadFinish()
    }

    override fun onDataRetrieve(data: List<T>) {
        view.showListData(data)
    }

    override fun onSingleData(data: T) {
        view.showData(data)
    }

    override fun modelCallBack(dtoMessage: DTOMessage) {
        val priority = when (dtoMessage.type) {
            MessageType.ERROR -> Log.ERROR
            MessageType.SUCCESS -> Log.DEBUG
            MessageType.WARNING -> Log.WARN
        }
        Log.println(priority, javaClass.simpleName, dtoMessage.message)
    }


    override fun queryData(value: String, field: String) {
        model.query(value, field)
    }

}