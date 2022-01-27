package com.ilustris.motiv.manager.ui.home

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ilustris.animations.bounce
import com.ilustris.animations.fadeIn
import com.ilustris.motiv.base.beans.Style
import com.ilustris.motiv.base.beans.User
import com.ilustris.motiv.base.beans.quote.Quote
import com.ilustris.motiv.base.beans.quote.QuoteAdapterData
import com.ilustris.motiv.base.databinding.QuotesCardBinding
import com.ilustris.motiv.base.utils.*
import com.ilustris.motiv.manager.R
import com.silent.ilustriscore.core.utilities.gone
import com.silent.ilustriscore.core.utilities.showSnackBar
import com.silent.ilustriscore.core.utilities.visible
import java.util.*


enum class QuoteAction {
    OPTIONS, LIKE, USER
}

class QuoteManagerAdapter(
    private val quoteAdapterList: java.util.ArrayList<QuoteAdapterData>,
    private val onSelectQuote: (QuoteAdapterData, QuoteAction) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    fun refreshData(quoteAdapterData: QuoteAdapterData) {
        quoteAdapterList.add(quoteAdapterData)
        notifyItemInserted(itemCount)
    }

    fun loadOnNextPage(quoteAdapterData: QuoteAdapterData, position: Int) {
        if (position <= itemCount) {
            val nextItem = quoteAdapterList[position + 1]
            if (nextItem.quote.id != quoteAdapterData.quote.id) {
                quoteAdapterList.add(position + 1, quoteAdapterData)
                notifyItemInserted(position + 1)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val context = parent.context
        return QuoteViewHolder(
            LayoutInflater.from(context).inflate(R.layout.quotes_card, parent, false)
        )
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is QuoteViewHolder -> holder.bind()

        }
    }

    override fun getItemCount(): Int = quoteAdapterList.size
    fun clearAdapter() {
        quoteAdapterList.clear()
        notifyDataSetChanged()
    }

    inner class QuoteViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind() {
            QuotesCardBinding.bind(view).run {
                setupData()

            }
        }

        private fun QuotesCardBinding.setupUser(user: User) {
            userpic.loadImage(user.picurl)
            username.text = user.name
        }

        private fun QuotesCardBinding.setupStyle(style: Style) {
            val context = root.context
            quoteBack.loadGif(style.backgroundURL) {
                quoteBack.fadeIn()
            }
            quoteTextView.defineTextAlignment(style.textAlignment)
            authorTextView.defineTextAlignment(style.textAlignment)
            quoteTextView.setTextColor(Color.parseColor(style.textColor))
            authorTextView.setTextColor(Color.parseColor(style.textColor))
            quoteTextView.setShadowLayer(
                style.shadowStyle.radius,
                style.shadowStyle.dx,
                style.shadowStyle.dy,
                Color.parseColor(style.shadowStyle.shadowColor)
            )
            authorTextView.setShadowLayer(
                style.shadowStyle.radius,
                style.shadowStyle.dx,
                style.shadowStyle.dy,
                Color.parseColor(style.shadowStyle.shadowColor)
            )

            FontUtils.getTypeFace(context, style.font)?.let {
                quoteTextView.setTypeface(it, style.fontStyle.getTypefaceStyle())
                authorTextView.setTypeface(it, style.fontStyle.getTypefaceStyle())
            }
            quoteTextView.bounce()
            authorTextView.bounce()
        }

        private fun QuotesCardBinding.setupData() {
            val context = root.context
            val quoteData = quoteAdapterList[bindingAdapterPosition]
            quoteTextView.text = quoteData.quote.quote
            authorTextView.text = quoteData.quote.author
            quoteTextView.setOnLongClickListener {
                copyToClipboard(context, quoteData.quote)
                false
            }

            authorTextView.setOnLongClickListener {
                copyToClipboard(context, quoteData.quote)
                false
            }
            like.isChecked = quoteData.quote.likes.contains(quoteData.currentUser?.uid)
            optionsButton.setOnClickListener {
                onSelectQuote(quoteData, QuoteAction.OPTIONS)
            }
            like.isEnabled = false
            setupStyle(quoteData.style)
            setupUser(quoteData.user)
            quoteDate.text = TextUtils.data(quoteData.quote.data)
            userTop.setOnClickListener {
                onSelectQuote(quoteData, QuoteAction.USER)
            }
            if (!quoteData.quote.isUserQuote()) {
                userTop.gone()
            } else {
                userTop.visible()
            }
            if (quoteData.quote.isReport) {
                cardShadow.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(context, R.color.material_red800))
            }
        }

        private fun QuotesCardBinding.copyToClipboard(
            context: Context,
            quote: Quote
        ) {
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (vibrator.hasVibrator()) {
                val mVibratePattern = longArrayOf(60, 60)
                vibrator.vibrate(mVibratePattern, -1)
            }
            val clipboard: ClipboardManager? =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
            val clip = ClipData.newPlainText(
                "Motiv",
                "${quote.quote}\uD83E\uDE90\n - ${quote.author} \n\n#${
                    context.getString(
                        R.string.app_name
                    ).lowercase(Locale.getDefault())
                } #${quote.author.replace(" ", "").lowercase(Locale.getDefault())}"
            )
            clipboard?.setPrimaryClip(clip)
            root.showSnackBar(
                "Copiado para área de transferência",
                backColor = ContextCompat.getColor(context, R.color.colorAccent)
            )
        }
    }


}