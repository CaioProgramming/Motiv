package com.creat.motiv.model

import com.creat.motiv.model.Beans.BaseBean
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

abstract class BaseModel<T> : ValueEventListener, ModelContract<T>, OnCompleteListener<Void> where T : BaseBean {


    var dbRef: DatabaseReference

    fun reference(): DatabaseReference {
        return FirebaseDatabase.getInstance().reference.child(path)
    }

    val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser


    override fun addData(data: T, forcedID: String?) {
        if (currentUser == null) {
            presenter.onError()
        } else {
            if (forcedID.isNullOrEmpty()) {
                dbRef.push().setValue(data).addOnCompleteListener {
                    if (it.isSuccessful) {
                        presenter.onSuccess()
                    } else {
                        presenter.onError()
                    }
                }
            } else {
                dbRef.child(forcedID).setValue(data).addOnCompleteListener {
                    if (it.isSuccessful) {
                        presenter.onSuccess()
                    } else {
                        presenter.onError()
                    }
                }
            }
        }

    }

    override fun editData(data: T, id: String) {
        if (currentUser == null) {
            presenter.onError()
        } else {
            dbRef.child(id).setValue(data).addOnCompleteListener(this)
        }
    }

    fun editField(data: Any, id: String, field: String) {
        dbRef.child(id).child(field).setValue(data)
    }

    override fun deleteData(id: String) {
        reference().child(id).removeValue().addOnCompleteListener(this)
    }

    override fun query(query: String, field: String) {
        dbRef.startAt(query + SEARCH_END, field)
    }

    override fun getAllData() {
        GlobalScope.launch {
            dbRef.addValueEventListener(this@BaseModel)
        }

    }

    override fun getSingleData(id: String) {
        dbRef.child(id).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                deserializeDataSnapshot(snapshot)?.let { presenter.onSingleData(it) }
            }

            override fun onCancelled(error: DatabaseError) {
                presenter.onError()
            }
        })
    }


    override fun onComplete(task: Task<Void>) {
        if (task.isSuccessful) {
            presenter.onSuccess()
        } else {
            presenter.onError()
        }
    }

    fun deleteAllData(dataList: List<T>) {
        GlobalScope.launch {
            for (data in dataList) {
                if (!data.id.isNullOrEmpty()) {
                    dbRef.child(data.id).removeValue()
                }
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
        presenter.onError()
    }


    init {
        dbRef = this.reference()
    }


}

const val SEARCH_END = "\uf8ff"