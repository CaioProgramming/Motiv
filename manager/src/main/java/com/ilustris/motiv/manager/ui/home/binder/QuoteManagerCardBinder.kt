package com.ilustris.motiv.manager.ui.home.binder

import android.annotation.SuppressLint
import android.graphics.Color
import com.ilustris.motiv.base.beans.QuoteStyle
import com.ilustris.motiv.base.beans.TextSize
import com.ilustris.motiv.base.presenter.QuotePresenter
import com.ilustris.motiv.base.R
import com.ilustris.motiv.base.beans.Quote
import com.ilustris.motiv.base.binder.QuoteStyleBinder
import com.ilustris.motiv.base.binder.UserViewBinder
import com.ilustris.motiv.base.databinding.QuotesCardBinding
import com.ilustris.motiv.base.utils.TextUtils
import com.ilustris.motiv.base.utils.autoSizeText
import com.ilustris.motiv.base.utils.FontUtils
import com.silent.ilustriscore.core.utilities.gone
import com.silent.ilustriscore.core.utilities.visible
import com.silent.ilustriscore.core.view.BaseView

class QuoteManagerCardBinder(
        var quote: Quote,
        override val viewBind: QuotesCardBinding) : BaseView<Quote>() {


    override val presenter = QuotePresenter(this)

    private fun updateStyle(quoteStyle: QuoteStyle) {
        viewBind.run {
            quoteTextView.typeface = FontUtils.getTypeFace(context, quoteStyle.font)
            authorTextView.typeface = FontUtils.getTypeFace(context, quoteStyle.font)
            val color = Color.parseColor(quoteStyle.textColor)
            quoteTextView.setTextColor(color)
            authorTextView.setTextColor(color)
            quoteTextView.visible()
            authorTextView.visible()
            handleTextSize(quoteStyle.textSize)
        }
    }

    private fun QuotesCardBinding.handleTextSize(textSize: TextSize) {
        val maxSize = when (textSize) {
            TextSize.DEFAULT -> R.dimen.default_quote_size
            TextSize.BIG -> R.dimen.big_quote_size
            TextSize.SMALL -> R.dimen.low_quote_size
            TextSize.EXTRASMALL -> R.dimen.min_quote_size
        }
        quoteTextView.autoSizeText(maxSize)
    }

    init {
        initView()
    }

    override fun initView() {
        showData(quote)
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun QuotesCardBinding.setupQuote() {
        quoteTextView.text = quote.quote
        authorTextView.text = quote.author
        onLoadFinish()
        quoteCard.visible()
    }

    private fun setupCard() {
        viewBind.run {
            deleteButton.run {
                setOnClickListener {
                    presenter.delete(quote.id)
                }
                visible()
            }
            reportButton.gone()
            shareButton.gone()
            editButton.gone()
            like.gone()
            likers.gone()
        }
    }

    override fun showData(data: Quote) {
        this.quote = data
        fillQuoteData(data)
    }

    private fun fillQuoteData(data: Quote) {
        if (data.isUserQuote()) {
            UserViewBinder(data.userID, viewBind.userTop).setDate(TextUtils.data(quote.data))
        } else {
            viewBind.userTop.userContainer.gone()
            viewBind.quoteOptions.gone()
        }
        QuoteStyleBinder(viewBind.styleView, this::updateStyle).getStyle(data.style)
        viewBind.run {
            setupQuote()
            setupCard()
        }
    }


}