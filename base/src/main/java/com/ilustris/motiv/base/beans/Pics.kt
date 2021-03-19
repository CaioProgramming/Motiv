package com.ilustris.motiv.base.beans

import com.creat.motiv.utilities.NEW_PIC
import com.silent.ilustriscore.core.bean.BaseBean

data class Pics(var uri: String = "", override var id: String = "") : BaseBean(id) {

    companion object {
        fun addPic(): Pics = Pics(id = NEW_PIC)
    }
}
