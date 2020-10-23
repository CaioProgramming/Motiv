package com.creat.motiv.view.custom

import android.content.Context
import android.os.Handler
import android.util.AttributeSet


class Typewritter : androidx.appcompat.widget.AppCompatTextView {
    private var text: String? = null
    private var index: Int = 0
    private var hndlr: Handler = Handler()
    var delay: Long = 100

    private val addcharacter = object : Runnable {
        override fun run() {
            setText(text!!.subSequence(0, index++))
            if (index < text!!.length + 1) {
                hndlr.postDelayed(this, delay)
            }

        }
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun animateText(text: String) {
        this.text = text
        index = 0
        setText("")
        hndlr.removeCallbacks(addcharacter)
        hndlr.postDelayed(addcharacter, delay)
    }
}
