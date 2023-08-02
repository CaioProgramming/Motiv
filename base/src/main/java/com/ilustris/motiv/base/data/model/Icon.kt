package com.ilustris.motiv.base.data.model

import com.silent.ilustriscore.core.bean.BaseBean

data class Icon(var uri: String = "", override var id: String = "") : BaseBean(id)
