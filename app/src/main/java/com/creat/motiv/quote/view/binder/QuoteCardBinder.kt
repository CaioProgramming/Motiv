package com.creat.motiv.quote.view.binder

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Vibrator
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import com.creat.motiv.R
import com.creat.motiv.databinding.QuotesCardBinding
import com.creat.motiv.quote.beans.Quote
import com.creat.motiv.quote.beans.QuoteStyle
import com.creat.motiv.quote.beans.TextSize
import com.creat.motiv.quote.presenter.QuotePresenter
import com.creat.motiv.utilities.*
import com.creat.motiv.quote.view.EditQuoteActivity
import com.creat.motiv.view.adapters.CardLikeAdapter
import com.creat.motiv.profile.view.binders.UserViewBinder
import com.silent.ilustriscore.core.utilities.gone
import com.silent.ilustriscore.core.utilities.visible
import com.silent.ilustriscore.core.view.BaseView

class QuoteCardBinder(
        var quote: Quote,
        override val viewBind: QuotesCardBinding) : BaseView<Quote>() {


    override val presenter = QuotePresenter(this)

    private fun updateStyle(quoteStyle: QuoteStyle) {
        viewBind.run {
            quoteTextView.typeface = TextUtils.getTypeFace(context, quoteStyle.font)
            authorTextView.typeface = TextUtils.getTypeFace(context, quoteStyle.font)
            val color = Color.parseColor(quoteStyle.textColor)
            quoteTextView.setTextColor(color)
            authorTextView.setTextColor(color)
            quoteTextView.visible()
            authorTextView.visible()
            handleTextSize(quoteStyle.textSize)
        }
    }

    fun QuotesCardBinding.handleTextSize(textSize: TextSize) {
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

    private fun QuotesCardBinding.setupCard() {
        quoteTextView.setOnLongClickListener {
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (vibrator.hasVibrator()) {
                val mVibratePattern = longArrayOf(50, 200)
                vibrator.vibrate(mVibratePattern, -1)
            }
            snackmessage(context, "Copiado para área de transferência")
            false
        }
        like.isChecked = quote.likes.contains(presenter.user?.uid)
        like.setOnClickListener {
            if (quote.likes.contains(presenter.user?.uid)) {
                presenter.deslikeQuote(quote)
            } else {
                presenter.likeQuote(quote)
            }
        }
        likers.run {
            val linearLayoutManager = LinearLayoutManager(context, HORIZONTAL, false)
            linearLayoutManager.stackFromEnd = true
            layoutManager = linearLayoutManager
            adapter = CardLikeAdapter(quote.likes.toList(), context)
        }
        viewBind.deleteButton.run {
            setOnClickListener {
                presenter.delete(quote.id)
            }
            if (quote.userID == presenter.user?.uid) {
                visible()
            } else {
                gone()
            }
        }
        viewBind.editButton.run {
            if (quote.userID == presenter.user?.uid) {
                visible()
            } else {
                gone()
            }
            setOnClickListener {
                editQuote()

            }
        }

        viewBind.shareButton.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                type = "text/pain"
                putExtra(Intent.EXTRA_SUBJECT, "Motiv")
                putExtra(Intent.EXTRA_TEXT, quote.quote + " -" + quote.author)
                context.startActivity(Intent.createChooser(this, "Escolha onde quer compartilhar"))
            }
        }
    }


    private fun editQuote() {
        val i = Intent(context, EditQuoteActivity::class.java)
        i.putExtra("Quote", quote)
        val options = ActivityOptions.makeSceneTransitionAnimation(context as Activity,
                android.util.Pair(viewBind.quoteCard as View, context.getString(R.string.quote_transaction)))
        context.startActivity(i, options.toBundle())
    }

    override fun showData(data: Quote) {
        this.quote = data
        fillQuoteData(data)
    }

    private fun fillQuoteData(data: Quote) {
        if (data.isUserQuote()) {
            UserViewBinder(data.userID, viewBind.userTop).setDate(TextUtils.data(quote.data))
            CardLikeAdapter(data.likes.toList(), context)
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