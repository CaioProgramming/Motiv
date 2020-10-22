package com.creat.motiv.model.Beans

import com.google.firebase.database.DataSnapshot
import java.io.Serializable

abstract class BaseBean(val id: String = "") : Serializable {

    abstract fun convertSnapshot(snapshot: DataSnapshot): BaseBean?


}