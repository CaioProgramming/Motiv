package com.creat.motiv.model

import com.creat.motiv.contract.ModelContract
import com.creat.motiv.model.beans.BaseBean
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

abstract class BaseModel<T> : ModelContract<T>, OnCompleteListener<Void> where T : BaseBean {


    fun db() = FirebaseFirestore.getInstance().collection(path)


    val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser



    override fun addData(data: T, forcedID: String?) {
        GlobalScope.launch {
            if (forcedID.isNullOrEmpty()) {
                db().add(data).addOnCompleteListener {
                    if (it.isSuccessful) {
                        presenter.onSuccess("Atualizado com sucesso")
                    } else {
                        presenter.onError("Ocorreu um erro ao processar")
                    }
                }
            } else {
                db().document(forcedID).set(data).addOnCompleteListener {
                    if (it.isSuccessful) {
                        presenter.onSuccess("Atualizado com sucesso")
                    } else {
                        presenter.onError("Ocorreu um erro ao processar")
                    }
                }
            }
        }
    }

    override fun editData(data: T) {
        if (checkUser()) return
        GlobalScope.launch {
            db().document(data.id).set(data).addOnCompleteListener(this@BaseModel)
        }
    }

    fun editField(data: Any, id: String, field: String) {
        if (checkUser()) return
        GlobalScope.launch {
            db().document(id).update(field, data).addOnCompleteListener(this@BaseModel)
        }

    }

    private fun checkUser(): Boolean {
        if (currentUser == null) {
            presenter.onError("Usuário desconectado")
            return true
        }
        return false
    }

    override fun deleteData(id: String) {
        if (checkUser()) return
        GlobalScope.launch {
            db().document(id).delete().addOnCompleteListener(this@BaseModel)
        }
    }

    override fun query(query: String, field: String) {
        if (checkUser()) return
        db().whereEqualTo(field, query).get().addOnSuccessListener { documents ->
            val dataList: ArrayList<T> = ArrayList()
            for (document in documents) {
                val data = deserializeDataSnapshot(document)
                data?.let {
                    dataList.add(it)
                }
                presenter.onDataRetrieve(dataList)
            }
        }
    }

    override fun getAllData() {
        if (checkUser()) return
        db().get().addOnSuccessListener { documents ->
            val dataList: ArrayList<T> = ArrayList()
            for (document in documents) {
                val data = deserializeDataSnapshot(document)
                data?.let {
                    dataList.add(it)
                }
                presenter.onDataRetrieve(dataList)
            }
        }
    }

    override fun getSingleData(id: String) {
        if (checkUser()) return
        db().document(id).get().addOnSuccessListener {
            if (it.exists()) {
                val bean: T? = deserializeDataSnapshot(it)
                if (bean != null) presenter.onSingleData(bean) else presenter.onError("Ocorreu um erro ao receber os dados")
            } else {
                presenter.onError("Dados não encontrados")
            }
        }
    }


    override fun onComplete(task: Task<Void>) {
        if (task.isSuccessful) {
            presenter.onSuccess("Operação concluída")
        } else {
            presenter.onError("Ocorreu um erro ao processar ${task.exception?.message ?: ""}")
        }
    }

    fun deleteAllData(dataList: List<T>) {
        if (checkUser()) return
        GlobalScope.launch {
            try {
                for (data in dataList) {
                    if (data.id.isNotEmpty()) {
                        deleteData(data.id)
                    }
                }
                presenter.onSuccess("Dados removidos com sucesso")
            } catch (e: Exception) {
                presenter.onError("Ocorreu um erro ao processar")
            }
        }
    }

}
