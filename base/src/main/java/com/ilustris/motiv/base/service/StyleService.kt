package com.ilustris.motiv.base.service

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.ilustris.motiv.base.beans.Quote
import com.ilustris.motiv.base.beans.Style
import com.silent.ilustriscore.core.bean.BaseBean
import com.silent.ilustriscore.core.model.BaseService
import com.silent.ilustriscore.core.model.DataException
import com.silent.ilustriscore.core.model.ServiceResult

class StyleService : BaseService() {
    override val dataPath = "Styles"

    override suspend fun getSingleData(id: String): ServiceResult<DataException, BaseBean> {
        return if (Style.isPreSavedStyle(id)) {
            val preSaveStyle = Style.getPreSaveStyle(id)
            if (preSaveStyle != null) {
                ServiceResult.Success(preSaveStyle)
            } else {
                ServiceResult.Error(DataException.NOTFOUND)
            }
        } else {
            super.getSingleData(id)
        }
    }

    override fun deserializeDataSnapshot(dataSnapshot: DocumentSnapshot): Style {
        return dataSnapshot.toObject(Style::class.java)!!.apply {
            this.id = dataSnapshot.id
        }
    }

    override fun deserializeDataSnapshot(dataSnapshot: QueryDocumentSnapshot): Style {
        return dataSnapshot.toObject(Style::class.java).apply {
            this.id = dataSnapshot.id
        }
    }

}