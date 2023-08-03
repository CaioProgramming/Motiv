package com.ilustris.motiv.base.service

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.storage.FirebaseStorage
import com.ilustris.motiv.base.data.model.Radio
import com.ilustris.motiv.base.service.helper.RadioHelper
import com.silent.ilustriscore.core.bean.BaseBean
import com.silent.ilustriscore.core.model.BaseService
import com.silent.ilustriscore.core.model.DataException
import com.silent.ilustriscore.core.model.ServiceResult
import com.silent.ilustriscore.core.utilities.Ordering
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RadioService @Inject constructor(
    private val preferencesService: PreferencesService,
    private val radioHelper: RadioHelper
) : BaseService() {
    override val dataPath: String = "Radios"
    override var requireAuth = true
    private fun storageInstance() = FirebaseStorage.getInstance()
    private fun storageReference() = FirebaseStorage.getInstance().reference.child(dataPath)
    private fun getRadioLocalFile(radioId: String) =
        radioHelper.getRadioFile(preferencesService.getStringValue(radioId))

    override fun deserializeDataSnapshot(dataSnapshot: DocumentSnapshot): BaseBean? =
        dataSnapshot.toObject(Radio::class.java)?.apply {
            id = dataSnapshot.id
            val localRadioFile = getRadioLocalFile(dataSnapshot.id)
            if (localRadioFile == null) {
                saveRadioFile(this)
            } else {
                url = localRadioFile.absolutePath
            }
        }

    private fun saveRadioFile(radio: Radio) {
        val radioFile = radioHelper.createRadioFile(radio.name)
        val downloadReference = storageInstance().getReferenceFromUrl(radio.url)
        downloadReference.getFile(radioFile).addOnCompleteListener {
            if (it.isSuccessful) {
                preferencesService.editPreference(radio.id, radioFile.path).run {
                    Log.i(javaClass.simpleName, "saveRadioFile: $this")
                }
            }
        }
    }

    override fun deserializeDataSnapshot(dataSnapshot: QueryDocumentSnapshot): BaseBean =
        dataSnapshot.toObject(Radio::class.java).apply {
            id = dataSnapshot.id
            val localRadioFile = getRadioLocalFile(dataSnapshot.id)
            if (localRadioFile == null) {
                saveRadioFile(this)
            } else {
                url = localRadioFile.absolutePath
            }
        }


    override suspend fun getAllData(
        limit: Long,
        orderBy: String,
        ordering: Ordering
    ): ServiceResult<DataException, ArrayList<BaseBean>> {
        return super.getAllData(100, "name", ordering)
    }

    override suspend fun addData(data: BaseBean): ServiceResult<DataException, BaseBean> {
        return try {
            val radio = data as Radio
            val uploadMusic =
                storageReference().child(radio.name).putFile(Uri.parse(radio.url)).await()
            return if (uploadMusic.task.isSuccessful) {
                radio.url = uploadMusic.storage.downloadUrl.await().toString()
                super.addData(radio)
            } else {
                ServiceResult.Error(DataException.UPLOAD)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ServiceResult.Error(DataException.SAVE)
        }

    }

    override suspend fun deleteData(id: String): ServiceResult<DataException, Boolean> {
        return try {
            val data = getSingleData(id).success.data as Radio
            storageReference().child(data.name).delete().await()
            return deleteData(id)
        } catch (e: Exception) {
            e.printStackTrace()
            ServiceResult.Error(DataException.DELETE)
        }
        return super.deleteData(id)
    }
}