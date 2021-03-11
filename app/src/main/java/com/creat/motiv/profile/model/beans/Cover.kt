package com.creat.motiv.profile.model.beans

import com.silent.ilustriscore.core.bean.BaseBean

class Cover(val url: String = DEFAULT_USER_BACKGROUND,
            override var id: String = "") : BaseBean(id)