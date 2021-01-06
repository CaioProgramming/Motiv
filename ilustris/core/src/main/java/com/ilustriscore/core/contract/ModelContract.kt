package com.ilustriscore.core.contract

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.ilustriscore.core.base.BaseBean
import com.ilustriscore.core.base.BasePresenter

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