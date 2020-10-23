package com.creat.motiv.presenter

import android.util.Log
import com.creat.motiv.model.beans.BaseBean
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


abstract class BasePresenter<T> : PresenterContract<T> where T : BaseBean {

    fun currentUser(): FirebaseUser? = FirebaseAuth.getInstance().currentUser


    fun loadData() {
        model.getAllData()
    }

    fun loadSingleData(key: String) {
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

    override fun onSuccess(message: String) {
        Log.d(javaClass.simpleName, "onSuccess called")
        view.success(message)
    }

    override fun onError(message: String) {
        Log.e(javaClass.simpleName, "onError called ")
        view.error(message)
    }

    override fun queryData(value: String, field: String) {
        model.query(value, field)
    }

}