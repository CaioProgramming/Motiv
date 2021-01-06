package com.creat.motiv.model.beans

import com.creat.motiv.utilities.NEW_PIC
import com.ilustriscore.core.base.BaseBean

data class Pics(var uri: String = "", override var id: String = "") : BaseBean(id) {

    companion object {
        fun addPic(): Pics = Pics(id = NEW_PIC)
    }
}
