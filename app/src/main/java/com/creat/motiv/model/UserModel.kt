package com.creat.motiv.model

import android.net.Uri
import com.creat.motiv.model.beans.Pics
import com.creat.motiv.model.beans.User
import com.creat.motiv.presenter.BasePresenter
import com.creat.motiv.utilities.ErrorType
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot

class UserModel(override val presenter: BasePresenter<User>) : BaseModel<User>() {

    override val path: String = "Users"

    override fun deserializeDataSnapshot(dataSnapshot: DocumentSnapshot): User? = dataSnapshot.toObject(User::class.java)


    override fun deserializeDataSnapshot(dataSnapshot: QueryDocumentSnapshot): User? = dataSnapshot.toObject(User::class.java)

    fun updateUserPic(pic: Pics) {
        if (currentUser == null) {
            presenter.modelCallBack(errorMessage("Usuário desconectado"))
            return
        }
        val profileChangeRequest = UserProfileChangeRequest.Builder().setPhotoUri(Uri.parse(pic.uri)).build()
        currentUser.updateProfile(profileChangeRequest).addOnCompleteListener {
            if (it.isSuccessful) {
                editField(pic.uri, currentUser.uid, "picurl")
            } else {
                presenter.modelCallBack(errorMessage("Erro ao processar ${it.exception?.message}"))
            }
        }

    }


    override fun addData(data: User, forcedID: String?) {
        if (!data.token.isBlank()) {
            super.addData(data, forcedID)
        }
    }

    override fun getSingleData(id: String) {
        if (isDisconnected()) return
        db().document(id).addSnapshotListener { snapshot, e ->
            if (e != null) {
                presenter.modelCallBack(errorMessage(e.message
                        ?: "Ocorreu um erro ao obter dados de $id"))
            }
            if (snapshot != null && snapshot.exists()) {
                deserializeDataSnapshot(snapshot)?.let {
                    if (it.token.isBlank()) {
                        presenter.modelCallBack(errorMessage("Usuário não encontrado, salvando ele na base de dados...", ErrorType.USERNOTFOUND))
                        currentUser!!.getIdToken(false).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                addData(User.fromFirebaseWithToken(currentUser, task.result.token!!), currentUser.uid)
                            } else {
                                errorMessage("Não foi possível obter o Token do usuário")
                            }
                        }
                    } else {
                        if (currentUser!!.uid == it.uid) {
                            currentUser.getIdToken(false).addOnSuccessListener { result ->
                                if (result.token != null && it.token != result.token) {
                                    it.token = result.token!!
                                    addData(it, it.id)
                                }
                            }
                        }
                    }
                    presenter.onSingleData(it)
                }
            } else {
                if (id == currentUser?.uid) {
                    presenter.modelCallBack(errorMessage("Usuário não encontrado, salvando ele na base de dados...", ErrorType.USERNOTFOUND))
                    currentUser.getIdToken(false).addOnCompleteListener {
                        if (it.isSuccessful) {
                            addData(User.fromFirebaseWithToken(currentUser, it.result.token!!), currentUser.uid)
                        } else {
                            errorMessage("Não foi possível obter o Token do usuário")
                        }
                    }
                } else {
                    presenter.modelCallBack(errorMessage("Dados não encontrados para $id", ErrorType.USERNOTFOUND))

                }
            }
        }
    }


    fun updateUserName(newName: String) {

        val profileChangeRequest = UserProfileChangeRequest.Builder().setDisplayName(newName).build()
        currentUser?.updateProfile(profileChangeRequest)?.addOnCompleteListener {
            if (it.isSuccessful) {
                editField(newName, currentUser.uid, "name")
            }
        }
    }


}
