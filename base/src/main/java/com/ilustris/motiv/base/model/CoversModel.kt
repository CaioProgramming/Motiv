package com.ilustris.motiv.base.model

import com.ilustris.motiv.base.beans.Cover
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.silent.ilustriscore.core.model.BaseModel
import com.silent.ilustriscore.core.presenter.BasePresenter

class CoversModel(presenter: BasePresenter<Cover>) : BaseModel<Cover>(presenter) {
    override val path = "Covers"

    override fun deserializeDataSnapshot(dataSnapshot: DocumentSnapshot): Cover {
        return dataSnapshot.toObject(Cover::class.java)!!.apply {
            id = dataSnapshot.id
        }
    }

    override fun deserializeDataSnapshot(dataSnapshot: QueryDocumentSnapshot): Cover {
        return dataSnapshot.toObject(Cover::class.java).apply {
            id = dataSnapshot.id
        }
    }
}