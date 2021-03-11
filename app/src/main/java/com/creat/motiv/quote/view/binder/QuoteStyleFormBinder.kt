package com.creat.motiv.quote.view.binder

import androidx.viewpager2.widget.ViewPager2
import com.creat.motiv.databinding.StylePagerBinding
import com.creat.motiv.quote.beans.QuoteStyle
import com.creat.motiv.quote.presenter.QuoteStylePresenter
import com.creat.motiv.quote.view.StylesAdapter
import com.silent.ilustriscore.core.view.BaseView
import kotlin.random.Random

class QuoteStyleFormBinder(override val viewBind: StylePagerBinding, val onPageChange: (QuoteStyle) -> Unit) : BaseView<QuoteStyle>() {

    override val presenter = QuoteStylePresenter(this)
    var quoteStyle: String? = null
    var styles: List<QuoteStyle>? = null

    override fun initView() {
        presenter.loadData()
    }

    override fun showListData(list: List<QuoteStyle>) {
        super.showListData(list)
        styles = list
        viewBind.stylesPager.apply {
            adapter = StylesAdapter(list)
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    onPageChange.invoke(list[position])
                }

            })
        }
        getStyle()

    }

    private fun getStyle() {
        quoteStyle?.let {
            styles?.let {
                it.forEach { style ->
                    if (style.id == quoteStyle) {
                        val styleIndex = it.indexOf(style)
                        viewBind.stylesPager.setCurrentItem(styleIndex, true)
                    }
                }
            }
        }
    }

    fun goToRandomStyle() {
        viewBind.stylesPager.run {
            adapter?.let {
                val position = Random.nextInt(0, it.itemCount)
                setCurrentItem(position, true)
            }

        }
    }

    fun goToStyle(styleID: String) {
        if (styleID != QuoteStyle.defaultStyle.id) {
            quoteStyle = styleID
            if (styles == null) {
                presenter.loadData()
            } else {
                getStyle()
            }
        }
    }

}