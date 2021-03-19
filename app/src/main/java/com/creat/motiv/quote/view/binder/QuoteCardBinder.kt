package com.creat.motiv.quote.view.binder

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
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
import com.ilustris.motiv.base.beans.QuoteStyle
import com.ilustris.motiv.base.presenter.QuotePresenter
import com.creat.motiv.quote.EditQuoteActivity
import com.creat.motiv.view.adapters.CardLikeAdapter
import com.ilustris.animations.slideInBottom
import com.ilustris.motiv.base.binder.UserViewBinder
import com.ilustris.motiv.base.beans.Quote
import com.ilustris.motiv.base.databinding.QuotesCardBinding
import com.ilustris.motiv.base.dialog.BottomSheetAlert
import com.ilustris.motiv.base.dialog.DefaultAlert
import com.ilustris.motiv.base.utils.TextUtils
import com.ilustris.motiv.base.utils.FontUtils
import com.silent.ilustriscore.core.utilities.ColorUtils
import com.silent.ilustriscore.core.utilities.gone
import com.silent.ilustriscore.core.utilities.showSnackBar
import com.silent.ilustriscore.core.utilities.visible
import com.silent.ilustriscore.core.view.BaseView
import java.io.File
import java.io.FileOutputStream

class QuoteCardBinder(
        var quote: Quote,
        override val viewBind: QuotesCardBinding) : BaseView<Quote>() {


    override val presenter = QuotePresenter(this)

    private fun updateStyle(quoteStyle: QuoteStyle) {
        viewBind.run {
            quoteTextView.typeface = FontUtils.getTypeFace(context, quoteStyle.font)
            authorTextView.setTypeface(FontUtils.getTypeFace(context, quoteStyle.font), Typeface.ITALIC)
            val color = Color.parseColor(quoteStyle.textColor)
            quoteTextView.setTextColor(color)
            authorTextView.setTextColor(color)
            quoteTextView.visible()
            authorTextView.visible()
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
            showSnackBar(context, "Copiado para área de transferência", rootView = root)
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
                BottomSheetAlert(context, "Opa, calma aí!", "Você quer mesmo remover esse post?", {
                    presenter.delete(quote.id)
                }).buildDialog()
            }

            if (quote.userID == presenter.user?.uid) {
                visible()
            } else {
                gone()
            }
        }
        reportButton.setOnClickListener {
            DefaultAlert(context, "Eita, isso é muito sério!", context.getString(R.string.report_quote_message), R.drawable.surprised_avatar, okClick = {
                presenter.reportQuote(quote)
            }).buildDialog()
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
            generateCardImage { file ->
                val uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", file)
                // QuoteShareDialog(context,file).buildDialog()
                uri?.let {
                    val shareIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        type = "image/*"
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        setDataAndType(uri, context.contentResolver.getType(uri))
                        putExtra(Intent.EXTRA_SUBJECT, context.resources.getString(R.string.app_name))
                        putExtra(Intent.EXTRA_TEXT, "${quote.quote}\n - ${quote.author}")
                        putExtra(Intent.EXTRA_STREAM, uri)

                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Compartilhar post em..."))
                }
                viewBind.quoteOptions.visible()
                viewBind.userTop.userContainer.visible()
            }


        }
    }


    private fun generateCardImage(onFileSave: (File) -> Unit) {
        try {
            viewBind.quoteOptions.gone()
            viewBind.userTop.userContainer.gone()
            val card = viewBind.quoteCard
            card.isDrawingCacheEnabled = true
            val bitmap = Bitmap.createBitmap(card.drawingCache)
            card.isDrawingCacheEnabled = false
            val cachePath = context.cacheDir.path + "/shared_quotes/"
            val cacheDir = File(cachePath)
            if (!cacheDir.exists()) cacheDir.mkdirs()
            val stream = FileOutputStream(cachePath + "quote_${quote.id}.png")
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.close()
            val file = File(cachePath + "quote_${quote.id}.png")
            Log.i(javaClass.simpleName, "generateCardImage: file saved ${file.absolutePath}")
            onFileSave.invoke(file)

        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(javaClass.simpleName, "generateCardImage: ${e.message}")
            showSnackBar(context, "Ocorreu um erro ao compartilhar a frase", ContextCompat.getColor(context, ColorUtils.ERROR), rootView = viewBind.quoteCard)
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
            viewBind.userTop.username.setTextColor(ContextCompat.getColor(context, R.color.md_white))

            CardLikeAdapter(data.likes.toList(), context)
            viewBind.quoteOptions.slideInBottom()
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