package com.ilustris.motiv.base.model

import android.net.Uri
import com.ilustris.motiv.base.beans.Pics
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.ilustris.motiv.base.beans.User
import com.silent.ilustriscore.core.model.BaseModel
import com.silent.ilustriscore.core.model.DataException
import com.silent.ilustriscore.core.model.ErrorType
import com.silent.ilustriscore.core.presenter.BasePresenter

class UserModel(val presenter: BasePresenter<User>) : BaseModel<User>(presenter) {

    override val path: String = "Users"


    fun updateUserPic(pic: Pics) {


        val profileChangeRequest = UserProfileChangeRequest.Builder().setPhotoUri(Uri.parse(pic.uri)).build()
        currentUser?.updateProfile(profileChangeRequest)?.addOnCompleteListener {
            if (it.isSuccessful) {
                editField(pic.uri, currentUser!!.uid, "picurl")
            } else {
                presenter.errorCallBack(DataException("Ocorreu um erro ao atualizar os dados do usuário", ErrorType.UPDATE))
            }
        }

    }

    override fun addData(data: User, forcedID: String?) {
        if (data.token.isNotBlank()) {
            super.addData(data, forcedID)
        }
    }


    fun updateUserName(newName: String) {
        val profileChangeRequest = UserProfileChangeRequest.Builder().setDisplayName(newName).build()
        currentUser?.updateProfile(profileChangeRequest)?.addOnCompleteListener {
            if (it.isSuccessful) {
                editField(newName, currentUser!!.uid, "name")
            }
        }
    }

    override fun deserializeDataSnapshot(dataSnapshot: DocumentSnapshot): User {
        return dataSnapshot.toObject(User::class.java)!!.apply {
            uid = dataSnapshot.id
        }
    }

    override fun deserializeDataSnapshot(dataSnapshot: QueryDocumentSnapshot): User {
        return dataSnapshot.toObject(User::class.java).apply {
            uid = dataSnapshot.id
        }
    }


}
