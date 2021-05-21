package com.creat.motiv.quote.view.binder

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityOptions
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.os.Vibrator
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import com.creat.motiv.BuildConfig
import com.creat.motiv.R
import com.creat.motiv.quote.EditQuoteActivity
import com.creat.motiv.quote.view.QuoteShareDialog
import com.creat.motiv.view.adapters.CardLikeAdapter
import com.ilustris.animations.fadeIn
import com.ilustris.animations.slideInBottom
import com.ilustris.motiv.base.beans.Quote
import com.ilustris.motiv.base.beans.Style
import com.ilustris.motiv.base.binder.UserViewBinder
import com.ilustris.motiv.base.databinding.QuotesCardBinding
import com.ilustris.motiv.base.dialog.BottomSheetAlert
import com.ilustris.motiv.base.dialog.DefaultAlert
import com.ilustris.motiv.base.dialog.listdialog.DialogItems
import com.ilustris.motiv.base.dialog.listdialog.ListDialog
import com.ilustris.motiv.base.dialog.listdialog.ListDialogBean
import com.ilustris.motiv.base.presenter.QuotePresenter
import com.ilustris.motiv.base.utils.*
import com.silent.ilustriscore.core.utilities.ColorUtils
import com.silent.ilustriscore.core.utilities.gone
import com.silent.ilustriscore.core.utilities.showSnackBar
import com.silent.ilustriscore.core.utilities.visible
import com.silent.ilustriscore.core.view.BaseView
import java.io.File
import java.io.FileOutputStream


class QuoteCardBinder(
    var quote: Quote,
    override val viewBind: QuotesCardBinding
) : BaseView<Quote>() {


    override val presenter = QuotePresenter(this)
    var quoteStyle: Style? = null

    private fun updateStyle(quoteStyle: Style) {
        this.quoteStyle = quoteStyle
        viewBind.run {
            FontUtils.getTypeFace(context, quoteStyle.font)?.let {
                quoteTextView.setTypeface(it, quoteStyle.fontStyle.getTypefaceStyle())
                authorTextView.setTypeface(it, quoteStyle.fontStyle.getTypefaceStyle())
            }
            val color = Color.parseColor(quoteStyle.textColor)
            quoteTextView.setTextColor(color)
            authorTextView.setTextColor(color)
            quoteStyle.shadowStyle.run {
                quoteTextView.setShadowLayer(radius, dx, dy, Color.parseColor(shadowColor))
                authorTextView.setShadowLayer(radius, dx, dy, Color.parseColor(shadowColor))
            }
            quoteTextView.defineTextAlignment(quoteStyle.textAlignment)
            authorTextView.defineTextAlignment(quoteStyle.textAlignment)
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

    private fun QuotesCardBinding.setupCard() {
        quoteTextView.setOnLongClickListener {
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (vibrator.hasVibrator()) {
                val mVibratePattern = longArrayOf(60, 60)
                vibrator.vibrate(mVibratePattern, -1)
            }
            copyToClipboard()
            showSnackBar(context, "Copiado para área de transferência", rootView = root)
            false
        }
        like.run {
            isChecked = quote.likes.contains(presenter.user?.uid)
            setOnClickListener {
                if (quote.likes.contains(presenter.user?.uid)) {
                    presenter.deslikeQuote(quote)
                } else {
                    presenter.likeQuote(quote)
                }
            }
        }
        likers.run {
            val linearLayoutManager = LinearLayoutManager(context, HORIZONTAL, false)
            linearLayoutManager.stackFromEnd = true
            layoutManager = linearLayoutManager
            adapter = CardLikeAdapter(quote.likes.toList(), context)
        }
        optionsButton.setOnClickListener {
            ListDialog(context, getQuoteOptions(), {
                if (quote.userID == presenter.user?.uid) {
                    BottomSheetAlert(
                        context,
                        "Opa, calma aí!",
                        "Você quer mesmo remover esse post?",
                        {
                            presenter.delete(quote.id)
                        }).buildDialog()

                } else {
                    DefaultAlert(
                        context,
                        "Eita, isso é muito sério!",
                        context.getString(R.string.report_quote_message),
                        R.drawable.surprised_avatar,
                        okClick = {
                            if (quote.isReport) {
                                DefaultAlert(
                                    context, "Opa!",
                                    "Esta publicação já foi denunciada e está sendo analisada!"
                                ).buildDialog()
                            } else {
                                presenter.reportQuote(quote)
                                DefaultAlert(
                                    context, "Denúncia realizada",
                                    "A sua denúncia foi enviada com sucesso e já está sendo analisada!"
                                ).buildDialog()
                            }
                        }).buildDialog()
                }
            }, DialogStyles.BOTTOM_NO_BORDER).buildDialog()
        }
        editButton.run {
            if (quote.userID != presenter.user?.uid) {
                gone()
            } else visible()
            setOnClickListener {
                editQuote()
            }
        }
        shareButton.setOnClickListener {
            quoteStyle?.let { style ->
                QuoteShareDialog(context, quote, style).buildDialog()
            }
        }
        if (quoteCard.visibility == View.GONE) quoteCard.fadeIn()

    }

    private fun getQuoteOptions(): DialogItems {
        val dialogList = ArrayList<ListDialogBean>()
        if (quote.userID == presenter.user?.uid) {
            dialogList.add(ListDialogBean("Remover publicação"))
        } else {
            dialogList.add(ListDialogBean("Denunciar publicação"))
        }

        return dialogList.toList()
    }

    private fun copyToClipboard() {
        val clipboard: ClipboardManager? = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
        val clip = ClipData.newPlainText("Motiv", "${quote.quote}\uD83E\uDE90\n - ${quote.author} \n\n#${context.getString(R.string.app_name).toLowerCase()} #${quote.author.replace(" ", "").toLowerCase()}")
        clipboard?.setPrimaryClip(clip)
    }

    private fun editQuote() {
        val i = Intent(context, EditQuoteActivity::class.java)
        i.putExtra("Quote", quote)
        val options = ActivityOptions.makeSceneTransitionAnimation(
            context as Activity,
            android.util.Pair(
                viewBind.quoteCard as View,
                context.getString(R.string.quote_transaction)
            )
        )
        context.startActivity(i, options.toBundle())
    }

    override fun showData(data: Quote) {
        this.quote = data
        fillQuoteData(data)
    }

    private fun fillQuoteData(data: Quote) {
        viewBind.shareButton.gone()
        if (data.isUserQuote()) {
            UserViewBinder(data.userID, viewBind.userTop).setDate(TextUtils.data(quote.data))
            viewBind.userTop.run {
                username.setTextColor(Color.WHITE)
                quoteDate.setTextColor(Color.WHITE)
                logo.imageTintList = ColorStateList.valueOf(Color.WHITE)
            }
            CardLikeAdapter(data.likes.toList(), context)
            if (viewBind.quoteOptions.visibility == View.GONE) {
                viewBind.quoteOptions.slideInBottom()
                viewBind.userTop.userContainer.slideInBottom()
            }
            delayedFunction(1500) {
                viewBind.shareButton.slideInBottom()
            }
        } else {
            viewBind.userTop.userContainer.gone()
            viewBind.quoteOptions.gone()
            viewBind.shareButton.gone()
        }
        QuoteStyleBinder(viewBind.styleView, this::updateStyle).getStyle(data.style)
        viewBind.run {
            setupQuote()
            setupCard()
        }
    }
}