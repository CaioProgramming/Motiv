package com.creat.motiv.presenter

import androidx.databinding.ViewDataBinding
import com.creat.motiv.model.BaseModel
import com.creat.motiv.model.Beans.Likes
import com.creat.motiv.model.LikeModel

class LikesPresenter(viewBinding: ViewDataBinding) : BasePresenter<Likes>(viewBinding) {


    override fun model(): BaseModel<Likes> {
        return LikeModel("", this)
    }

    override fun onLoading() {
        TODO("Not yet implemented")
    }

    override fun onLoadFinish() {
        TODO("Not yet implemented")
    }

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
        model().deleteData(likes.id)
    }
}