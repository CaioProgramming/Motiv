package com.creat.motiv.presenter

import com.creat.motiv.model.BaseModel
import com.creat.motiv.model.Beans.Likes
import com.creat.motiv.model.LikeModel
import com.creat.motiv.view.BaseView

class LikesPresenter(override val view: BaseView<Likes>, val quoteID: String) : BasePresenter<Likes>() {


    override val model: BaseModel<Likes> = LikeModel(quoteID, this)


    override fun onDataRetrieve(data: List<Likes>) {
        TODO("Not yet implemented")
    }

    override fun onSingleData(data: Likes) {
        TODO("Not yet implemented")
    }

    override fun onError() {
        TODO("Not yet implemented")
    }

    override fun onSuccess() {
        TODO("Not yet implemented")
    }

    fun likeQuote(likes: Likes) {

        saveData(likes, currentUser!!.uid)
    }


    fun deslikeQuote(likes: Likes) {
        if (currentUser == null) {
            onError()
        }
        likes.id.let { model.deleteData(it) }
    }
}