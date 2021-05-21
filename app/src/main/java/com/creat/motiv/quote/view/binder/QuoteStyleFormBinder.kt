package com.creat.motiv.quote.view.binder

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.creat.motiv.databinding.StylePagerBinding
import com.creat.motiv.quote.view.adapter.StylePreviewAdapter
import com.ilustris.motiv.base.beans.Style
import com.ilustris.motiv.base.adapters.StylesAdapter
import com.ilustris.motiv.base.presenter.QuoteStylePresenter
import com.silent.ilustriscore.core.view.BaseView
import kotlin.random.Random

class QuoteStyleFormBinder(override val viewBind: StylePagerBinding, val onPageChange: (Style) -> Unit, val stylesRecyclerView: RecyclerView) : BaseView<Style>() {

    override val presenter = QuoteStylePresenter(this)
    var stylePreviewAdapter: StylePreviewAdapter? = null
    var quoteStyle: String? = null
    var styles: List<Style> = listOf()

    override fun initView() {
        presenter.loadData()
    }

    override fun showListData(list: List<Style>) {
        Log.i(javaClass.simpleName, "showListData: showing ${list.size}")
        super.showListData(list)
        if (list.isEmpty()) {
            viewBind.stylesPager.run {
                adapter = StylesAdapter(listOf(Style.emptyStyle))
            }
        } else {
            styles = list
            viewBind.stylesPager.run {
                adapter = StylesAdapter(list)
                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        onPageChange.invoke(list[position])
                        stylePreviewAdapter?.setSelectedStyle(list[position].id)
                        stylesRecyclerView.smoothScrollToPosition(position)
                    }

                })
                stylePreviewAdapter = StylePreviewAdapter(list, list[currentItem].id) {
                    setCurrentItem(it, true)
                }
                stylesRecyclerView.adapter = stylePreviewAdapter
                setCurrentItem(Random.nextInt(list.size - 1), true)
            }
            getStyle()
        }


    }


    private fun getStyle() {
        quoteStyle?.let {
            styles.let {
                it.forEach { style ->
                    if (style.id == quoteStyle) {
                        val styleIndex = it.indexOf(style)
                        viewBind.stylesPager.setCurrentItem(styleIndex, true)
                    }
                }
            }
        }
    }

    fun goToStyle(styleID: String) {
        if (styleID != Style.defaultStyle.id) {
            quoteStyle = styleID
            if (styles == null) {
                presenter.loadData()
            } else {
                getStyle()
            }
        }
    }

}