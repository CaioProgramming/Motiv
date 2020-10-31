package com.creat.motiv.model

import android.util.Log
import com.creat.motiv.contract.ModelContract
import com.creat.motiv.model.beans.BaseBean
import com.creat.motiv.utilities.ErrorType
import com.creat.motiv.utilities.MessageType
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

abstract class BaseModel<T> : ModelContract<T>, OnCompleteListener<Void>, EventListener<QuerySnapshot> where T : BaseBean {


    fun db(): CollectionReference = FirebaseFirestore.getInstance().collection(path)


    val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser


    fun saveComplete(data: T): OnCompleteListener<DocumentReference> {
        return OnCompleteListener {
            if (it.isSuccessful) {
                presenter.modelCallBack(successMessage("Dados salvos com sucesso: $data"))
            } else {
                presenter.modelCallBack(errorMessage("Ocorreu um erro ao salvar os dados de $data \n ${it.exception?.message} "))
            }
        }
    }

    fun updateComplete(data: T): OnCompleteListener<Void> {
        return OnCompleteListener {
            if (it.isSuccessful) {
                presenter.modelCallBack(successMessage("Dados atualizados com sucesso: $data"))
            } else {
                presenter.modelCallBack(errorMessage("Ocorreu um erro ao atualizar os dados de $data \n ${it.exception?.message} "))
            }
        }
    }

    override fun addData(data: T, forcedID: String?) {
        GlobalScope.launch {
            if (forcedID.isNullOrEmpty()) {
                db().add(data).addOnCompleteListener(saveComplete(data))
            } else {
                editData(data)
            }
        }
    }

    fun errorMessage(message: String = "Ocorreu um erro ao processar", errorType: ErrorType = ErrorType.UNKNOW): DTOMessage = DTOMessage(message, MessageType.ERROR, errorType)
    fun successMessage(message: String = "Operação concluída com sucesso"): DTOMessage = DTOMessage(message, MessageType.SUCCESS)
    fun warningMessage(message: String = "Um erro inesperado aconteceu, recomenda-se verificar"): DTOMessage = DTOMessage(message, MessageType.WARNING)
    fun infoMessage(message: String): DTOMessage = DTOMessage(message, MessageType.INFO)

    override fun editData(data: T) {
        if (isDisconnected()) return
        Log.i(javaClass.simpleName, "editing: $data")
        db().document(data.id).set(data).addOnCompleteListener(updateComplete(data))
    }

    fun editField(data: Any, id: String, field: String) {
        if (isDisconnected()) return
        GlobalScope.launch {
            db().document(id).update(field, data).addOnCompleteListener(this@BaseModel)
        }

    }

    protected fun isDisconnected(): Boolean {
        if (currentUser == null) {
            presenter.modelCallBack(DTOMessage("Usuário desconectado", MessageType.ERROR))
            return true
        }
        return false
    }

    override fun deleteData(id: String) {
        if (isDisconnected()) return
        db().document(id).delete().addOnCompleteListener(this@BaseModel)
    }

    override fun query(query: String, field: String) {
        if (isDisconnected()) return
        presenter.modelCallBack(infoMessage("Buscando por $query em $field na collection $path"))
        db().whereEqualTo(field, query).addSnapshotListener(this)
    }

    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
        if (error != null) {
            presenter.modelCallBack(errorMessage("Erro ao receber dados ${error.message}"))
            return
        }
        val dataList: ArrayList<T> = ArrayList()
        for (doc in value!!) {
            deserializeDataSnapshot(doc)?.let { dataList.add(it) }
        }
        presenter.modelCallBack(successMessage("Dados recebidos: $dataList"))
        presenter.onDataRetrieve(dataList)

    }


    override fun getAllData() {
        if (isDisconnected()) return
        db().addSnapshotListener(this)
    }

    override fun getSingleData(id: String) {
        if (isDisconnected()) return
        Log.i(javaClass.name, "querying data $id")
        db().document(id).addSnapshotListener { snapshot, e ->
            if (e != null) {
                presenter.modelCallBack(errorMessage(e.message
                        ?: "Ocorreu um erro ao obter dados de $id"))
            }
            if (snapshot != null && snapshot.exists()) {
                deserializeDataSnapshot(snapshot)?.let { presenter.onSingleData(it) }
            } else {
                presenter.modelCallBack(errorMessage("Dados não encontrados para $id"))
            }
        }
    }


    override fun onComplete(task: Task<Void>) {
        if (task.isSuccessful) {
            presenter.modelCallBack(DTOMessage("Operação concluída", MessageType.SUCCESS))
        } else {
            presenter.modelCallBack(DTOMessage("Ocorreu um erro ao processar\n->${task.exception?.message}", MessageType.ERROR))
        }
    }

    fun deleteAllData(dataList: List<T>) {
        if (isDisconnected()) return
        GlobalScope.launch {
            try {
                for (data in dataList) {
                    if (data.id.isNotEmpty()) {
                        deleteData(data.id)
                    }
                }
            } catch (e: Exception) {
                presenter.modelCallBack(DTOMessage("Ocorreu um erro ${e.message}", MessageType.ERROR))
            }
        }
    }

}
