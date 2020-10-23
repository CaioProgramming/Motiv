package com.creat.motiv.model

import com.creat.motiv.model.beans.Likes
import com.creat.motiv.presenter.BasePresenter
import com.google.firebase.firestore.DocumentSnapshot

class LikeModel(val quoteID: String, override val presenter: BasePresenter<Likes>) : BaseModel<Likes>() {
    override val path = "Quotes/$quoteID/Likes"


    override fun deserializeDataSnapshot(dataSnapshot: DocumentSnapshot): Likes? {
        return dataSnapshot.toObject(Likes::class.java)
    }


}