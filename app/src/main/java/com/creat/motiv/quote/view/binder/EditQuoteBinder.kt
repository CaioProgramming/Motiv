package com.creat.motiv.quote.view.binder


import android.graphics.Color
import android.os.Handler

import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener

import com.creat.motiv.R
import com.creat.motiv.databinding.NewquotepopupBinding
import com.creat.motiv.quote.beans.QuoteStyle
import com.creat.motiv.quote.beans.TextSize
import com.ilustris.motiv.base.utils.FontUtils
import com.ilustris.motiv.base.presenter.QuotePresenter

import com.ilustris.animations.bounce
import com.ilustris.animations.fadeIn
import com.ilustris.animations.fadeOut
import com.ilustris.motiv.base.utils.TextUtils
import com.ilustris.motiv.base.Tools
import com.ilustris.motiv.base.beans.Quote
import com.ilustris.motiv.base.utils.autoSizeText
import com.silent.ilustriscore.core.model.DTOMessage
import com.silent.ilustriscore.core.utilities.OperationType
import com.silent.ilustriscore.core.utilities.showSnackBar
import com.silent.ilustriscore.core.view.BaseView
import com.skydoves.balloon.*
import java.util.*


class EditQuoteBinder(
        var quote: Quote? = null,
        override val viewBind: NewquotepopupBinding) : BaseView<Quote>() {

    override val presenter = QuotePresenter(this)
    private val styleFormBinder = QuoteStyleFormBinder(viewBind.stylesPager, this::updateStyle)

    init {
        initView()
    }


    private fun updateStyle(quoteStyle: QuoteStyle) {
        viewBind.run {
            animateText(quoteStyle)
        }
        quote?.style = quoteStyle.id
    }

    override fun onLoading() {
        viewBind.editloading.fadeIn()
        viewBind.saveQuoteButton.text = ""
    }

    override fun onLoadFinish() {
        Handler().postDelayed({
            viewBind.editloading.fadeOut()
            viewBind.saveQuoteButton.text = context.getString(R.string.save_quote_button)
        }, 1500)

    }

    override fun initView() {
        if (quote == null) {
            quote = emptyQuote()
        }
        styleFormBinder.initView()
        showData(quote!!)
        showBalloonTip()

    }

    private fun showBalloonTip() {
        val balloon = createBalloon(context) {
            setArrowSize(10)
            setWidthRatio(0.5f)
            setHeight(50)
            setCornerRadius(10f)
            setWidth(100)
            setArrowOrientation(ArrowOrientation.BOTTOM)
            setAutoDismissDuration(5000)
            setText("Deslize para ver estilos diferentes")
            setTextColorResource(R.color.white)
            setBackgroundColorResource(R.color.md_black)
            setBalloonAnimation(BalloonAnimation.ELASTIC)
            setOnBalloonDismissListener {
                styleFormBinder.goToRandomStyle()
            }
            setLifecycleOwner(lifecycleOwner)
        }
        viewBind.quoteTextView.showAlignTop(balloon)
    }

    override fun showData(data: Quote) {
        viewBind.run {
            quoteTextView.setText(data.quote)
            authorTextView.setText(data.author)
            saveQuoteButton.setOnClickListener {
                saveQuote()
            }
            styleFormBinder.goToStyle(data.style)
        }
    }


    private fun NewquotepopupBinding.animateText(quoteStyle: QuoteStyle) {
        quoteTextView.typeface = FontUtils.getTypeFace(context, quoteStyle.font)
        authorTextView.typeface = FontUtils.getTypeFace(context, quoteStyle.font)
        val textColor = Color.parseColor(quoteStyle.textColor)
        quoteTextView.setTextColor(textColor)
        authorTextView.setTextColor(textColor)
        quoteTextView.bounce()
        authorTextView.bounce()
        val maxSize = when (quoteStyle.textSize) {
            TextSize.DEFAULT -> R.dimen.default_quote_size
            TextSize.BIG -> R.dimen.big_quote_size
            TextSize.SMALL -> R.dimen.low_quote_size
            TextSize.EXTRASMALL -> R.dimen.min_quote_size
        }
        quoteTextView.addTextChangedListener {
            quoteTextView.autoSizeText(maxSize)
        }
    }


    private fun actualday(): Date {
        return Calendar.getInstance().time
    }

    override fun getCallBack(dtoMessage: DTOMessage) {
        super.getCallBack(dtoMessage)
        if (dtoMessage.operationType == OperationType.DATA_SAVED) {
            showSnackBar(context, message = "Citação salva com sucesso!", backColor = context.resources.getColor(R.color.material_green500), rootView = viewBind.root)
            quote = emptyQuote()
            quote?.let { showData(it) }
        } else if (dtoMessage.operationType == OperationType.DATA_UPDATED) {
            showSnackBar(context, message = "Citação atualizada com sucesso!", backColor = context.resources.getColor(R.color.material_green500), rootView = viewBind.root)
            if (context is AppCompatActivity) {
                (context as AppCompatActivity).finish()
            }
        }
    }

    private fun saveQuote() {
        quote?.let {
            it.quote = viewBind.quoteTextView.text.toString()
            it.author = viewBind.authorTextView.text.toString()
            if (it.quote.isNotEmpty()) presenter.saveData(it, it.id) else showSnackBar(context, message = Tools.emptyQuote(), rootView = viewBind.root)
        }
    }

    private fun emptyQuote(): Quote {
        val firebaseUser = presenter.user

        return Quote(
                data = actualday(),
                author = firebaseUser?.displayName ?: "",
                userID = firebaseUser?.uid ?: ""
        )
    }

}