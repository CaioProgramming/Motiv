package com.creat.motiv.view.binders

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Handler
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.creat.motiv.FontUtils
import com.creat.motiv.R
import com.creat.motiv.databinding.NewquotepopupBinding
import com.creat.motiv.model.beans.Quote
import com.creat.motiv.presenter.QuotePresenter
import com.creat.motiv.utilities.*
import com.creat.motiv.view.adapters.FontAdapter
import com.creat.motiv.view.adapters.PickedColor
import com.creat.motiv.view.adapters.RecyclerColorAdapter
import com.ilustriscore.core.base.BaseView
import com.ilustriscore.core.base.DTOMessage
import com.ilustriscore.core.utilities.*
import com.skydoves.balloon.ArrowOrientation.TOP
import com.skydoves.balloon.BalloonAnimation.ELASTIC
import com.skydoves.balloon.createBalloon
import com.skydoves.balloon.showAlignBottom
import java.util.*

class EditQuoteBinder(
        var quote: Quote? = null,
        override val context: Context,
        override val viewBind: NewquotepopupBinding) : BaseView<Quote>() {

    override fun presenter() = QuotePresenter(this)
    lateinit var fontAdapter: FontAdapter

    init {
        initView()
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
        val balloon = createBalloon(context) {
            setArrowSize(10)
            setWidthRatio(0.5f)
            setHeight(50)
            setCornerRadius(10f)
            setArrowOrientation(TOP)
            setAutoDismissDuration(10000)
            setText("Deslize para baixo ver as outras fontes")
            setTextColorResource(R.color.white)
            setBackgroundColorResource(R.color.colorPrimary)
            setBalloonAnimation(ELASTIC)
            setLifecycleOwner(lifecycleOwner)
        }
        viewBind.fontSelector.showAlignBottom(balloon)
        showData(quote!!)
    }

    override fun showData(data: Quote) {
        getcolorGallery()
        getFonts()
        viewBind.run {
            quoteTextView.setTextColor(data.intTextColor())
            authorTextView.setTextColor(data.intTextColor())
            quoteCard.setCardBackgroundColor(data.intBackColor())
            quoteTextView.setText(data.quote)
            authorTextView.setText(data.author)
            updateFont(data.font)
            saveQuoteButton.setOnClickListener {
                salvar()
            }
            quoteTextView.addTextChangedListener {
                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O) {
                    quoteTextView.autoSizeText()
                }
            }
        }
    }


    private fun updateFont(newPosition: Int) {
        quote?.let {
            it.font = newPosition
            val typeface = TextUtils.getTypeFace(context, FontUtils.fonts()[it.font].path)
            viewBind.fontSelector.currentItem = it.font
            viewBind.quoteTextView.typeface = typeface
            viewBind.authorTextView.typeface = typeface
            viewBind.authorTextView.bounce()
            viewBind.authorTextView.bounce()
        }
    }

    private fun getcolorGallery() {
        val colors = ArrayList<Int>()
        val fields = Class.forName("com.github.mcginty" + ".R\$color").declaredFields
        for (field in fields) {
            if (field.getInt(null) != Color.TRANSPARENT) {
                val colorId = field.getInt(null)
                val color = context.resources.getColor(colorId)
                colors.add(color)
            }
        }
        println("Load " + colors.size + " colors")
        val llm = GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
        val recyclerColorAdapter = RecyclerColorAdapter(colors, context, ::getSelectedColor)
        viewBind.colorlibrary.layoutManager = llm
        viewBind.colorlibrary.adapter = recyclerColorAdapter
        viewBind.colorlibrary.setHasFixedSize(true)


    }

    private fun getFonts() {
        viewBind.fontSelector.apply {
            fontAdapter = FontAdapter(context, quote!!.intTextColor())
            adapter = fontAdapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    updateFont(position)
                }
            })
        }
    }


    private fun getSelectedColor(pickedColor: PickedColor) {
        when (pickedColor.selectedView) {
            SelectedViewType.BACKGROUND -> {
                quote?.backgroundcolor = pickedColor.color
                viewBind.animateBackground(Color.parseColor(pickedColor.color))
            }
            SelectedViewType.TEXT -> {
                quote?.textcolor = pickedColor.color
                viewBind.animateText(Color.parseColor(pickedColor.color))
            }
        }
    }

    private fun NewquotepopupBinding.animateText(color: Int) {
        quoteTextView.setTextColor(color)
        authorTextView.setTextColor(color)
        quoteTextView.setHintTextColor(ColorUtils.lighten(color, 0.5))
        authorTextView.setHintTextColor(ColorUtils.lighten(color, 0.5))
        fontAdapter.updateTextColor(color)
        quoteTextView.bounce()
        authorTextView.bounce()
    }

    private fun NewquotepopupBinding.animateBackground(color: Int) {
        quoteCard.setCardBackgroundColor(color)
        quoteCard.bounce()
    }


    private fun actualday(): Date {
        return Calendar.getInstance().time
    }

    override fun getCallBack(dtoMessage: DTOMessage) {
        super.getCallBack(dtoMessage)
        if (dtoMessage.operationType == OperationType.DATA_SAVED) {
            snackmessage(context, message = "Citação salva com sucesso!", parentContainer = R.id.mainContainer)
            quote = emptyQuote()
            quote?.let { showData(it) }
        } else if (dtoMessage.operationType == OperationType.DATA_UPDATED) {
            snackmessage(context, message = "Citação atualizada com sucesso!", parentContainer = R.id.mainContainer)
            if (context is Activity) {
                context.finish()
            }
        }
    }

    fun salvar() {
        quote?.let {
            it.quote = viewBind.quoteTextView.text.toString()
            it.author = viewBind.authorTextView.text.toString()
            if (it.quote.isNotEmpty()) presenter().saveData(it, it.id) else snackmessage(context, message = Tools.emptyquote(), parentContainer = R.id.mainContainer)
        }
    }

    private fun emptyQuote(): Quote {
        val firebaseUser = presenter().currentUser
        val backcolor = if (Tools.darkMode(context as Activity)) toHex(Color.BLACK) else toHex(Color.WHITE)
        val textcolor = if (!Tools.darkMode(context)) toHex(Color.BLACK) else toHex(Color.WHITE)
        return Quote(
                data = actualday(),
                backgroundcolor = backcolor,
                textcolor = textcolor,
                author = firebaseUser?.displayName ?: "",
                userID = firebaseUser?.uid ?: ""
        )
    }

}