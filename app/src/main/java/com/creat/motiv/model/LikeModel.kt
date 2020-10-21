package com.creat.motiv.model

import com.creat.motiv.model.Beans.Likes
import com.creat.motiv.presenter.BasePresenter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LikeModel(val quoteID: String, presenter: BasePresenter<Likes>) : BaseModel<Likes>(presenter) {


    override fun reference(): DatabaseReference {
        return FirebaseDatabase.getInstance().reference.child("Quotes").child(quoteID).child("likes")
    }

}