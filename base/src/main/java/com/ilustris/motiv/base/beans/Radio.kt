package com.ilustris.motiv.base.beans

import com.silent.ilustriscore.core.bean.BaseBean

class Radio(
    override var id: String = "",
    val visualizer: String = "https://media.giphy.com/media/l2QE06aQn33bCDNC0/giphy.gif",
    val url: String = "",
    val name: String = ""
) : BaseBean(id)