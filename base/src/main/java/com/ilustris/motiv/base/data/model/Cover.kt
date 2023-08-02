package com.ilustris.motiv.base.data.model

import com.silent.ilustriscore.core.bean.BaseBean

data class Cover(
    val url: String = DEFAULT_USER_BACKGROUND,
    override var id: String = ""
) : BaseBean(id)

const val DEFAULT_USER_BACKGROUND = "https://media.giphy.com/media/l0HlQpsp5lhK6p3CE/giphy.gif"