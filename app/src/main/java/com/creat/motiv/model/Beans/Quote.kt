package com.creat.motiv.model.Beans

import android.graphics.Color

class Quote(val key: String,
            var phrase: String,
            var author: String,
            val data: String,
            val userID: String,
            val username: String,
            val userphoto: String,
            var backgroundcolor: Int = Color.WHITE,
            var textcolor: Int = Color.BLACK,
            var isReport: Boolean = false,
            var font: Int = 0) : BaseBean(key)
