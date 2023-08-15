package com.ilustris.motiv.base.data.model

import com.silent.ilustriscore.core.bean.BaseBean

data class Developer(
    val nome: String = "",
    val photoURI: String = "",
    val socialLink: String = "",
    override var id: String = ""
) : BaseBean(id)