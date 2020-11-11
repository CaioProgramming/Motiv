package com.creat.motiv.model.beans

data class Developer(val nome: String = "",
                     val photoURI: String = "",
                     override val id: String = "") : BaseBean(id)
