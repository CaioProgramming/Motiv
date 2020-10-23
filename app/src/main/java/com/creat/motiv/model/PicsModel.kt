package com.creat.motiv.model

import com.creat.motiv.model.beans.Pics
import com.creat.motiv.presenter.BasePresenter
import com.google.firebase.firestore.DocumentSnapshot

class PicsModel(override val presenter: BasePresenter<Pics>) : BaseModel<Pics>() {

    override val path = "Pics"

    override fun deserializeDataSnapshot(dataSnapshot: DocumentSnapshot): Pics? {
        return dataSnapshot.toObject(Pics::class.java)
    }
}