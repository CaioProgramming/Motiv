package com.creat.motiv.model.Beans

import android.graphics.Color
import com.google.firebase.database.DataSnapshot

class Quote(key: String? = null,
            var phrase: String = "",
            var author: String = "",
            val data: String = "",
            val userID: String = "",
            val username: String = "",
            val userphoto: String = "",
            var backgroundcolor: Int = Color.WHITE,
            var textcolor: Int = Color.BLACK,
            var isReport: Boolean = false,
            var font: Int = 0) : BaseBean(key ?: "") {

    override fun convertSnapshot(snapshot: DataSnapshot): Quote? {
        return snapshot.getValue(Quote::class.java)
    }

}
