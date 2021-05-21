package com.ilustris.motiv.manager.ui.styles

import android.graphics.Color
import android.graphics.Typeface
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ilustris.motiv.base.beans.Style
import com.ilustris.motiv.base.beans.TextAlignment
import com.giphy.sdk.core.models.Media
import com.giphy.sdk.ui.GPHContentType
import com.giphy.sdk.ui.GPHSettings
import com.giphy.sdk.ui.themes.GPHTheme
import com.giphy.sdk.ui.themes.GridType
import com.giphy.sdk.ui.views.GiphyDialogFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.hmomeni.verticalslider.VerticalSlider
import com.ilustris.motiv.base.beans.FontStyle
import com.ilustris.motiv.base.presenter.QuoteStylePresenter
import com.ilustris.motiv.base.utils.*
import com.ilustris.motiv.manager.R
import com.ilustris.motiv.manager.databinding.NewStyleFormBinding
import com.silent.ilustriscore.core.model.DTOMessage
import com.silent.ilustriscore.core.model.DataException
import com.silent.ilustriscore.core.model.ErrorType
import com.silent.ilustriscore.core.utilities.OperationType
import com.silent.ilustriscore.core.utilities.showSnackBar
import com.silent.ilustriscore.core.view.BaseView

class NewStyleBinder(override val viewBind: NewStyleFormBinding, val fragmentManager: FragmentManager) : BaseView<Style>() {
    override val presenter = QuoteStylePresenter(this)

    var style = Style.defaultStyle
    var previewAdapter: StylePreviewAdapter? = null
    var colorAdapter: RecyclerColorAdapter? = null
    var fontAdapter: StyleFontsAdapter? = null
    override fun initView() {

        viewBind.run {
            fontsRecyclerView.run {
                fontAdapter = StyleFontsAdapter {
                    style.font = it
                    updateStyle()
                }
                adapter = fontAdapter
            }
            initButtons()
            getColorGallery()
        }
        presenter.loadData()
        updateStyle()
    }

    fun saveStyle() {
        if (style.id == Style.defaultStyle.id) style.id = ""

        if (style.id.isEmpty()) {
            presenter.saveData(style)
        } else {
            presenter.updateData(style)

        }
    }


