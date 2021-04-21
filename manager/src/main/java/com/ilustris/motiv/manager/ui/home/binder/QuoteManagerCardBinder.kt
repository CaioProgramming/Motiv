package com.ilustris.motiv.manager.ui.home.binder

import android.annotation.SuppressLint
import android.graphics.Color
import com.ilustris.animations.fadeIn
import com.ilustris.motiv.base.beans.Style
import com.ilustris.motiv.base.presenter.QuotePresenter
import com.ilustris.motiv.base.beans.Quote
import com.ilustris.motiv.base.binder.QuoteStyleBinder
import com.ilustris.motiv.base.binder.UserViewBinder
import com.ilustris.motiv.base.databinding.QuotesCardBinding
import com.ilustris.motiv.base.dialog.BottomSheetAlert
import com.ilustris.motiv.base.dialog.listdialog.ListDialog
import com.ilustris.motiv.base.dialog.listdialog.ListDialogBean
import com.ilustris.motiv.base.utils.DialogStyles
import com.ilustris.motiv.base.utils.TextUtils
import com.ilustris.motiv.base.utils.FontUtils
import com.ilustris.motiv.base.utils.defineTextAlignment
import com.silent.ilustriscore.core.utilities.gone
import com.silent.ilustriscore.core.utilities.invisible
import com.silent.ilustriscore.core.utilities.visible
import com.silent.ilustriscore.core.view.BaseView

class QuoteManagerCardBinder(
        var quote: Quote,
        override val viewBind: QuotesCardBinding) : BaseView<Quote>() {


    override val presenter = QuotePresenter(this)

    private fun updateStyle(quoteStyle: Style) {
        viewBind.run {
            quoteTextView.typeface = FontUtils.getTypeFace(context, quoteStyle.font)
            authorTextView.typeface = FontUtils.getTypeFace(context, quoteStyle.font)
            val color = Color.parseColor(quoteStyle.textColor)
            quoteTextView.setTextColor(color)
            authorTextView.setTextColor(color)
            quoteTextView.fadeIn()
            authorTextView.fadeIn()
            quoteTextView.defineTextAlignment(quoteStyle.textAlignment)
            authorTextView.defineTextAlignment(quoteStyle.textAlignment)
            quoteStyle.shadowStyle.run {
                quoteTextView.setShadowLayer(radius, dx, dy, Color.parseColor(shadowColor))
                authorTextView.setShadowLayer(radius, dx, dy, Color.parseColor(shadowColor))
            }
        }
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
            /*deleteButton.setOnClickListener {
                BottomSheetAlert(context, "Opa, calma aí!", "Você quer mesmo remover esse post?", {
                    presenter.delete(quote.id)
                }).buildDialog()
            }*/
            shareButton.gone()
            like.gone()
            likers.invisible()
        }
    }

    override fun showData(data: Quote) {
        this.quote = data
        fillQuoteData(data)
    }

    private fun fillQuoteData(data: Quote) {
        if (data.isUserQuote()) {
            UserViewBinder(data.userID, viewBind.userTop).setDate(TextUtils.data(quote.data))
            viewBind.userTop.username.setTextColor(Color.WHITE)
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