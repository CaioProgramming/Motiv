package com.creat.motiv.model

import com.creat.motiv.model.beans.BaseBean
import com.creat.motiv.presenter.BasePresenter
import com.google.firebase.database.DataSnapshot

interface ModelContract<T> where T : BaseBean {

    val path: String
    val presenter: BasePresenter<T>
    fun deserializeDataSnapshot(dataSnapshot: DataSnapshot): T?
    fun addData(data: T, forcedID: String? = null)
    fun editData(data: T, id: String)
    fun deleteData(id: String)
    fun query(query: String, field: String)
    fun getAllData()
    fun getSingleData(id: String)


}