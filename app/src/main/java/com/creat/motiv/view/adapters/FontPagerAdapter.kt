package com.creat.motiv.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.creat.motiv.R
import com.creat.motiv.databinding.FontPagerBinding
import com.creat.motiv.utilities.TextUtils

class FontPagerAdapter(val context: Context) : PagerAdapter() {

    val fontList = TextUtils.fonts()

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeViewInLayout(`object` as TextView)
    }


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fontPagerBinding: FontPagerBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.font_pager, container, false)

        fontPagerBinding.fontTextView.run {
            val font = fontList[position]
            text = font.name
            typeface = TextUtils.getTypeFace(context, font.path)
        }
        container.addView(fontPagerBinding.root)
        return fontPagerBinding.root
    }


    override fun getCount(): Int {
        return fontList.size
    }
}