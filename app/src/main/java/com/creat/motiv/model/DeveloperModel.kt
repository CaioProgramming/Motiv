package com.creat.motiv.model

import com.creat.motiv.model.beans.Developer
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.silent.ilustriscore.core.model.BaseModel
import com.silent.ilustriscore.core.presenter.BasePresenter

class DeveloperModel(presenter: BasePresenter<Developer>) : BaseModel<Developer>(presenter) {

    override val path: String = "Developers"
    override fun deserializeDataSnapshot(dataSnapshot: DocumentSnapshot): Developer = dataSnapshot.toObject(Developer::class.java)!!.apply { id = dataSnapshot.id }
    override fun deserializeDataSnapshot(dataSnapshot: QueryDocumentSnapshot): Developer = dataSnapshot.toObject(Developer::class.java).apply { id = dataSnapshot.id }
}