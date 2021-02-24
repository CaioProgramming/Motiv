package com.creat.motiv.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.text.format.DateUtils
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.creat.motiv.R
import com.creat.motiv.databinding.ActivityFullQuoteBinding
import com.creat.motiv.model.beans.Quote
import com.creat.motiv.utilities.TextUtils
import com.creat.motiv.utilities.autoSizeText
import com.creat.motiv.utilities.popIn
import com.creat.motiv.view.binders.UserViewBinder
import com.skydoves.balloon.*
import com.skydoves.balloon.overlay.BalloonOverlayRect
import kotlinx.android.synthetic.main.activity_full_quote.*
import kotlinx.android.synthetic.main.activity_full_quote.author_text_view
import kotlinx.android.synthetic.main.activity_full_quote.quoteCard
import kotlinx.android.synthetic.main.activity_full_quote.quoteTextView


class FullQuoteActivity : AppCompatActivity(R.layout.activity_full_quote) {

    lateinit var actbind: ActivityFullQuoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_full_quote)
        actbind = DataBindingUtil.setContentView(this, R.layout.activity_full_quote)
        setSupportActionBar(toolbar)
        supportActionBar.setDisplayHomeAsUpEnabled(true)
        val quote = intent.getSerializableExtra("Quote") as? Quote
        quote?.let { setupQuote(it) }

    }


    fun setupQuote(quote: Quote) {
        val animationDrawable = mainContainer.background as AnimationDrawable
        animationDrawable.run {
            setEnterFadeDuration(3000)
            setExitFadeDuration(4500)
            start()
        }
        quoteCard.setCardBackgroundColor(quote.intBackColor())
        author_text_view.text = quote.author
        author_text_view.typeface = TextUtils.getTypeFace(this, TextUtils.fonts()[quote.font].path)
        author_text_view.setTextColor(quote.intTextColor())
        quoteTextView.run {
            setOnClickListener {
                setSelectAllOnFocus(true)
                isSelected = true
                requestFocus()
                copyText(quote)
                autoSizeText()
            }
            text = quote.quote
            typeface = TextUtils.getTypeFace(context, TextUtils.fonts()[quote.font].path)
            setTextColor(quote.intTextColor())

        }
        quoteDate.text = TextUtils.data(qDate = quote.data)
        UserViewBinder(quote.userID, this, actbind.userTop)

    }

    private fun copyText(quote: Quote) {
        val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(quote.author, quote.quote)
        clipboard.setPrimaryClip(clip)
        val balloon = createBalloon(this) {
            setArrowSize(10)
            setWidthRatio(0.5f)
            setHeight(70)
            setCornerRadius(10f)
            setArrowOrientation(ArrowOrientation.BOTTOM)
            setAutoDismissDuration(5000)
            setOverlayColor(R.attr.blurOverLayColor)
            setOverlayShape(BalloonOverlayRect)
            setPadding(8)
            setText("copiada para a área de transferência")
            setTextColorResource(R.color.white)
            setBackgroundColorResource(R.color.colorAccent)
            setBalloonAnimation(BalloonAnimation.OVERSHOOT)
            setLifecycleOwner(lifecycleOwner)
        }
        quoteTextView.showAlignTop(balloon)
    }

}