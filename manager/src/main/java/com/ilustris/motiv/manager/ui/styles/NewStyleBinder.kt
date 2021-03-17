package com.ilustris.motiv.manager.ui.styles

import android.view.LayoutInflater
import android.widget.TextView
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
import com.google.android.material.shape.CornerFamily
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.ilustris.animations.fadeIn
import com.ilustris.motiv.base.dialog.DefaultAlert
import com.ilustris.motiv.base.presenter.QuoteStylePresenter
import com.ilustris.motiv.base.utils.FontUtils
import com.ilustris.motiv.base.utils.activity
import com.ilustris.motiv.base.utils.loadGif
import com.ilustris.motiv.manager.R
import com.ilustris.motiv.manager.databinding.FragmentStylesBinding
import com.silent.ilustriscore.core.model.DTOMessage
import com.silent.ilustriscore.core.model.DataException
import com.silent.ilustriscore.core.model.ErrorType
import com.silent.ilustriscore.core.utilities.OperationType
import com.silent.ilustriscore.core.utilities.showSnackBar
import com.silent.ilustriscore.core.view.BaseView

class NewStyleBinder(override val viewBind: FragmentStylesBinding, val fragmentManager: FragmentManager) : BaseView<QuoteStyle>() {
    override val presenter = QuoteStylePresenter(this)

    var style = QuoteStyle()
    val fontAdapter = StyleFontAdapter()
    var previewAdapter: StylePreviewAdapter? = null

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
            styleBackground.run {
                viewBind.styleBackground.loadGif(style.backgroundURL)
                setOnClickListener {
                    openGiphyDialog()
                }
            }
            styleWallpaper.setOnClickListener {
                openGiphyDialog()
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
                    val view = LayoutInflater.from(context).inflate(R.layout.tab_font_view, null, false)
                    tab.customView = view
                    tab.view.findViewById<TextView>(R.id.fontText).typeface = FontUtils.getTypeFace(context, position)
                }.attach()
            }
            fontsTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.customView?.findViewById<TextView>(R.id.fontText)?.run {
                        setTextColor(ContextCompat.getColor(context, R.color.material_white))
                        setOnClickListener {
                            it.isSelected = true
                        }
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    tab?.customView?.findViewById<TextView>(R.id.fontText)?.run {
                        setTextColor(ContextCompat.getColor(context, R.color.lwhite))
                    }
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }

            })

            getColorGallery()
        }
        presenter.loadData()
        updateStyle()
    }

    private fun openGiphyDialog() {
        GiphyDialogFragment.newInstance(GPHSettings(GridType.waterfall, GPHTheme.Automatic)).apply {
            gifSelectionListener = object : GiphyDialogFragment.GifSelectionListener {
                override fun didSearchTerm(term: String) {

                }

                override fun onDismissed(selectedContentType: GPHContentType) {

                }

                override fun onGifSelected(media: Media, searchTerm: String?, selectedContentType: GPHContentType) {
                    media.images.downsizedMedium?.gifUrl?.let {
                        style.backgroundURL = it
                        Glide.with(this@NewStyleBinder.context).load(style.backgroundURL).into(viewBind.styleBackground)
                        updateStyle()
                    }
                }

            }
        }.show(fragmentManager, "GIPHYDIALOG")

    }

    override fun getCallBack(dtoMessage: DTOMessage) {
        super.getCallBack(dtoMessage)
        if (dtoMessage.operationType == OperationType.DATA_SAVED || dtoMessage.operationType == OperationType.DATA_UPDATED) {
            DefaultAlert(context, "Ótimo!", "O estilo foi salvo com sucesso!", okClick = {
                context.activity()?.finish()
            }, cancelClick = {
                context.activity()?.finish()
            }).buildDialog()

            style = QuoteStyle.emptyStyle
            updateStyle()
        }
    }

    override fun error(dataException: DataException) {
        super.error(dataException)
        when (dataException.code) {
            ErrorType.UPDATE -> showSnackBar(context, "Ocorreu um erro ao atualizar o estilo", rootView = viewBind.styleContainer)
            ErrorType.SAVE -> showSnackBar(context, "Ocorreu um erro ao atualizar o estilo", rootView = viewBind.styleContainer)
            else -> showSnackBar(context, "Ocorreu um erro desconhecido", rootView = viewBind.styleContainer)
        }

    }

    override fun showListData(list: List<QuoteStyle>) {
        super.showListData(list)
        viewBind.stylesRecycler.stylesRecycler.run {
            previewAdapter = StylePreviewAdapter(list, true, style.id, ::getStyleFromPreview)
            adapter = previewAdapter
        }
    }

    private fun getStyleFromPreview(selectedStyle: QuoteStyle) {
        style = selectedStyle
        updateStyle()
        viewBind.fontsTabs.getTabAt(selectedStyle.font)?.select()
        viewBind.styleBackground.loadGif(style.backgroundURL)
    }

    private fun getColorGallery() {
        viewBind.colorGallery.adapter = RecyclerColorAdapter(context) {
            style.textColor = it
            updateStyle()
        }
    }

    fun updateStyle() {
        viewBind.run {
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
            previewAdapter?.setSelectedStyle(style.id)

        }
    }
}