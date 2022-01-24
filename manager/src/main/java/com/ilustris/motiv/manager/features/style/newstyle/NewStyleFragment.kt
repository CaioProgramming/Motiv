package com.ilustris.motiv.manager.features.style.newstyle

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.giphy.sdk.core.models.Media
import com.giphy.sdk.ui.GPHContentType
import com.giphy.sdk.ui.GPHSettings
import com.giphy.sdk.ui.themes.GPHTheme
import com.giphy.sdk.ui.themes.GridType
import com.giphy.sdk.ui.views.GiphyDialogFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.ilustris.motiv.base.beans.FontStyle
import com.ilustris.motiv.base.beans.Style
import com.ilustris.motiv.base.beans.TextAlignment
import com.ilustris.motiv.base.utils.FontUtils
import com.ilustris.motiv.base.utils.defineTextAlignment
import com.ilustris.motiv.base.utils.getTypefaceStyle
import com.ilustris.motiv.base.utils.loadGif
import com.ilustris.motiv.manager.R
import com.ilustris.motiv.manager.databinding.NewStyleFormBinding
import com.ilustris.motiv.manager.features.style.adapter.StylePreviewAdapter
import com.ilustris.motiv.manager.features.style.newstyle.adapter.RecyclerColorAdapter
import com.ilustris.motiv.manager.features.style.newstyle.adapter.StyleFontsAdapter
import com.ilustris.motiv.manager.features.style.newstyle.viewmodel.NewStyleViewModel
import com.silent.ilustriscore.core.model.ViewModelBaseState
import com.silent.ilustriscore.core.utilities.showSnackBar

class NewStyleFragment : Fragment() {

    private val args: NewStyleFragmentArgs? by navArgs()
    private val newStyleViewModel = NewStyleViewModel()
    private var newStyleFormBinding: NewStyleFormBinding? = null
    private var style = Style.defaultStyle
    private var previewAdapter: StylePreviewAdapter? = null
    private var colorAdapter: RecyclerColorAdapter? = null
    private var fontAdapter: StyleFontsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        newStyleFormBinding = NewStyleFormBinding.inflate(inflater)
        return newStyleFormBinding?.root
    }


    private fun observeViewModel() {
        newStyleViewModel.viewModelState.observe(this, {
            when (it) {
                is ViewModelBaseState.DataListRetrievedState -> {
                    setupPreview(it.dataList as ArrayList<Style>)
                }
                is ViewModelBaseState.DataSavedState -> {
                    view?.showSnackBar(
                        "Estilo salvo com sucesso",
                        requireContext().getColor(R.color.colorPrimaryDark)
                    )
                }
                is ViewModelBaseState.DataUpdateState -> {
                    view?.showSnackBar(
                        "Estilo atualizado com sucesso",
                        requireContext().getColor(R.color.colorPrimaryDark)
                    )

                }
                is ViewModelBaseState.ErrorState -> {
                    view?.showSnackBar(
                        "Ocorreu um erro inesperado",
                        requireContext().getColor(R.color.material_red500)
                    )

                }
            }
        })
    }

    private fun setupPreview(styles: ArrayList<Style>) {
        previewAdapter = StylePreviewAdapter(styles, true, style.id) {
            selectStyle(it)
            previewAdapter?.setSelectedStyle(it.id)
        }
        newStyleFormBinding?.stylePreviewLayout?.stylesRecycler?.adapter = previewAdapter

    }

    private fun selectStyle(it: Style) {
        style = it
        updateStyle()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args?.style?.let {
            style = it
        }
        newStyleFormBinding?.initView()
        observeViewModel()
        newStyleViewModel.getAllData()
    }

    private fun NewStyleFormBinding.initView() {
        fontAdapter = StyleFontsAdapter {
            style.font = it
            updateStyle()
        }
        initButtons()
        getColorGallery()
    }

    private fun NewStyleFormBinding.initButtons() {
        dotslayout.motivDots.setOnClickListener {
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
            styleBackground.loadGif(style.backgroundURL)
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
        saveStyleButton.setOnClickListener {
            newStyleViewModel.saveData(style)
        }
    }

    private fun openGiphyDialog() {
        GiphyDialogFragment.newInstance(GPHSettings(GridType.waterfall, GPHTheme.Automatic)).apply {
            gifSelectionListener = object : GiphyDialogFragment.GifSelectionListener {
                override fun didSearchTerm(term: String) {

                }

                override fun onDismissed(selectedContentType: GPHContentType) {

                }

                override fun onGifSelected(
                    media: Media,
                    searchTerm: String?,
                    selectedContentType: GPHContentType
                ) {
                    media.images.downsized?.gifUrl?.let {
                        style.backgroundURL = it
                        newStyleFormBinding?.styleBackground?.loadGif(style.backgroundURL)
                        updateStyle()
                    }
                }

            }
        }.show(childFragmentManager, "GIPHYDIALOG")

    }

    fun updateStyle() {
        newStyleFormBinding?.run {
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
            updateColors()
            fontAdapter?.updateFont(style.font, style.fontStyle)
        }
    }

    private fun updateColors() {
        colorAdapter?.updateSelectedColor(style.textColor)
    }

    private fun NewStyleFormBinding.getColorGallery() {
        stylecolorGallery.run {
            colorAdapter = RecyclerColorAdapter(
                context,
                R.drawable.ic_baseline_text_fields_24
            ) {
                style.textColor = it
                updateStyle()
            }.apply {
                updateSelectedColor(style.textColor)
            }
            adapter = colorAdapter
        }
    }


}