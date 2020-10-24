package com.creat.motiv.contract

import com.creat.motiv.model.BaseModel
import com.creat.motiv.model.DTOMessage
import com.creat.motiv.model.beans.BaseBean
import com.creat.motiv.view.BaseView

interface PresenterContract<T> where T : BaseBean {

    val view: BaseView<T>
    val model: BaseModel<T>

    fun queryData(value: String, field: String)
    fun onDataRetrieve(data: List<T>)
    fun onSingleData(data: T)
    fun modelCallBack(dtoMessage: DTOMessage)
}