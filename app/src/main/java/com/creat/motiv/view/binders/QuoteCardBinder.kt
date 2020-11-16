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
import android.os.Handler
import android.os.Vibrator
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.View.VISIBLE
import android.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import com.creat.motiv.R
import com.creat.motiv.databinding.QuotesCardBinding
import com.creat.motiv.model.QuoteModel
import com.creat.motiv.model.beans.Quote
import com.creat.motiv.presenter.QuotePresenter
import com.creat.motiv.utilities.*
import com.creat.motiv.view.BaseView
import com.creat.motiv.view.activities.EditQuoteActivity
import com.creat.motiv.view.adapters.CardLikeAdapter
import com.devs.readmoreoption.ReadMoreOption
import java.text.SimpleDateFormat
import java.util.*

class QuoteCardBinder(
        var quote: Quote,
        override val context: Context,
        override val viewBind: QuotesCardBinding) : BaseView<Quote>() {


    override fun presenter(): QuotePresenter = QuotePresenter(this)
    val userBinder = UserViewBinder(quote.userID, context, viewBind.userTop)
    val likeAdapter = CardLikeAdapter(quote.likes.toList(), context)

    init {
        initView()
    }

    override fun initView() {
        onLoading()
        viewBind.run {
            Log.d(javaClass.simpleName, "initView: setup view")
            setupQuote()
            setupCard()
        }
        onLoadFinish()
    }


    private fun QuotesCardBinding.setupQuote() {
        quoteCard.setCardBackgroundColor(quote.intBackColor())
        quoteTextView.text = quote.quote
        authorTextView.text = quote.author
        quoteTextView.typeface = Tools.fonts(context)[quote.font]
        authorTextView.typeface = Tools.fonts(context)[quote.font]
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
        quoteDate.text = data()
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
                like.backgroundTintList = ColorStateList.valueOf(context.resources.getColor(R.color.material_grey300))
            } else {
                like.backgroundTintList = ColorStateList.valueOf(context.resources.getColor(R.color.material_red500))

            }
        }
       onLoadFinish()
    }

    private fun showQuotePopupMenu() {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            val mVibratePattern = longArrayOf(50, 100)
            vibrator.vibrate(mVibratePattern, -1) // for 500 ms
        }
        val popup = PopupMenu(context, viewBind.quoteTextView)
        popup.run {
            menuInflater.inflate(R.menu.quotemenu, popup.menu)
            val user = presenter().currentUser
            val editItem = menu.findItem(R.id.quoteEdit)
            editItem.isVisible = user?.uid.equals(quote.userID)
            val deleteItem = menu.findItem(R.id.quoteDelete)
            deleteItem.isVisible = user?.uid.equals(quote.userID)
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
                                snackmessage(context, message = "Frase de ${quote.author} copiada para a área de transferência")
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
                                val alert = Alert(context as Activity)
                                alert.showAlert(
                                        message = context.getString(R.string.report_message),
                                        buttonMessage = "denunciar",
                                        icon = R.drawable.ic_astronaut,
                                        okClick = {
                                            QuoteModel(presenter()).denunciar(quote)
                                        }, cancelClick = null)
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
                unblurView(context)
            }
            show()
            blurView(context)

        }
    }


    fun editQuote() {
        val i = Intent(context, EditQuoteActivity::class.java)
        i.putExtra("Quote", quote)
        val options = ActivityOptions.makeSceneTransitionAnimation(context as Activity,
                android.util.Pair(viewBind.quoteTextView as View, "quote"),
                android.util.Pair(viewBind.authorTextView as View, "author"))
        context.startActivity(i, options.toBundle())
    }

    fun data(): String {
        val postdia = quote.data
        val now = Calendar.getInstance().time
        val fmt = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale.getDefault())
        val dayCount = ((now.time - postdia.time) / 1000 / 60 / 60 / 24).toInt()
        return when {
            dayCount < 1 -> {
                "Hoje"
            }
            dayCount == 1 -> {
                "Ontem"
            }
            dayCount <= 6 -> "Há ${dayCount} dias"

            dayCount == 7 -> "Há 1 semana"

            dayCount <= 28 -> "Há ${dayCount / 7} semanas"

            dayCount == 30 -> "Há 1 mês"

            dayCount <= 90 -> "Há ${dayCount / 30} meses"

            else -> {
                fmt.format(postdia)
            }
        }
    }

    override fun onLoading() {
        if (viewBind.cardShimmer.quoteShimmer.visibility == VISIBLE) {
            viewBind.cardShimmer.quoteShimmer.run {
                viewBind.quoteMainView.invisible()
                fadeIn()
                startShimmer()
            }
        }

    }

    override fun onLoadFinish() {
        if (viewBind.cardShimmer.quoteShimmer.visibility == VISIBLE) {
            viewBind.cardShimmer.quoteShimmer.run {
                Handler().postDelayed({
                    stopShimmer()
                    fadeOut()
                    viewBind.quoteMainView.fadeIn()
                }, 2500)

            }
        }

    }


    override fun showData(data: Quote) {
        this.quote = data
        initView()
    }


}