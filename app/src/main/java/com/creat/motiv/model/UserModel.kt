package com.creat.motiv.model

import android.net.Uri
import com.creat.motiv.model.Beans.Pics
import com.creat.motiv.model.Beans.User
import com.creat.motiv.presenter.BasePresenter
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DataSnapshot

class UserModel(override val presenter: BasePresenter<User>) : BaseModel<User>() {

    override val path: String = "Users"
    override fun deserializeDataSnapshot(dataSnapshot: DataSnapshot): User? {
        return User().convertSnapshot(dataSnapshot)
    }

    fun updateUserPic(pic: Pics) {
        if (currentUser == null) {
            presenter.onError()
            return
        }
        val profileChangeRequest = UserProfileChangeRequest.Builder().setPhotoUri(Uri.parse(pic.uri)).build()
        currentUser.updateProfile(profileChangeRequest).addOnCompleteListener {
            if (it.isSuccessful) {
                editField(pic.uri, currentUser.uid, "picurl")
            }
        }

    }
}
