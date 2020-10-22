package com.creat.motiv.presenter

import android.util.Log
import com.creat.motiv.model.Beans.BaseBean
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


abstract class BasePresenter<T> : PresenterContract<T> where T : BaseBean {

    val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    fun loadData() {
        model.getAllData()
    }

    fun saveData(data: T, forcedID: String? = null) {
        if (currentUser == null) {
            onError()
            return
        }
        if (forcedID == null) {
            model.addData(data)
        } else {
            model.addData(data, forcedID)
        }
    }

    override fun onDataRetrieve(data: List<T>) {
        view.showListData(data)
    }

    override fun onSingleData(data: T) {
        view.showData(data)
    }

    override fun onSuccess() {
        Log.d(javaClass.simpleName, "onSuccess called")
    }

    override fun onError() {
        Log.e(javaClass.simpleName, "onError called ")
    }


}