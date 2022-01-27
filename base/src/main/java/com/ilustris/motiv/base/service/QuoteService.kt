package com.ilustris.motiv.base.service

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.ilustris.motiv.base.beans.quote.Quote
import com.silent.ilustriscore.core.bean.BaseBean
import com.silent.ilustriscore.core.model.BaseService
import com.silent.ilustriscore.core.model.DataException
import com.silent.ilustriscore.core.model.ServiceResult
import kotlinx.coroutines.tasks.await

class QuoteService : BaseService() {
    override val dataPath = "Quotes"

    override var requireAuth = true

    override fun deserializeDataSnapshot(dataSnapshot: DocumentSnapshot): BaseBean {
        return dataSnapshot.toObject(Quote::class.java)!!.apply {
            this.id = dataSnapshot.id
        }
    }

    override fun deserializeDataSnapshot(dataSnapshot: QueryDocumentSnapshot): BaseBean {
        return dataSnapshot.toObject(Quote::class.java).apply {
            this.id = dataSnapshot.id
        }
    }

    suspend fun findOnArray(
        field: String,
        value: String
    ): ServiceResult<DataException, ArrayList<BaseBean>> {
        Log.i(javaClass.simpleName, "query: Buscando por $value em $field na collection $dataPath")
        if (requireAuth && currentUser == null) return ServiceResult.Error(DataException.AUTH)
        val query = reference.whereArrayContains(field, value).get().await().documents
        return if (query.isNotEmpty()) {
            ServiceResult.Success(getDataList(query))
        } else {
            ServiceResult.Error(DataException.NOTFOUND)
        }
    }

}