package com.ilustris.motiv.manager.ui.styles

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
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
    override fun initView() {

        viewBind.run {
            saveButton.setOnClickListener {
                if (style.isStoredStyle()) {
                    presenter.updateData(style)
                } else {
                    presenter.saveData(style.apply { id = "" })
                }
            }
            viewBind.fontsTabs.run {
                repeat(FontUtils.fonts.size) {
                    addTab(newTab().apply {
                        val view = LayoutInflater.from(context).inflate(R.layout.tab_font_view, null, false)
                        customView = view
                        view.findViewById<TextView>(R.id.fontText).run {
                            typeface = FontUtils.getTypeFace(context, it)
                            setOnClickListener {
                                select()
                                style.font = position
                                updateStyle()
                            }
                        }
                    })
                }
            }
            initButtons()
            getColorGallery()
        }
        presenter.loadData()
        updateStyle()
    }

    private fun NewStyleFormBinding.initButtons() {
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
        styleShadowPropretiesSlider.addOnChangeListener { slider, value, fromUser ->
            handleSlider(optionsGroup.checkedRadioButtonId, value)
        }
        optionsGroup.setOnCheckedChangeListener { group, checkedId ->
            style.shadowStyle.run {
                val minValue = styleShadowPropretiesSlider.valueFrom
                styleShadowPropretiesSlider.value = when (checkedId) {
                    R.id.shadowRadius -> if (radius >= minValue) radius else minValue
                    R.id.shadowXposition -> if (dx >= minValue) dx else minValue
                    R.id.shadowYposition -> if (dy >= minValue) dy else minValue
                    else -> 0.10f
                }
            }

            styleShadowPropretiesSlider.isEnabled = (optionsGroup.checkedRadioButtonId != -1)
        }
    }

    private fun handleSlider(checkedID: Int, value: Float) {
        when (checkedID) {
            R.id.shadowYposition -> style.shadowStyle.dy = value
            R.id.shadowXposition -> style.shadowStyle.dx = value
            R.id.shadowRadius -> style.shadowStyle.radius = value
        }
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

            style = Style.emptyStyle
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
            updateStyle()

        }
    }

    private fun getColorGallery() {
        colorAdapter = RecyclerColorAdapter(context) {
            if (!viewBind.styleShadowColor.isChecked) {
                style.textColor = it
                colorAdapter?.updateTextColor(it)
            } else {
                style.shadowStyle.shadowColor = it
                colorAdapter?.updateShadowColor(it)
            }
            updateStyle()
        }
        viewBind.stylecolorGallery.adapter = colorAdapter
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
            styleText.run {
                defineTextAlignment(style.textAlignment)
                setTextColor(Color.parseColor(style.textColor))
                typeface = FontUtils.getTypeFace(context, style.font)
                style.shadowStyle.run {
                    setShadowLayer(radius, dx, dy, Color.parseColor(shadowColor))
                }
            }
            colorAdapter?.run {
                updateTextColor(style.textColor)
                updateShadowColor(style.shadowStyle.shadowColor)
            }
            fontsTabs.getTabAt(style.font)?.select()
        }
    }

}