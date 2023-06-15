package com.creat.motiv.features.newquote

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.ColorUtils
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.creat.motiv.R
import com.creat.motiv.databinding.NewQuoteFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.ilustris.animations.slideInBottom
import com.ilustris.motiv.base.Tools
import com.ilustris.motiv.base.adapters.StylesAdapter
import com.ilustris.motiv.base.beans.DEFAULT_STYLE_ID
import com.ilustris.motiv.base.beans.Style
import com.ilustris.motiv.base.beans.quote.Quote
import com.ilustris.motiv.base.utils.defineTextAlignment
import com.ilustris.motiv.base.utils.getTypefaceStyle
import com.ilustris.motiv.manager.features.style.adapter.StylePreviewAdapter
import com.ilustris.ui.extensions.gone
import com.ilustris.ui.extensions.showSnackBar
import com.ilustris.ui.extensions.visible
import com.silent.ilustriscore.core.model.ViewModelBaseState
import com.silent.ilustriscore.core.utilities.delayedFunction
import java.util.Calendar
import kotlin.random.Random


class NewQuoteFragment : Fragment() {
    private val args: NewQuoteFragmentArgs? by navArgs()
    private var quote = Quote()
    private var newQuoteFragmentBinding: NewQuoteFragmentBinding? = null
    private val newQuoteViewModel by lazy { NewQuoteViewModel(requireActivity().application) }
    private var stylePreviewAdapter =
        StylePreviewAdapter(ArrayList(), true, quote.style, ::selectStyle)
    private var styleAdapter = StylePreviewAdapter(ArrayList(), false, quote.style, ::selectStyle)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        newQuoteFragmentBinding = NewQuoteFragmentBinding.inflate(inflater, container, false)
        return newQuoteFragmentBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args?.quote?.let {
            quote = it
        }
        newQuoteFragmentBinding?.setupForm()
        observeViewModel()
        newQuoteViewModel.getStyles()
    }

    private fun observeViewModel() {
        newQuoteViewModel.newQuoteViewState.observe(viewLifecycleOwner) {
            when (it) {
                is NewQuoteViewState.StylesRetrieved -> setupStyles(ArrayList(it.styles))
                NewQuoteViewState -> view?.showSnackBar(Tools.emptyQuote(), backColor = Color.RED)
                else -> {}
            }
        }
        newQuoteViewModel.viewModelState.observe(viewLifecycleOwner) {
            when (it) {
                is ViewModelBaseState.DataSavedState -> {
                    requireView().showSnackBar(
                        "Post publicado!", backColor = requireContext().getColor(
                            R.color.colorAccent
                        )
                    )
                    delayedFunction(5000) {
                        findNavController().popBackStack()
                    }
                }
                is ViewModelBaseState.DataUpdateState -> {
                    requireView().showSnackBar(
                        "Post atualizado!", backColor = requireContext().getColor(
                            R.color.colorAccent
                        )
                    )
                    delayedFunction(5000) {
                        findNavController().popBackStack()
                    }
                }

                is ViewModelBaseState.ErrorState -> {
                    view?.showSnackBar(it.dataException.code.message, backColor = Color.RED)
                }

                else -> {}
            }
        }
    }

    private fun selectStyle(style: Style, position: Int) {
        quote.style = style.id
        stylePreviewAdapter.setSelectedStyle(style.id)
        styleAdapter.setSelectedStyle(style.id)
        newQuoteFragmentBinding?.run {
            stylesPager.setCurrentItem(position, true)
            setupStyle(style)
            BottomSheetBehavior.from(stylesContainer).state = BottomSheetBehavior.STATE_COLLAPSED
            stylePreviewRecycler.smoothScrollToPosition(position)
        }


    }

    private fun NewQuoteFragmentBinding.setupStyle(style: Style) {
        quoteTextView.setTypeface(
            style.typeface,
            style.fontStyle.getTypefaceStyle()
        )
        authorTextView.setTypeface(
            style.typeface,
            style.fontStyle.getTypefaceStyle()
        )
        val textColor = Color.parseColor(style.textColor)
        quoteTextView.setHintTextColor(
            ColorUtils.setAlphaComponent(
                textColor,
                70
            )
        )
        authorTextView.setHintTextColor(
            ColorUtils.setAlphaComponent(
                textColor,
                70
            )
        )
        quoteTextView.defineTextAlignment(style.textAlignment)
        authorTextView.defineTextAlignment(style.textAlignment)
        quoteTextView.setTextColor(textColor)
        authorTextView.setTextColor(textColor)
        style.shadowStyle.run {
            quoteTextView.setShadowLayer(radius, dx, dy, Color.parseColor(shadowColor))
            authorTextView.setShadowLayer(radius, dx, dy, Color.parseColor(shadowColor))
        }
    }

    private fun setupStyles(styles: ArrayList<Style>) {
        stylePreviewAdapter =
            StylePreviewAdapter(ArrayList(styles), true, quote.style, ::selectStyle)
        styleAdapter = StylePreviewAdapter(ArrayList(styles), false, quote.style, ::selectStyle)
        newQuoteFragmentBinding?.run {
            stylePreviewRecycler.adapter = stylePreviewAdapter
            stylesPager.adapter = StylesAdapter(styles)
            stylesPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                    if (state == ViewPager.SCROLL_STATE_IDLE) {
                        selectStyle(styles[stylesPager.currentItem], stylesPager.currentItem)
                    }
                }
            })
            availableStyles.text = "${styles.size} estilos disponíveis"
            stylePreviewRecycler.slideInBottom()
        }
        if (quote.style != DEFAULT_STYLE_ID) {
            val currentStyle = styles.indexOfFirst { it.id == quote.style }
            selectStyle(styles[currentStyle], currentStyle)
        } else {
            delayedFunction(3000) {
                if (quote.style == Style.defaultStyle.id) {
                    newQuoteFragmentBinding?.stylesPager?.setCurrentItem(
                        Random.nextInt(styles.size),
                        true
                    )
                }
            }
        }

    }

    private fun NewQuoteFragmentBinding.setupForm() {
        cancelButton.setOnClickListener {
            findNavController().popBackStack()
        }
        saveQuoteButton.setOnClickListener {
            if (quote.id.isNotEmpty()) {
                quote.quote = quoteTextView.text.toString()
                quote.author = authorTextView.text.toString()
                newQuoteViewModel.editData(quote)
            } else {
                newQuoteViewModel.saveData(getQuote())
            }
        }
        quoteTextView.addTextChangedListener {
            saveQuoteButton.isEnabled = quoteTextView.text.toString().isNotEmpty()
        }
        quoteTextView.setText(quote.quote)
        authorTextView.setText(quote.author)
        val bottomSheetBehavior = BottomSheetBehavior.from(stylesContainer)
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    availableStyles.gone()
                    stylePreviewRecycler.adapter = stylePreviewAdapter
                    stylePreviewRecycler.layoutManager =
                        GridLayoutManager(requireContext(), 1, RecyclerView.HORIZONTAL, false)
                    if (quote.style != Style.defaultStyle.id) {
                        stylePreviewRecycler.smoothScrollToPosition(styleAdapter.styles.indexOfFirst { it.id == quote.style })
                    }
                } else if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    availableStyles.visible()
                    stylePreviewRecycler.visible()
                } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    stylePreviewRecycler.adapter = styleAdapter
                    stylePreviewRecycler.layoutManager =
                        GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
                    if (quote.style != Style.defaultStyle.id) {
                        stylePreviewRecycler.smoothScrollToPosition(styleAdapter.styles.indexOfFirst { it.id == quote.style })
                    }

                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                availableStyles.scaleX = slideOffset
                availableStyles.scaleY = slideOffset
            }
        })
    }

    private fun NewQuoteFragmentBinding.getQuote(): Quote {
        return quote.apply {
            quote = quoteTextView.text.toString()
            author = authorTextView.text.toString()
            if (id.isEmpty()) {
                data = Calendar.getInstance().time
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        newQuoteFragmentBinding = null
    }


}