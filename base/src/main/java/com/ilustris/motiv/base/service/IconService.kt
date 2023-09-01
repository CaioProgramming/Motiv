package com.ilustris.motiv.base.service

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.storage.FirebaseStorage
import com.ilustris.motiv.base.data.model.Icon
import com.silent.ilustriscore.core.bean.BaseBean
import com.silent.ilustriscore.core.contract.DataError
import com.silent.ilustriscore.core.contract.ServiceResult
import com.silent.ilustriscore.core.service.BaseService
import kotlinx.coroutines.tasks.await

class IconService : BaseService() {
    override val dataPath: String = "Icons"
    override var requireAuth = true
    private fun storageReference() = FirebaseStorage.getInstance().reference.child(dataPath)


    override fun deserializeDataSnapshot(dataSnapshot: DocumentSnapshot): Icon? =
        dataSnapshot.toObject(Icon::class.java).apply {
            this?.id = dataSnapshot.id
        }

    override fun deserializeDataSnapshot(dataSnapshot: QueryDocumentSnapshot): Icon =
        dataSnapshot.toObject(Icon::class.java).apply {
            this.id = dataSnapshot.id
        }


    override suspend fun addData(data: BaseBean): ServiceResult<DataError, BaseBean> {
        return try {
            val icon = data as Icon
            val uploadTask =
                storageReference().child(icon.id.trim()).putFile(Uri.parse(icon.uri)).await()
            return if (uploadTask.task.isSuccessful) {
                icon.uri = uploadTask.storage.downloadUrl.await().toString()
                super.addData(icon)
            } else {
                Log.e(
                    javaClass.simpleName,
                    "addData: Error uploading icon -> ${uploadTask.error?.message} ",
                )
                ServiceResult.Error(DataError.Upload)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ServiceResult.Error(DataError.Save)
        }
    }

    override suspend fun deleteData(id: String): ServiceResult<DataError, Boolean> {
        return try {
            val data = getSingleData(id).success.data as Icon
            storageReference().child(data.id.trim()).delete().await()
            return deleteData(id)
        } catch (e: Exception) {
            e.printStackTrace()
            ServiceResult.Error(DataError.Unknown(e.message))
        }
    }

    override suspend fun editData(data: BaseBean): ServiceResult<DataError, BaseBean> {
        return try {
            val icon = data as Icon
            val uploadTask =
                storageReference().child(icon.id.trim()).putFile(Uri.parse(icon.uri)).await()
            return if (uploadTask.task.isSuccessful) {
                icon.uri = uploadTask.storage.downloadUrl.await().toString()
                super.editData(icon)
            } else {
                Log.e(
                    javaClass.simpleName,
                    "addData: Error uploading icon -> ${uploadTask.error?.message} ",
                )
                ServiceResult.Error(DataError.Upload)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ServiceResult.Error(DataError.Unknown(e.message))
        }
    }
}