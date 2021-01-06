package com.creat.motiv.model.beans

import com.ilustriscore.core.base.BaseBean


data class Developer(val nome: String = "",
                     val photoURI: String = "",
                     override val id: String = "") : BaseBean(id)
