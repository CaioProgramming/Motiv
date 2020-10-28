package com.creat.motiv.view.binders

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import com.creat.motiv.R
import com.creat.motiv.databinding.NewquotepopupBinding
import com.creat.motiv.model.beans.Quote
import com.creat.motiv.presenter.QuotePresenter
import com.creat.motiv.utilities.*
import com.creat.motiv.view.BaseView
import com.creat.motiv.view.adapters.PickedColor
import com.creat.motiv.view.adapters.RecyclerColorAdapter
import com.pes.androidmaterialcolorpickerdialog.ColorPicker
import java.util.*

class EditQuoteBinder(
        var quote: Quote? = null,
        override val context: Context,
        override val viewBind: NewquotepopupBinding) : BaseView<Quote>() {

    override fun presenter() = QuotePresenter(this)


    init {
        initView()
    }


    override fun onLoading() {
        Toast.makeText(context, "Salvando frase...", Toast.LENGTH_LONG).show()
    }

    override fun onLoadFinish() {

    }

    override fun initView() {
        if (quote == null) {
            quote = emptyQuote()
        }
        showData(quote!!)
    }

    override fun showData(data: Quote) {
        getcolorGallery()
        viewBind.run {
            quoteTextView.setTextColor(data.intTextColor())
            authorTextView.setTextColor(data.intTextColor())
            quoteTextView.setText(data.quote)
            authorTextView.setText(data.author)
            textcolorfab.imageTintList = ColorStateList.valueOf(data.intTextColor())
            fontSelector.setTextColor(data.intTextColor())
            background.setCardBackgroundColor(data.intBackColor())
            backcolorfab.imageTintList = ColorStateList.valueOf(ColorUtils.darken(data.intBackColor(), 0.8))
            fontSelector.setOnClickListener {
                quote?.font = quote?.font!! + 1
                updateFont()
            }
            backColorpicker()
            textColorPicker()
            saveQuoteButton.setOnClickListener {
                salvar()
            }
            quoteTextView.addTextChangedListener {
                quoteTextView.textSize = textSize(quote!!.quote.length, context)
            }
        }
        UserViewBinder(data.userID, context, viewBind.userTop)
    }


    private fun NewquotepopupBinding.updateFont() {
        quoteTextView.typeface = Tools.fonts(context)[quote?.font ?: 0]
        authorTextView.typeface = Tools.fonts(context)[quote?.font ?: 0]
        quoteTextView.fadeIn().subscribe()
        authorTextView.fadeIn().subscribe()
    }


    private fun NewquotepopupBinding.backColorpicker() {
        backcolorfab.setOnClickListener {
            if (context is Activity) {
                val cp = ColorPicker(context as Activity?)
                cp.show()
                cp.enableAutoClose()
                cp.setCallback { color ->
                    quote?.backgroundcolor = toHex(color)
                    animateBackground(color)
                }
            }
        }
    }

    private fun NewquotepopupBinding.textColorPicker() {
        textcolorfab.setOnClickListener {
            val cp = ColorPicker(context as Activity?)
            cp.show()
            cp.enableAutoClose()
            cp.setCallback { color ->
                quote?.textcolor = toHex(color)
                viewBind.animateText(color)

            }
        }
    }


    private fun getcolorGallery() {
        val colors = ArrayList<Int>()
        val fields = Class.forName("com.github.mcginty" + ".R\$color").declaredFields
        for (field in fields) {
            if (field.getInt(null) != R.color.transparent) {
                val colorId = field.getInt(null)
                val color = context.resources.getColor(colorId)
                colors.add(color)
            }
        }
        println("Load " + colors.size + " colors")
        val llm = GridLayoutManager(context, 3, GridLayoutManager.HORIZONTAL, false)
        val recyclerColorAdapter = RecyclerColorAdapter(colors, context, ::getSelectedColor)
        viewBind.colorlibrary.layoutManager = llm
        viewBind.colorlibrary.adapter = recyclerColorAdapter
        viewBind.colorlibrary.setHasFixedSize(true)

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
        textcolorfab.imageTintList = ColorStateList.valueOf(color)
        quoteTextView.fadeIn().subscribe()
        authorTextView.fadeIn().subscribe()
    }

    private fun NewquotepopupBinding.animateBackground(color: Int) {
        background.setCardBackgroundColor(color)
        val anim = AnimationUtils.loadAnimation(context, R.anim.bounce)
        background.startAnimation(anim)
        backcolorfab.imageTintList = ColorStateList.valueOf(color)
    }


    private fun actualday(): Date {
        return Calendar.getInstance().time
    }

    fun salvar() {
        quote?.let {
            it.quote = viewBind.quoteTextView.text.toString()
            it.author = viewBind.authorTextView.text.toString()
            presenter().saveData(it, it.id)
        }
    }

    private fun emptyQuote(): Quote {
        val firebaseUser = presenter().currentUser()
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