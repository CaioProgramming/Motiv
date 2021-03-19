package com.ilustris.motiv.base.model


import android.net.Uri
import com.ilustris.motiv.base.beans.Pics
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.storage.FirebaseStorage
import com.silent.ilustriscore.core.model.BaseModel
import com.silent.ilustriscore.core.model.DataException
import com.silent.ilustriscore.core.model.ErrorType
import com.silent.ilustriscore.core.presenter.BasePresenter
import java.io.File
import java.io.FileInputStream

class PicsModel(val presenter: BasePresenter<Pics>) : BaseModel<Pics>(presenter) {
    override val path = "Icons"


    override fun addData(data: Pics, forcedID: String?) {
        try {
            val file = File(data.uri)
            val uriFile = Uri.fromFile(file)
            val storageRef = FirebaseStorage.getInstance().reference
            val iconRef = storageRef.child("$path/${file.name}")
            val uploadTask = iconRef.putFile(uriFile)
            uploadTask.addOnFailureListener {
                presenter.errorCallBack(DataException("Ocorreu um erro ao salvar o ícone ${it.message}", ErrorType.SAVE))
            }
            uploadTask.addOnSuccessListener {
                val downloadUrl = it.storage.downloadUrl
                downloadUrl.addOnSuccessListener { downloadUrl ->
                    data.uri = downloadUrl.toString()
                    data.id = file.name
                    super.addData(data, forcedID)
                }
                downloadUrl.addOnFailureListener {
                    presenter.errorCallBack(DataException("Ocorreu um erro ao salvar o ícone ${it.message}", ErrorType.SAVE))
                }
            }
        } catch (e: Exception) {
            presenter.errorCallBack(DataException.fromException(e))
        }


    }


    override fun deleteData(id: String) {
        val storageRef = FirebaseStorage.getInstance().reference
        val iconsRef = storageRef.child("$path/$id")
        iconsRef.delete().addOnSuccessListener {
            super.deleteData(id)
        }.addOnFailureListener {
            presenter.errorCallBack(DataException("Ocorreu um erro ao deletar o arquivo", ErrorType.DELETE))
        }

    }

    override fun deserializeDataSnapshot(dataSnapshot: DocumentSnapshot): Pics {
        return dataSnapshot.toObject(Pics::class.java)!!.apply {
            id = dataSnapshot.id
        }
    }

    override fun deserializeDataSnapshot(dataSnapshot: QueryDocumentSnapshot): Pics {
        return dataSnapshot.toObject(Pics::class.java).apply {
            id = dataSnapshot.id
        }
    }


}