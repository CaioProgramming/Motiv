package com.creat.motiv.contract

import com.creat.motiv.model.beans.BaseBean
import com.creat.motiv.presenter.BasePresenter
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot

interface ModelContract<T> where T : BaseBean {

    val path: String
    val presenter: BasePresenter<T>
    fun deserializeDataSnapshot(dataSnapshot: DocumentSnapshot): T?
    fun deserializeDataSnapshot(dataSnapshot: QueryDocumentSnapshot): T?
    fun addData(data: T, forcedID: String? = null)
    fun editData(data: T)
    fun deleteData(id: String)
    fun query(query: String, field: String)
    fun getAllData()
    fun getSingleData(id: String)


}