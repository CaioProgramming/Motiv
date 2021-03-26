package com.ilustris.motiv.base.model

import com.google.firebase.firestore.*
import com.ilustris.motiv.base.beans.Quote
import com.silent.ilustriscore.core.model.BaseModel
import com.silent.ilustriscore.core.model.DTOMessage
import com.silent.ilustriscore.core.model.DataException
import com.silent.ilustriscore.core.presenter.BasePresenter
import com.silent.ilustriscore.core.utilities.MessageType
import com.silent.ilustriscore.core.utilities.OperationType

class QuoteModel(val presenter: BasePresenter<Quote>, override val path: String = "Quotes") : BaseModel<Quote>(presenter) {

    fun getFavorites(uid: String, onListLoad: (List<Quote>) -> Unit) {
        this.reference.whereArrayContains("likes", uid).addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    presenter.errorCallBack(DataException("Erro ao receber dados ${error.message}"))
                    return
                }
                val dataList: ArrayList<Quote> = ArrayList()
                for (doc in value!!) {
                    deserializeDataSnapshot(doc).let { dataList.add(it) }
                }
                presenter.modelCallBack(DTOMessage("Dados recebidos: $dataList", MessageType.SUCCESS, OperationType.DATA_RETRIEVED))
                onListLoad.invoke(dataList)
            }

        })
    }

    override fun deserializeDataSnapshot(dataSnapshot: DocumentSnapshot): Quote {
        return dataSnapshot.toObject(Quote::class.java)!!.apply {
            id = dataSnapshot.id
        }
    }

    override fun deserializeDataSnapshot(dataSnapshot: QueryDocumentSnapshot): Quote {
        return dataSnapshot.toObject(Quote::class.java).apply {
            id = dataSnapshot.id
        }
    }


}