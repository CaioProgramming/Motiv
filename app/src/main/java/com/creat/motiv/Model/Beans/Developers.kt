package com.creat.motiv.Model.Beans

class Developers {
    var nome: String = ""
    var cargo: String = ""
    var backgif: String = ""
    var photouri: String = ""
    var linkedin: String = ""

    constructor(nome: String, cargo: String, backgif: String, photouri: String, linkedin: String) {
        this.nome = nome
        this.cargo = cargo
        this.backgif = backgif
        this.photouri = photouri
        this.linkedin = linkedin
    }

    constructor()
}
