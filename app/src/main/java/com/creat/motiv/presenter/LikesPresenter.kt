package com.creat.motiv.presenter

import com.creat.motiv.model.BaseModel
import com.creat.motiv.model.LikeModel
import com.creat.motiv.model.beans.Likes
import com.creat.motiv.view.BaseView

class LikesPresenter(override val view: BaseView<Likes>, quoteID: String) : BasePresenter<Likes>() {


    override val model: BaseModel<Likes> = LikeModel(quoteID, this)


    fun likeQuote() {
        currentUser()?.let {
            val like = Likes(
                    it.uid,
                    it.displayName!!,
                    it.photoUrl!!.path!!
            )
            saveData(like, it.uid)
        }

    }


    fun deslikeQuote() {
        currentUser()?.let {
            model.deleteData(it.uid)
        }
    }
}