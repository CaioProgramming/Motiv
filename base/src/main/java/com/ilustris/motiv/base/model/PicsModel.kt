package com.ilustris.motiv.base.model


import com.ilustris.motiv.base.beans.Pics
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.silent.ilustriscore.core.model.BaseModel
import com.silent.ilustriscore.core.presenter.BasePresenter

class PicsModel(presenter: BasePresenter<Pics>) : BaseModel<Pics>(presenter) {
    override val path = "Icons"

    override fun deserializeDataSnapshot(dataSnapshot: DocumentSnapshot): Pics {
        return dataSnapshot.toObject(Pics::class.java)!!.apply {
            id = dataSnapshot.id
        }
    }

    override fun deserializeDataSnapshot(dataSnapshot: QueryDocumentSnapshot): Pics {
        return dataSnapshot.toObject(Pics::class.java).apply {
            id = dataSnapshot.id
        }
    }


}