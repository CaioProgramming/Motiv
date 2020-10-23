package com.creat.motiv.model.beans

import com.google.firebase.database.DataSnapshot
import java.io.Serializable

abstract class BaseBean(val id: String = "") : Serializable {

    abstract fun convertSnapshot(snapshot: DataSnapshot): BaseBean?


}