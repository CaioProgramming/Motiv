package com.creat.motiv.model.beans

import com.silent.ilustriscore.core.bean.BaseBean

data class Developer(val nome: String = "",
                     val photoURI: String = "",
                     override var id: String = "") : BaseBean(id)
