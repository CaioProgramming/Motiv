package com.creat.motiv.model.beans

import com.ilustriscore.core.base.BaseBean


data class Likes(var userid: String = "", var username: String = "name not found", var userpic: String = "") : BaseBean(userid)
