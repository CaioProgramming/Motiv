package com.creat.motiv.view.custom

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.creat.motiv.R
import com.google.android.material.appbar.AppBarLayout

class AppTitle(context: Context, attrs: AttributeSet) : AppBarLayout(context, attrs) {

    init {
        inflate(context, R.layout.title_layout, this)
        val titleView: TextView = findViewById(R.id.title)
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.AppTitle)
        titleView.text = attributes.getString(R.styleable.AppTitle_title)
    }

}