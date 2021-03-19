package com.creat.motiv.radio

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.silent.ilustriscore.core.model.BaseModel
import com.silent.ilustriscore.core.presenter.BasePresenter

class RadioModel(presenter: BasePresenter<Radio>) : BaseModel<Radio>(presenter) {
    override val path = "Radios"
    override fun deserializeDataSnapshot(dataSnapshot: DocumentSnapshot): Radio {
        return dataSnapshot.toObject(Radio::class.java)!!.apply {
            this.id = dataSnapshot.id
        }
    }

    override fun deserializeDataSnapshot(dataSnapshot: QueryDocumentSnapshot): Radio {
        return dataSnapshot.toObject(Radio::class.java).apply {
            this.id = dataSnapshot.id
        }
    }
}