    private fun NewStyleFormBinding.initButtons() {
        dotslayout.setOnClickListener {
            val bottomSheetBehavior = BottomSheetBehavior.from(stylesSettings)
            bottomSheetBehavior.state =
                if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) BottomSheetBehavior.STATE_EXPANDED else BottomSheetBehavior.STATE_COLLAPSED
        }
        styleAlign.setOnClickListener {
            style.textAlignment = when (style.textAlignment) {
                TextAlignment.CENTER -> TextAlignment.END
                TextAlignment.START -> TextAlignment.CENTER
                TextAlignment.END -> TextAlignment.START
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
        fontStyle.setOnClickListener {
            style.fontStyle = when (style.fontStyle) {
                FontStyle.REGULAR -> FontStyle.BOLD
                FontStyle.BOLD -> FontStyle.ITALIC
                FontStyle.ITALIC -> FontStyle.REGULAR
            }
            updateStyle()
        }
        extrudeDxPosition.addOnChangeListener { slider, value, fromUser ->
            style.shadowStyle.dx = value
            style.shadowStyle.dy = value
            updateStyle()
        }

        extrudeShadowSize.addOnChangeListener { slider, value, fromUser ->
            style.shadowStyle.radius = value
            updateStyle()
        }


    }


    private fun openGiphyDialog() {
        GiphyDialogFragment.newInstance(GPHSettings(GridType.waterfall, GPHTheme.Automatic)).apply {
            gifSelectionListener = object : GiphyDialogFragment.GifSelectionListener {
                override fun didSearchTerm(term: String) {

                }

                override fun onDismissed(selectedContentType: GPHContentType) {

                }

                override fun onGifSelected(media: Media, searchTerm: String?, selectedContentType: GPHContentType) {
                    media.images.downsized?.gifUrl?.let {
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
            showSnackBar(context, "Estilo salvo com sucesso", Color.GREEN, rootView = viewBind.root)

            style = Style.defaultStyle
            updateStyle()
            viewBind.styleBackground.loadGif(style.backgroundURL)
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

    override fun showListData(list: List<Style>) {
        super.showListData(list)
        if (previewAdapter == null) {
            viewBind.stylePreviewLayout.stylesRecycler.run {
                previewAdapter = StylePreviewAdapter(list, true, onRequestDelete = this@NewStyleBinder::getStyleFromPreview)
                adapter = previewAdapter
                setHasFixedSize(true)
                layoutManager = GridLayoutManager(context, 1, RecyclerView.HORIZONTAL, false)
            }
        } else {
            previewAdapter?.updateStyles(list)
            viewBind.stylePreviewLayout.stylesRecycler.scheduleLayoutAnimation()
        }
    }

    private fun getStyleFromPreview(position: Int) {
        previewAdapter?.let {
            val selectedStyle = it.styles[position]
            style = if (style.id == selectedStyle.id) {
                Style.defaultStyle
            } else {
                selectedStyle
            }
            previewAdapter?.setSelectedStyle(style.id)
            viewBind.styleBackground.loadGif(style.backgroundURL)
            viewBind.stylePreviewLayout.stylesRecycler.scheduleLayoutAnimation()
            viewBind.stylePreviewLayout.stylesRecycler.smoothScrollToPosition(position)
            viewBind.extrudeShadowSize.value =
                if (style.shadowStyle.radius > viewBind.extrudeShadowSize.valueTo) 1f else style.shadowStyle.radius
            viewBind.extrudeDxPosition.value =
                if (style.shadowStyle.dx > viewBind.extrudeDxPosition.valueTo) 1f else style.shadowStyle.dx
            //viewBind.extrudeDyPosition.value = if (style.shadowStyle.radius > viewBind.extrudeDyPosition.valueTo) 1f else  style.shadowStyle.dy.toInt().toFloat()
            updateStyle()

        }
    }

    private fun getColorGallery() {
        viewBind.run {
            stylecolorGallery.run {
                adapter = RecyclerColorAdapter(
                    context
                ) {
                    style.textColor = it
                    updateStyle()
                }
            }
            styleExtrudeColorGallery.run {
                adapter = RecyclerColorAdapter(
                    context
                ) {
                    style.shadowStyle.shadowColor = it
                    updateStyle()

                }
            }
            styleStrokeColorGallery.run {
                adapter = RecyclerColorAdapter(
                    context
                ) {
                    style.shadowStyle.strokeColor = it
                    updateStyle()
                }
            }
        }
    }

    fun updateStyle() {
        viewBind.run {
            styleAlign.run {
                val src = when (style.textAlignment) {
                    TextAlignment.CENTER -> R.drawable.ic_baseline_format_align_center_24
                    TextAlignment.START -> R.drawable.ic_baseline_format_align_left_24
                    TextAlignment.END -> R.drawable.ic_baseline_format_align_right_24
                }
                setImageResource(src)
            }
            fontStyle.run {
                val src = when (style.fontStyle) {
                    FontStyle.REGULAR -> R.drawable.ic_baseline_format_clear_24
                    FontStyle.BOLD -> R.drawable.ic_baseline_format_bold_24
                    FontStyle.ITALIC -> R.drawable.ic_baseline_format_italic_24
                }
                setImageResource(src)
            }
            styleText.run {
                defineTextAlignment(style.textAlignment)
                setTextColor(Color.parseColor(style.textColor))
                FontUtils.getTypeFace(context, style.font)?.let {
                    setTypeface(it, style.fontStyle.getTypefaceStyle())
                }
                style.shadowStyle.run {
                    setShadowLayer(radius, dx, dy, Color.parseColor(shadowColor))
                }
            }
            styleAuthor.run {
                defineTextAlignment(style.textAlignment)
                setTextColor(Color.parseColor(style.textColor))
                FontUtils.getTypeFace(context, style.font)?.let {
                    setTypeface(it, style.fontStyle.getTypefaceStyle())
                }
                style.shadowStyle.run {
                    setShadowLayer(radius, dx, dy, Color.parseColor(shadowColor))
                }
            }
            colorAdapter?.run {
                updateTextColor(style.textColor)
            }
            fontAdapter?.updateFont(style.font, style.fontStyle)

        }
    }

}