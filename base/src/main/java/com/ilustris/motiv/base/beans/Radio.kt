package com.ilustris.motiv.base.beans

import com.silent.ilustriscore.core.bean.BaseBean

class Radio(override var id: String = "",
            val url: String = "",
            val name: String = "") : BaseBean(id)