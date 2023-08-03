package com.creat.motiv.features.settings.data

data class UserMetaData(
    val email: String? = "",
    val createTimeStamp: Long? = 0L,
    val provider: String? = "",
    val emailVerified: Boolean = false,
    val admin: Boolean = false
)
