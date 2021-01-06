package com.ilustriscore.core.base

import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.ilustriscore.core.contract.PresenterContract
import com.ilustriscore.core.utilities.MessageType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


abstract class BasePresenter<T> : PresenterContract<T> where T : BaseBean {

    val currentUser: FirebaseUser?
        get() = model.currentUser


    fun loadData() {
        view.onLoading()
        model.getAllData()
    }

    protected fun loadSingleData(key: String) {
        view.onLoading()
        model.getSingleData(key)

    }


    fun saveData(data: T, forcedID: String? = null) {
        view.onLoading()
        if (forcedID.isNullOrBlank()) {
            model.addData(data)
        } else {
            model.addData(data, forcedID)
        }
        view.onLoadFinish()
    }

    override fun onDataRetrieve(data: List<T>) {
        GlobalScope.launch(Dispatchers.Main) {
            view.showListData(data)
            view.onLoadFinish()
        }

    }

    override fun onSingleData(data: T) {
        GlobalScope.launch(Dispatchers.Main) {
            view.showData(data)
            view.onLoadFinish()
        }

    }

    override fun modelCallBack(dtoMessage: DTOMessage) {
        GlobalScope.launch(Dispatchers.Main) {
            val priority = when (dtoMessage.type) {
                MessageType.ERROR -> Log.ERROR
                MessageType.SUCCESS -> Log.DEBUG
                MessageType.WARNING -> Log.WARN
                MessageType.INFO -> Log.INFO
            }
            Log.println(priority, javaClass.simpleName, dtoMessage.message)
            view.getCallBack(dtoMessage)
        }
    }


    override fun queryData(value: String, field: String) {
        view.onLoading()
        model.query(value, field)
    }

    fun findPreciseData(value: String, field: String) {
        view.onLoading()
        model.explicitSearch(value, field)
    }

}