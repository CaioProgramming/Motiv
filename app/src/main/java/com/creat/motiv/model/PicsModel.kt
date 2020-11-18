package com.creat.motiv.model


import com.creat.motiv.model.beans.Pics
import com.creat.motiv.presenter.BasePresenter
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.io.FileInputStream

class PicsModel(override val presenter: BasePresenter<Pics>) : BaseModel<Pics>() {

    override val path = "Icons"

    override fun addData(data: Pics, forcedID: String?) {
        val stream = FileInputStream(File(data.uri))
        val iconReference = Firebase.storage.reference.child(path).child(data.uri)
        val uploadTask = iconReference.putStream(stream)
        uploadTask.addOnFailureListener {
            errorMessage("Ocorre um erro ao enviar a imagem ${it.message}")
        }.addOnSuccessListener { task ->
            iconReference.downloadUrl.addOnSuccessListener {
                data.uri = it.toString()
                super.addData(data, null)
            }
        }.addOnFailureListener {
            errorMessage("Ocorre um erro ao enviar a imagem ${it.message}")
        }
    }

    override fun deserializeDataSnapshot(dataSnapshot: DocumentSnapshot): Pics? = dataSnapshot.toObject(Pics::class.java)


    override fun deserializeDataSnapshot(dataSnapshot: QueryDocumentSnapshot): Pics? = dataSnapshot.toObject(Pics::class.java)
}