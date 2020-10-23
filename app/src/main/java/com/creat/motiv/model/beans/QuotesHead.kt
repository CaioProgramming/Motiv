package com.creat.motiv.model.beans

import android.util.Log

class QuoteHead(val titulo: String, val quoteslist: ArrayList<Quote>) {
    init {
        Log.i(javaClass.simpleName, "Quote Head: ${titulo} + with ${quoteslist.size}")
    }
}
