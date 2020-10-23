package com.creat.motiv.model.beans

class Artists {
    var nome: String? = null
    var uri: String? = null
    var color: Int = 0

    constructor()

    constructor(nome: String, uri: String, color: Int) {
        this.nome = nome
        this.uri = uri
        this.color = color
    }
}
