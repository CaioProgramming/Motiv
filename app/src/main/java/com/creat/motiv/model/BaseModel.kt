package com.creat.motiv.model

import com.creat.motiv.model.beans.BaseBean
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

abstract class BaseModel<T> : ValueEventListener, ModelContract<T>, OnCompleteListener<Void> where T : BaseBean {



    fun reference(): DatabaseReference {
        return FirebaseDatabase.getInstance().reference.child(path)
    }

    val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser


    override fun addData(data: T, forcedID: String?) {
        GlobalScope.launch {
            if (forcedID.isNullOrEmpty()) {
                reference().push().setValue(data).addOnCompleteListener {
                    if (it.isSuccessful) {
                        presenter.onSuccess("Operação concluída com sucesso")
                    } else {
                        presenter.onError("Ocorreu um erro ao processar ${it.exception?.cause}")
                    }
                }
            } else {
                reference().child(forcedID).setValue(data).addOnCompleteListener {
                    if (it.isSuccessful) {
                        presenter.onSuccess("Atualizado com sucesso")
                    } else {
                        presenter.onError("Ocorreu um erro ao processar")
                    }
                }
            }
        }
    }

    override fun editData(data: T, id: String) {
        if (checkUser()) return
        GlobalScope.launch {
            reference().child(id).setValue(data).addOnCompleteListener(this@BaseModel)
        }
    }

    fun editField(data: Any, id: String, field: String) {
        if (checkUser()) return
        GlobalScope.launch {
            reference().child(id).child(field).setValue(data)
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
            reference().child(id).removeValue().addOnCompleteListener(this@BaseModel)
        }
    }

    override fun query(query: String, field: String) {
        if (checkUser()) return
        reference().startAt(query + SEARCH_END, field)
    }

    override fun getAllData() {
        if (checkUser()) return
        GlobalScope.launch {
            reference().addValueEventListener(this@BaseModel)
        }

    }

    override fun getSingleData(id: String) {
        if (checkUser()) return
        GlobalScope.launch {
            reference().child(id).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    deserializeDataSnapshot(snapshot)?.let {
                        presenter.onSingleData(it)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    presenter.onError("Ocorreu um erro ${error.message}")
                }
            })
        }
    }


    override fun onComplete(task: Task<Void>) {
        if (task.isSuccessful) {
            presenter.onSuccess("Operação concluída")
        } else {
            presenter.onError("Ocorreu um erro ao processar")
        }
    }

    fun deleteAllData(dataList: List<T>) {
        if (checkUser()) return
        GlobalScope.launch {
            try {
                for (data in dataList) {
                    if (data.id.isNotEmpty()) {
                        reference().child(data.id).removeValue()
                    }
                }
                presenter.onSuccess("Dados removidos com sucesso")
            } catch (e: Exception) {
                presenter.onError("Ocorreu um erro ao processar")
            }
        }
    }


    override fun onDataChange(dataSnapshot: DataSnapshot) {
        val dataList = ArrayList<T>()
        for (d in dataSnapshot.children) {
            deserializeDataSnapshot(d)?.let { dataList.add(it) }
        }
        presenter.onDataRetrieve(dataList.toList())
    }

    override fun onCancelled(error: DatabaseError) {
        presenter.onError("Ocorreu um erro ${error.message}")
    }




}

const val SEARCH_END = "\uf8ff"