package com.creat.motiv.model

import com.creat.motiv.model.Beans.BaseBean

interface ModelContract<T> where T : BaseBean {

    fun addData(data: T, forcedID: String? = null)
    fun editData(data: T, id: String)
    fun deleteData(id: String)
    fun query(query: String, field: String)
    fun getAllData()


}