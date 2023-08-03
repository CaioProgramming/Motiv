package com.ilustris.motiv.base.data.model

import com.silent.ilustriscore.core.bean.BaseBean

class Radio(
    override var id: String = "",
    var visualizer: String = "https://media.giphy.com/media/l2QE06aQn33bCDNC0/giphy.gif",
    var url: String = "",
    var name: String = "",
) : BaseBean(id)