package com.ilustris.motiv.base.service

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.ilustris.motiv.base.data.model.Quote
import com.silent.ilustriscore.core.bean.BaseBean
import com.silent.ilustriscore.core.contract.DataError
import com.silent.ilustriscore.core.contract.ServiceResult
import com.silent.ilustriscore.core.service.BaseService
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

    suspend fun getFavorites(uid: String): ServiceResult<DataError, List<BaseBean>> {
        if (requireAuth && getCurrentUser() == null) return ServiceResult.Error(DataError.Auth)

        val field = "likes"
        Log.i(
            javaClass.simpleName,
            "query: searching for $uid at array $field on collection $dataPath"
        )
        val query =
            reference.orderBy("data").whereArrayContains(field, uid).get().await().documents
        return if (query.isNotEmpty()) {
            ServiceResult.Success(getDataList(query))
        } else {
            ServiceResult.Error(DataError.NotFound)
        }
    }
}