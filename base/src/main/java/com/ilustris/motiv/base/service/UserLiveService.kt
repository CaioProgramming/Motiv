package com.ilustris.motiv.base.service

import android.net.Uri
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.ilustris.motiv.base.data.model.User
import com.silent.ilustriscore.core.bean.BaseBean
import com.silent.ilustriscore.core.contract.DataError
import com.silent.ilustriscore.core.contract.ServiceResult
import com.silent.ilustriscore.core.service.BaseLiveService
import kotlinx.coroutines.tasks.await

class UserLiveService : BaseLiveService() {
    override val dataPath: String = "Users"
    override val offlineEnabled: Boolean = true
    override val requireAuth: Boolean = true

    override fun deserializeDataSnapshot(dataSnapshot: DocumentSnapshot): User? =  dataSnapshot.toObject(
        User::class.java
    )

    override fun deserializeDataSnapshot(dataSnapshot: QueryDocumentSnapshot): User =  dataSnapshot.toObject(
        User::class.java
    )

    suspend fun saveNewUser(icon: String): ServiceResult<DataError, BaseBean> {
        getCurrentUser()!!.run {
            val profileChangeRequest =
                UserProfileChangeRequest.Builder().setPhotoUri(Uri.parse(icon)).build()
            updateProfile(profileChangeRequest).await()
            val newUser = User(
                uid = uid,
                picurl = icon,
                name = displayName ?: "",
            )
            return addData(newUser)
        }
    }

    suspend fun updateUserName(name: String): ServiceResult<DataError, String> {
        try {
            getCurrentUser()!!.run {
                val profileChangeRequest =
                    UserProfileChangeRequest.Builder().setDisplayName(name).build()
                this.updateProfile(profileChangeRequest).await()
                return editField("name", uid, name)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return ServiceResult.Error(DataError.Unknown(e.message))
        }
    }
}