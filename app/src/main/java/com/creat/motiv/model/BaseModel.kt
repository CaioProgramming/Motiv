package com.creat.motiv.model

import com.creat.motiv.model.Beans.BaseBean
import com.creat.motiv.presenter.BasePresenter
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

abstract class BaseModel<T>(val presenter: BasePresenter<T>) : ValueEventListener, ModelContract<T>, OnCompleteListener<Void> where T : BaseBean {


    var dbRef: DatabaseReference
    abstract fun reference(): DatabaseReference

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
            dbRef.child(id).setValue(data).addOnCompleteListener {
                if (it.isSuccessful) {
                    presenter.onSuccess()
                } else {
                    presenter.onError()
                }
            }
        }
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
                dbRef.child(data.id).removeValue()
            }
        }
    }


    override fun onDataChange(dataSnapshot: DataSnapshot) {
        presenter.onLoading()
        val dataList = ArrayList<T>()
        for (d in dataSnapshot.children) {
            val data: T = dataSnapshot.value as T
            dataList.add(data)
        }
        presenter.onDataRetrieve(dataList.toList())
        presenter.onLoadFinish()
    }


    override fun onCancelled(error: DatabaseError) {
        presenter.onError()
    }


    init {
        dbRef = this.reference()
    }


}

const val SEARCH_END = "\uf8ff"