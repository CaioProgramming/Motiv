package com.creat.motiv.quote.view.binder

import android.graphics.Color
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.creat.motiv.databinding.StylePagerBinding
import com.google.android.material.card.MaterialCardView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.ilustris.motiv.base.beans.QuoteStyle
import com.ilustris.motiv.base.adapters.StylesAdapter
import com.ilustris.motiv.base.presenter.QuoteStylePresenter
import com.ilustris.motiv.base.utils.FontUtils
import com.ilustris.motiv.base.utils.loadGif
import com.ilustris.motiv.manager.R
import com.ilustris.motiv.manager.databinding.StyleCardBinding
import com.ilustris.motiv.manager.databinding.StylePreviewCardBinding
import com.silent.ilustriscore.core.view.BaseView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class QuoteStyleFormBinder(override val viewBind: StylePagerBinding, val onPageChange: (QuoteStyle) -> Unit, val styleTabLayout: TabLayout) : BaseView<QuoteStyle>() {

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
            TabLayoutMediator(styleTabLayout, this) { tab, position ->
                val view = LayoutInflater.from(context).inflate(R.layout.style_preview_card, null, false)
                val stylePreviewCardBinding = StylePreviewCardBinding.bind(view)
                stylePreviewCardBinding.run {
                    val style = list[position]
                    styleText.typeface = FontUtils.getTypeFace(context, style.font)
                    styleText.setTextColor(Color.parseColor(style.textColor))
                    styleImage.loadGif(style.backgroundURL)
                    styleCard.setOnClickListener {
                        tab.select()
                    }
                }
                tab.customView = stylePreviewCardBinding.root
            }.attach()
            styleTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.view?.findViewById<MaterialCardView>(R.id.styleCard)?.isSelected = true
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    tab?.view?.findViewById<MaterialCardView>(R.id.styleCard)?.isSelected = false
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {

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