package com.creat.motiv.model.Beans

import com.google.firebase.database.DataSnapshot

class Pics(val uri: String) : BaseBean() {

    override fun convertSnapshot(snapshot: DataSnapshot): BaseBean? {
        return snapshot.getValue(Pics::class.java)
    }
}
