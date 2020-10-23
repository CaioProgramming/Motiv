package com.creat.motiv.model.beans

import android.graphics.Color
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue

class Quote(key: String? = null,
            var quote: String = "",
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
        return snapshot.getValue<Quote>()
    }

}
