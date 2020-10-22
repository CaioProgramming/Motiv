package com.creat.motiv.presenter

import com.creat.motiv.model.BaseModel
import com.creat.motiv.model.Beans.BaseBean
import com.creat.motiv.view.BaseView

interface PresenterContract<T> where T : BaseBean {

    val view: BaseView<T>
    val model: BaseModel<T>

    fun onDataRetrieve(data: List<T>)
    fun onSingleData(data: T)
    fun onError()
    fun onSuccess()
}