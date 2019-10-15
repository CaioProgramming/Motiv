package com.creat.motiv.Model.Beans

class Quotes {
    constructor()
    constructor(id: String, quote: String, author: String, data: String, userID: String, username: String, userphoto: String, backgroundcolor: Int, textcolor: Int, isReport: Boolean, font: Int) {
        this.id = id
        this.quote = quote
        this.author = author
        this.data = data
        this.userID = userID
        this.username = username
        this.userphoto = userphoto
        this.backgroundcolor = backgroundcolor
        this.textcolor = textcolor
        this.isReport = isReport
        this.font = font
    }

    var id: String? = ""
    var quote: String? = ""
    var author: String? = ""
    var data: String? = ""
    var userID: String? = ""
    var username: String? = ""
    var userphoto: String? = ""
    var backgroundcolor: Int? = 0
    var textcolor: Int? = 0
    var isReport: Boolean? = false
    var font: Int? = 0


}
