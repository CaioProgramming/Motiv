 package com.creat.motiv.view.binders

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Vibrator
import android.util.Log
import android.util.TypedValue
import android.view.MenuItem
import android.widget.PopupMenu
import android.widget.TextView
import com.creat.motiv.R
import com.creat.motiv.databinding.QuotescardBinding
import com.creat.motiv.model.QuoteModel
import com.creat.motiv.model.beans.Quote
import com.creat.motiv.presenter.QuotePresenter
import com.creat.motiv.utils.Alert
import com.creat.motiv.utils.ColorUtils
import com.creat.motiv.utils.Tools
import com.creat.motiv.view.BaseView
import com.devs.readmoreoption.ReadMoreOption
import java.text.SimpleDateFormat
import java.util.*

 class QuoteCardBinder(
         var quote: Quote,
         override val context: Context,
         override val viewBind: QuotescardBinding) : BaseView<Quote>() {


     override fun presenter(): QuotePresenter = QuotePresenter(this)

     init {
         initView()
     }

     override fun initView() {
         viewBind.run {
             Log.d(javaClass.simpleName, "initView: setup view")
             setupCard()
             setupLike()
             setupQuote()
             setupUser()
         }
     }


    private fun defineTextSize(length: Int, textView: TextView) {
        textView.textSize = when {
            length <= 40 -> {
                TypedValue.COMPLEX_UNIT_SP
                60f
            }
            length <= 99 -> {
                TypedValue.COMPLEX_UNIT_SP
                40f
            }
            length >= 100 -> {
                TypedValue.COMPLEX_UNIT_SP
                30f
            }
            length >= 200 -> {
                TypedValue.COMPLEX_UNIT_SP
                20f
            }
            else -> {
                TypedValue.COMPLEX_UNIT_SP
                10f
            }
        }
    }






    private fun QuotescardBinding.setupUser() {
        UserViewBinder(quote.userID, context, userTop)
    }

    private fun QuotescardBinding.setupQuote() {
        quoteData = quote
        quoteTextView.typeface = Tools.fonts(context)[quote.font]
        defineTextSize(quote.quote.length, quoteTextView)
        background.setCardBackgroundColor(quote.backgroundcolor)
        quoteTextView.text = quote.quote
        author.text = quote.author
        val color = ColorUtils.lighten(quote.textcolor, 0.3)
        val readMoreOption = ReadMoreOption.Builder(context)
                .textLength(120, ReadMoreOption.TYPE_CHARACTER)
                .moreLabel(" Ver mais...")
                .lessLabel(" Ver menos")
                .moreLabelColor(color)
                .lessLabelColor(color)
                .expandAnimation(true)
                .build()
        readMoreOption.addReadMoreTo(quoteTextView, quote.quote)
        quoteDate.text = data()
    }

    private fun QuotescardBinding.setupLike() {
        LikeCardBinder(quote.id, context, likeView)
    }

    private fun QuotescardBinding.setupCard() {

        background.setOnClickListener {
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (vibrator.hasVibrator()) {
                val mVibratePattern = longArrayOf(100, 150)
                vibrator.vibrate(mVibratePattern, -1) // for 500 ms
            }
            val popup = PopupMenu(context, background)
            popup.menuInflater.inflate(R.menu.quotemenu, popup.menu)
            popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                override fun onMenuItemClick(item: MenuItem?): Boolean {
                    item?.let {
                        when (it.itemId) {
                            R.id.quoteCopy -> {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("${quote.author}", quote.quote)
                                clipboard.setPrimaryClip(clip)
                                return true
                            }
                            R.id.quoteShare -> {
                                val shareintent = Intent(Intent.ACTION_SEND)
                                shareintent.type = "text/pain"
                                shareintent.putExtra(Intent.EXTRA_SUBJECT, "Motiv")
                                shareintent.putExtra(Intent.EXTRA_TEXT, quote.quote + " -" + quote.author)
                                context.startActivity(Intent.createChooser(shareintent, "Escolha onde quer compartilhar"))
                                return true
                            }
                            R.id.quoteReport -> {
                                val alert = Alert(context as Activity)
                                alert.showAlert(
                                        message = context.getString(R.string.report_message),
                                        buttonMessage = "denunciar",
                                        icon = R.drawable.flamencodeleteconfirmation,
                                        okClick = {
                                            QuoteModel(presenter()).denunciar(quote)
                                        }, cancelClick = null)
                                return true
                            }
                            else -> return false
                        }
                    }
                    return false

                }
            })
            popup.show()
            popup.setOnDismissListener {

            }
        }
    }


    fun data(): String {
        val postdia = Tools.convertDate(quote.data)
        val now = Calendar.getInstance().time
        //print("Date comparision ${now.compareTo(postdia)}")
        val fmt = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale.getDefault())
        val dayCount = ((now.time - postdia.time) / 1000 / 60 / 60 / 24).toInt()
        return when {
            dayCount < 1 -> {
                "Hoje"
            }
            dayCount == 1 -> {
                "Ontem"
            }
            else -> {
                fmt.format(postdia)
            }
        }
    }

    override fun onLoading() {
        TODO("Not yet implemented")
    }

    override fun onLoadFinish() {
        TODO("Not yet implemented")
    }


    override fun showData(data: Quote) {
        this.quote = data
        initView()
    }


}