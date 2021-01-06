package com.creat.motiv.view.binders

import android.app.Activity
import android.app.ActivityOptions
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Vibrator
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import com.creat.motiv.FontUtils
import com.creat.motiv.R
import com.creat.motiv.databinding.QuotesCardBinding
import com.creat.motiv.model.QuoteModel
import com.creat.motiv.model.beans.Quote
import com.creat.motiv.presenter.QuotePresenter
import com.creat.motiv.utilities.autoSizeText
import com.creat.motiv.utilities.blurView
import com.creat.motiv.utilities.getSupportFragmentManager
import com.creat.motiv.utilities.unblurView
import com.creat.motiv.view.activities.EditQuoteActivity
import com.creat.motiv.view.adapters.CardLikeAdapter
import com.devs.readmoreoption.ReadMoreOption
import com.ilustriscore.core.base.BaseView
import com.ilustriscore.core.utilities.ColorUtils
import com.ilustriscore.core.utilities.TextUtils
import com.ilustriscore.core.view.dialog.VerticalDialog
import com.skydoves.balloon.ArrowOrientation
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.createBalloon
import com.skydoves.balloon.showAlignBottom

class QuoteCardBinder(
        var quote: Quote,
        override val context: Context,
        override val viewBind: QuotesCardBinding) : BaseView<Quote>() {


    override fun presenter(): QuotePresenter = QuotePresenter(this)


    init {
        initView()
    }

    override fun initView() {
        showData(quote)
    }


    private fun QuotesCardBinding.setupQuote() {
        quoteCard.setCardBackgroundColor(quote.intBackColor())
        quoteTextView.text = quote.quote
        authorTextView.text = quote.author
        quoteTextView.typeface = TextUtils.getTypeFace(context, FontUtils.fonts()[quote.font].path)
        authorTextView.typeface = TextUtils.getTypeFace(context, FontUtils.fonts()[quote.font].path)
        quoteTextView.setTextColor(quote.intTextColor())
        authorTextView.setTextColor(quote.intTextColor())
        val color = ColorUtils.lighten(quote.intTextColor(), 0.8)
        val readMoreOption = ReadMoreOption.Builder(context)
                .textLength(200, ReadMoreOption.TYPE_CHARACTER)
                .moreLabel("\nVer mais...")
                .lessLabel("\nVer menos")
                .moreLabelColor(color)
                .lessLabelColor(color)
                .expandAnimation(true)
                .build()
        readMoreOption.addReadMoreTo(quoteTextView, quote.quote)
        quoteDate.text = TextUtils.data(quote.data)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            quoteTextView.autoSizeText()
        }
        onLoadFinish()
    }

    private fun QuotesCardBinding.setupCard() {
        quoteCard.setOnLongClickListener {
            showQuotePopupMenu()
            false
        }
        like.isChecked = quote.likes.contains(presenter().currentUser?.uid)
        like.setOnClickListener {
            if (quote.likes.contains(presenter().currentUser?.uid)) {
                presenter().deslikeQuote(quote)
            } else {
                presenter().likeQuote(quote)
            }
        }
        likers.run {
            val linearLayoutManager = LinearLayoutManager(context, HORIZONTAL, false)
            linearLayoutManager.stackFromEnd = true
            layoutManager = linearLayoutManager
            adapter = CardLikeAdapter(quote.likes.toList(), context)
        }
        if (quote.intBackColor() == Color.BLACK) {
            if (!like.isChecked) {
                like.backgroundTintList = ColorStateList.valueOf(Color.LTGRAY)
            } else {
                like.backgroundTintList = ColorStateList.valueOf(Color.RED)

            }
        }
    }

    private fun showQuotePopupMenu() {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            val mVibratePattern = longArrayOf(50, 100)
            vibrator.vibrate(mVibratePattern, -1) // for 500 ms
        }
        quotePopup()
    }

    private fun quotePopup() {
        val popup = PopupMenu(context, viewBind.quoteTextView)
        popup.run {
            menuInflater.inflate(R.menu.quotemenu, popup.menu)
            val user = presenter().currentUser
            val editItem = menu.findItem(R.id.quoteEdit)
            editItem.isVisible = user?.uid.equals(quote.userID)
            val deleteItem = menu.findItem(R.id.quoteDelete)
            deleteItem.isVisible = user?.uid.equals(quote.userID)
            val item = menu.getItem(3)
            val span = SpannableString(item.title)
            span.setSpan(ForegroundColorSpan(ContextCompat.getColor(context, com.ilustriscore.R.color.red_01)), 0, item.title.length, 0)
            item.title = span
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                setForceShowIcon(true)
            }
            setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                override fun onMenuItemClick(item: MenuItem?): Boolean {
                    item?.let {
                        when (it.itemId) {
                            R.id.quoteCopy -> {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText(quote.author, quote.quote)
                                clipboard.setPrimaryClip(clip)
                                val balloon = createBalloon(context) {
                                    setArrowSize(10)
                                    setWidthRatio(0.5f)
                                    setHeight(70)
                                    setCornerRadius(10f)
                                    setArrowOrientation(ArrowOrientation.TOP)
                                    setAutoDismissDuration(5000)
                                    setPadding(8)
                                    setText("copiada para a área de transferência")
                                    setTextColorResource(R.color.white)
                                    setBackgroundColorResource(R.color.colorAccent)
                                    setBalloonAnimation(BalloonAnimation.OVERSHOOT)
                                    setLifecycleOwner(lifecycleOwner)
                                }
                                viewBind.quoteTextView.showAlignBottom(balloon)
                                return true
                            }
                            R.id.quoteShare -> {
                                Intent(Intent.ACTION_SEND).apply {
                                    type = "text/pain"
                                    putExtra(Intent.EXTRA_SUBJECT, "Motiv")
                                    putExtra(Intent.EXTRA_TEXT, quote.quote + " -" + quote.author)
                                    context.startActivity(Intent.createChooser(this, "Escolha onde quer compartilhar"))

                                }

                                return true
                            }
                            R.id.quoteReport -> {
                                context.getSupportFragmentManager()?.let { it1 ->
                                    VerticalDialog.build(R.id.rootblur, it1,
                                            context.getString(R.string.report_message), okMessage = "denunciar", okClick = { QuoteModel(presenter()).denunciar(quote) })
                                }
                                return true
                            }
                            R.id.quoteEdit -> {
                                editQuote()
                                return true
                            }
                            R.id.quoteDelete -> {
                                presenter().delete(quote.id)
                                return true
                            }
                            else -> return false
                        }
                    }
                    return false
                }
            })
            setOnDismissListener {
                unblurView(context, R.id.rootblur)
            }
            show()
            blurView(context, R.id.rootblur)

        }
    }


    fun editQuote() {
        val i = Intent(context, EditQuoteActivity::class.java)
        i.putExtra("Quote", quote)
        val options = ActivityOptions.makeSceneTransitionAnimation(context as Activity,
                android.util.Pair(viewBind.quoteCard as View, context.getString(R.string.card_transaction)))
        context.startActivity(i, options.toBundle())
    }

    override fun showData(data: Quote) {
        this.quote = data
        fillQuoteData(data)
    }

    private fun fillQuoteData(data: Quote) {
        UserViewBinder(data.userID, context, viewBind.userTop)
        CardLikeAdapter(data.likes.toList(), context)
        viewBind.run {
            setupQuote()
            setupCard()
        }
    }


}