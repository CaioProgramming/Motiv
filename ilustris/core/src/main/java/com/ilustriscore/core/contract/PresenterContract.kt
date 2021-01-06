package com.ilustriscore.core.contract

import com.ilustriscore.core.base.BaseBean
import com.ilustriscore.core.base.BaseModel
import com.ilustriscore.core.base.BaseView
import com.ilustriscore.core.base.DTOMessage


interface PresenterContract<T> where T : BaseBean {

    val view: BaseView<T>
    val model: BaseModel<T>


    fun queryData(value: String, field: String)
    fun onDataRetrieve(data: List<T>)
    fun onSingleData(data: T)
    fun modelCallBack(dtoMessage: DTOMessage)
}