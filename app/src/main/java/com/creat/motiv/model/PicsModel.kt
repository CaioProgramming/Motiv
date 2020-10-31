package com.creat.motiv.model

import com.creat.motiv.model.beans.Pics
import com.creat.motiv.presenter.BasePresenter
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot

class PicsModel(override val presenter: BasePresenter<Pics>) : BaseModel<Pics>() {

    override val path = "Pics"

    override fun deserializeDataSnapshot(dataSnapshot: DocumentSnapshot): Pics? = dataSnapshot.toObject(Pics::class.java)


    override fun deserializeDataSnapshot(dataSnapshot: QueryDocumentSnapshot): Pics? = dataSnapshot.toObject(Pics::class.java)
}