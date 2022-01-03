package com.ilustris.motiv.base.beans

import com.silent.ilustriscore.core.bean.BaseBean
import java.util.*
import kotlin.collections.ArrayList


data class QuoteData(
        var quote: Quote,
        var style: Style = Style.defaultStyle,
        var user: User = User()
)

