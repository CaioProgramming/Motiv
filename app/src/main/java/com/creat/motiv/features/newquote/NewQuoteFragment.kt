package com.creat.motiv.features.newquote

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.creat.motiv.R
import com.creat.motiv.databinding.NewQuoteFragmentBinding
import com.creat.motiv.features.newquote.adapter.StylePreviewAdapter
import com.ilustris.animations.bounce
import com.ilustris.animations.slideInBottom
import com.ilustris.motiv.base.Tools
import com.ilustris.motiv.base.adapters.StylesAdapter
import com.ilustris.motiv.base.beans.DEFAULT_STYLE_ID
import com.ilustris.motiv.base.beans.quote.Quote
import com.ilustris.motiv.base.beans.Style
import com.ilustris.motiv.base.utils.FontUtils
import com.ilustris.motiv.base.utils.defineTextAlignment
import com.ilustris.motiv.base.utils.getTypefaceStyle
import com.silent.ilustriscore.core.model.ViewModelBaseState
import com.silent.ilustriscore.core.utilities.delayedFunction
import com.silent.ilustriscore.core.utilities.showSnackBar
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random


class NewQuoteFragment : Fragment() {
    private val args: NewQuoteFragmentArgs? by navArgs()
    private var quote = Quote()
    private var newQuoteFragmentBinding: NewQuoteFragmentBinding? = null
    private var newQuoteViewModel = NewQuoteViewModel()
    private var stylePreviewAdapter = StylePreviewAdapter(ArrayList(), quote.style, ::selectStyle)

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
        newQuoteViewModel.newQuoteViewState.observe(this, {
            when (it) {
                is NewQuoteViewState.StylesRetrieved -> setupStyles(it.styles)
                NewQuoteViewState -> view?.showSnackBar(Tools.emptyQuote(), backColor = Color.RED)
            }
        })
        newQuoteViewModel.viewModelState.observe(this, {
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
            }
        })
    }

    private fun selectStyle(position: Int, style: Style) {
        quote.style = style.id
        stylePreviewAdapter.setSelectedStyle(style.id)
        newQuoteFragmentBinding?.run {
            stylesPager.setCurrentItem(position, true)
            stylePreviewRecycler.smoothScrollToPosition(position)
            setupStyle(style)
        }

    }

    private fun NewQuoteFragmentBinding.setupStyle(style: Style) {
        quoteTextView.setTypeface(
            FontUtils.getTypeFace(requireContext(), style.font),
            style.fontStyle.getTypefaceStyle()
        )
        authorTextView.setTypeface(
            FontUtils.getTypeFace(requireContext(), style.font),
            style.fontStyle.getTypefaceStyle()
        )
        val textColor = Color.parseColor(style.textColor)
        quoteTextView.setHintTextColor(
            androidx.core.graphics.ColorUtils.setAlphaComponent(
                textColor,
                60
            )
        )
        authorTextView.setHintTextColor(
            androidx.core.graphics.ColorUtils.setAlphaComponent(
                textColor,
                60
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
        quoteTextView.bounce()
        authorTextView.bounce()
    }

    private fun setupStyles(styles: List<Style>) {
        stylePreviewAdapter = StylePreviewAdapter(ArrayList(styles), quote.style, ::selectStyle)
        newQuoteFragmentBinding?.run {
            stylePreviewRecycler.adapter = stylePreviewAdapter
            stylesPager.adapter = StylesAdapter(styles)
            stylesPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                    if (state == ViewPager.SCROLL_STATE_IDLE) {
                        selectStyle(stylesPager.currentItem, styles[stylesPager.currentItem])
                    }
                }
            })
            availableStyles.text = "${styles.size} estilos disponíveis"
            stylePreviewRecycler.slideInBottom()
        }
        if (quote.style != DEFAULT_STYLE_ID) {
            val currentStyle = styles.indexOfFirst { it.id == quote.style }
            selectStyle(currentStyle, styles[currentStyle])
        } else {
            delayedFunction(3000) {
                newQuoteFragmentBinding?.stylesPager?.setCurrentItem(
                    Random.nextInt(styles.size),
                    true
                )
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