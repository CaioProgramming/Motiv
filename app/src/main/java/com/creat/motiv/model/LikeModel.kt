package com.creat.motiv.model

import com.creat.motiv.model.beans.Likes
import com.creat.motiv.presenter.BasePresenter
import com.google.firebase.database.DataSnapshot

class LikeModel(val quoteID: String, override val presenter: BasePresenter<Likes>) : BaseModel<Likes>() {
    override val path = "Quotes/$quoteID/Likes"

    override fun deserializeDataSnapshot(dataSnapshot: DataSnapshot): Likes? {
        return Likes().convertSnapshot(dataSnapshot)
    }


}