package com.creat.motiv.profile.model

import android.net.Uri
import com.creat.motiv.profile.model.beans.Pics
import com.creat.motiv.model.beans.User
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.silent.ilustriscore.core.model.BaseModel
import com.silent.ilustriscore.core.model.DTOMessage
import com.silent.ilustriscore.core.presenter.BasePresenter
import com.silent.ilustriscore.core.utilities.ErrorType
import com.silent.ilustriscore.core.utilities.MessageType

class UserModel(val presenter: BasePresenter<User>) : BaseModel<User>(presenter) {

    override val path: String = "Users"


    fun updateUserPic(pic: Pics) {
        if (currentUser == null) {
            presenter.modelCallBack(DTOMessage("Usuário desconectado", MessageType.ERROR, ErrorType.USERNOTFOUND))
            return
        }

        val profileChangeRequest = UserProfileChangeRequest.Builder().setPhotoUri(Uri.parse(pic.uri)).build()
        currentUser?.updateProfile(profileChangeRequest)?.addOnCompleteListener {
            if (it.isSuccessful) {
                editField(pic.uri, currentUser!!.uid, "picurl")
            } else {
                presenter.modelCallBack(DTOMessage("Erro ao processar dados", MessageType.ERROR, ErrorType.UPDATE_ERROR))
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
