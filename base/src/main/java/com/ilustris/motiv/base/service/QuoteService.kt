package com.ilustris.motiv.base.service

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.ilustris.motiv.base.data.model.Quote
import com.silent.ilustriscore.core.bean.BaseBean
import com.silent.ilustriscore.core.model.BaseService
import com.silent.ilustriscore.core.model.DataException
import com.silent.ilustriscore.core.model.ServiceResult
import kotlinx.coroutines.tasks.await

class QuoteService : BaseService() {
    override val dataPath: String = "Quotes"
    override var requireAuth = true

    override fun deserializeDataSnapshot(dataSnapshot: DocumentSnapshot): BaseBean? =
        dataSnapshot.toObject(Quote::class.java).apply {
            this?.id = dataSnapshot.id
        }

    override fun deserializeDataSnapshot(dataSnapshot: QueryDocumentSnapshot): BaseBean =
        dataSnapshot.toObject(Quote::class.java).apply {
            this.id = dataSnapshot.id
        }

    suspend fun getFavorites(uid: String): ServiceResult<DataException, List<BaseBean>> {
        val field = "likes"
        Log.i(
            javaClass.simpleName,
            "query: searching for $uid at array $field on collection $dataPath"
        )
        if (requireAuth && currentUser() == null) return ServiceResult.Error(DataException.AUTH)
        val query =
            reference.orderBy("data").whereArrayContains(field, uid).get().await().documents
        return if (query.isNotEmpty()) {
            ServiceResult.Success(getDataList(query))
        } else {
            ServiceResult.Error(DataException.NOTFOUND)
        }
    }
}