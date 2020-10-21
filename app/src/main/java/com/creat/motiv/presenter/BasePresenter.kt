package com.creat.motiv.presenter

import androidx.databinding.ViewDataBinding
import com.creat.motiv.model.BaseModel
import com.creat.motiv.model.Beans.BaseBean
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


abstract class BasePresenter<T>(val viewBinding: ViewDataBinding) : PresenterContract<T> where T : BaseBean {


    val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    abstract fun model(): BaseModel<T>

    fun loadData() {
        model().getAllData()
    }

    fun saveData(data: T, forcedID: String?) {
        if (currentUser == null) {
            onError()
            return
        }
        if (forcedID == null) {
            model().addData(data)
        } else {
            model().addData(data, forcedID)
        }
    }


}