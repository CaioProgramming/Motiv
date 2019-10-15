package com.creat.motiv.Model.Beans

import android.graphics.Typeface

class Fonts {

    var font: Typeface? = null
    var fontname: String = ""


    constructor(font: Typeface, fontname: String) {
        this.font = font
        this.fontname = fontname
    }

    constructor()


    constructor(font: Typeface) {
        this.font = font
    }
}
