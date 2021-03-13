package com.ilustris.motiv.manager.ui.styles

import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.ilustris.motiv.base.beans.QuoteStyle
import com.ilustris.motiv.base.beans.TextAlignment
import com.ilustris.motiv.base.beans.TextSize
import com.giphy.sdk.core.models.Media
import com.giphy.sdk.ui.GPHContentType
import com.giphy.sdk.ui.GPHSettings
import com.giphy.sdk.ui.themes.GPHTheme
import com.giphy.sdk.ui.themes.GridType
import com.giphy.sdk.ui.views.GiphyDialogFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.ilustris.animations.fadeIn
import com.ilustris.motiv.base.presenter.QuoteStylePresenter
import com.ilustris.motiv.base.utils.FontUtils
import com.ilustris.motiv.manager.R
import com.ilustris.motiv.manager.databinding.FragmentStylesBinding
import com.silent.ilustriscore.core.model.DTOMessage
import com.silent.ilustriscore.core.utilities.OperationType
import com.silent.ilustriscore.core.utilities.showSnackBar
import com.silent.ilustriscore.core.view.BaseView

class StyleBinder(override val viewBind: FragmentStylesBinding, val fragmentManager: FragmentManager) : BaseView<QuoteStyle>() {
    override val presenter = QuoteStylePresenter(this)

    var style = QuoteStyle()
    val fontAdapter = StyleFontAdapter()

    override fun initView() {

        viewBind.run {
            styleSize.setOnClickListener {
                when (style.textSize) {
                    TextSize.DEFAULT -> style.textSize = TextSize.BIG
                    TextSize.BIG -> style.textSize = TextSize.EXTRASMALL
                    TextSize.SMALL -> style.textSize = TextSize.DEFAULT
                    TextSize.EXTRASMALL -> style.textSize = TextSize.SMALL
                }
                updateStyle()
            }
            styleAlign.setOnClickListener {
                when (style.textAlignment) {
                    TextAlignment.CENTER -> style.textAlignment = TextAlignment.END
                    TextAlignment.START -> style.textAlignment = TextAlignment.CENTER
                    TextAlignment.END -> style.textAlignment = TextAlignment.START
                }
                updateStyle()
            }
            styleWallpaper.setOnClickListener {
                GiphyDialogFragment.newInstance(GPHSettings(GridType.waterfall, GPHTheme.Automatic)).apply {
                    gifSelectionListener = object : GiphyDialogFragment.GifSelectionListener {
                        override fun didSearchTerm(term: String) {

                        }

                        override fun onDismissed(selectedContentType: GPHContentType) {

                        }

                        override fun onGifSelected(media: Media, searchTerm: String?, selectedContentType: GPHContentType) {
                            media.images.downsizedMedium?.gifUrl?.let {
                                style.backgroundURL = it
                                updateStyle()
                            }
                        }

                    }
                }.show(fragmentManager, "GIPHYDIALOG")

            }
            saveButton.setOnClickListener {
                if (style.id.isNotEmpty()) {
                    presenter.updateData(style)
                } else {
                    presenter.saveData(style)
                }
            }
            styleFontPager.run {
                adapter = fontAdapter
                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        style.font = position
                    }
                })
                TabLayoutMediator(viewBind.fontsTabs, this) { tab, position ->
                    tab.icon = ContextCompat.getDrawable(context, R.drawable.dot)
                    //this.setCurrentItem(position,true)
                }.attach()
            }
            getColorGallery()
        }
        presenter.loadData()
        updateStyle()
    }

    override fun getCallBack(dtoMessage: DTOMessage) {
        super.getCallBack(dtoMessage)
        if (dtoMessage.operationType == OperationType.DATA_SAVED) {
            showSnackBar(context, "Estilo Salvo com sucesso", ContextCompat.getColor(context, R.color.material_purple900), rootView = viewBind.root)
        }
    }

    override fun showListData(list: List<QuoteStyle>) {
        super.showListData(list)
        viewBind.stylesRecycler.stylesRecycler.run {
            adapter = StylePreviewAdapter(list, true) {
                style = it
                updateStyle()
                viewBind.fontsTabs.getTabAt(it.font)?.select()
            }
        }
    }

    private fun getColorGallery() {
        viewBind.colorGallery.adapter = RecyclerColorAdapter(context) {
            style.textColor = it
            updateStyle()
        }
    }

    fun updateStyle() {
        viewBind.run {
            Glide.with(context).asGif().centerCrop().load(style.backgroundURL).addListener(object : RequestListener<GifDrawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<GifDrawable>?, isFirstResource: Boolean): Boolean {
                    return false
                }

                override fun onResourceReady(resource: GifDrawable?, model: Any?, target: Target<GifDrawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    styleBackground.setImageDrawable(resource)
                    styleBackground.fadeIn()
                    return false
                }

            }).into(styleBackground)
            styleSize.text = when (style.textSize) {
                TextSize.DEFAULT -> "Aa"
                TextSize.BIG -> "AA"
                TextSize.SMALL -> "aa"
                TextSize.EXTRASMALL -> "ᵃᵃ"
            }
            styleAlign.run {
                val src = when (style.textAlignment) {
                    TextAlignment.CENTER -> R.drawable.ic_baseline_format_align_center_24
                    TextAlignment.START -> R.drawable.ic_baseline_format_align_left_24
                    TextAlignment.END -> R.drawable.ic_baseline_format_align_left_24
                }
                setImageResource(src)
            }
            fontAdapter.run {
                updateAlignment(style.textAlignment)
                updateTextColor(style.textColor)
                updateTextSize(style.textSize)
            }

        }
    }
}