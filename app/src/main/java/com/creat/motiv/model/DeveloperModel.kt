package com.creat.motiv.model

import com.creat.motiv.model.beans.Developer
import com.creat.motiv.presenter.BasePresenter
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot

class DeveloperModel(override val presenter: BasePresenter<Developer>) : BaseModel<Developer>() {

    override val path: String = "Developers"
    override fun deserializeDataSnapshot(dataSnapshot: DocumentSnapshot): Developer? = dataSnapshot.toObject(Developer::class.java)
    override fun deserializeDataSnapshot(dataSnapshot: QueryDocumentSnapshot): Developer? = dataSnapshot.toObject(Developer::class.java)
}