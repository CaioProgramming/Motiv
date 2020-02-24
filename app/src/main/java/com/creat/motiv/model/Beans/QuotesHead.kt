package com.creat.motiv.model.Beans

import android.util.Log

class QuoteHead(val titulo:String,val quoteslist: ArrayList<Quotes>){
    init {
        Log.i(javaClass.simpleName,"Quote Head: ${titulo} + with ${quoteslist.size}")
    }
}
