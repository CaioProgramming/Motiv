package com.creat.motiv.view.binders

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.creat.motiv.databinding.NewquotepopupBinding
import com.creat.motiv.model.beans.Quote
import com.creat.motiv.presenter.QuotePresenter
import com.creat.motiv.utils.Tools
import com.creat.motiv.utils.invisible
import com.creat.motiv.utils.toHex
import com.creat.motiv.utils.visible
import com.creat.motiv.view.BaseView
import com.creat.motiv.view.adapters.PickedColor
import com.creat.motiv.view.adapters.RecyclerColorAdapter
import com.creat.motiv.view.adapters.SelectedViewType
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
        if (quote == null) quote = emptyQuote()
        showData(quote!!)
    }

    override fun showData(data: Quote) {
        viewBind.qData = data
        viewBind.run {
            fontSelector.setOnClickListener {
                quote?.font = quote?.font!! + 1
                updateFont()
            }
            backColorpicker()
            textColorPicker()
            colorGallery()
            saveQuoteButton.setOnClickListener {
                salvar()
            }
        }
        UserViewBinder(data.userID, context, viewBind.userTop)
    }


    private fun NewquotepopupBinding.updateFont() {
        quoteTextView.typeface = Tools.fonts(context)[quote?.font ?: 0]
        authorTextView.typeface = Tools.fonts(context)[quote?.font ?: 0]
    }


    private fun NewquotepopupBinding.backColorpicker() {
        backcolorfab.setOnClickListener {
            if (context is Activity) {
                val cp = ColorPicker(context as Activity?)
                cp.show()
                cp.enableAutoClose()
                cp.setCallback { color ->
                    background.invisible()
                    background.setBackgroundColor(color)
                    val cx = background.right
                    val cy = background.top
                    val radius = background.width.coerceAtLeast(background.height)
                    val anim = ViewAnimationUtils.createCircularReveal(background, cx, cy,
                            0f, radius.toFloat())
                    background.visible()
                    anim.start()
                    quote?.backgroundcolor = toHex(color)
                    backcolorfab.backgroundTintList = ColorStateList.valueOf(color)
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
                Log.d("Pure Hex", Integer.toHexString(color))
                val colorFrom = quote?.textcolor
                val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, color)
                colorAnimation.duration = 2000 // milliseconds
                colorAnimation.addUpdateListener {
                    quoteTextView.setTextColor(color)
                    authorTextView.setTextColor(color)
                }
                textcolorfab.backgroundTintList = ColorStateList.valueOf(color)
                colorAnimation.start()
                quote?.textcolor = toHex(color)
            }
        }
    }

    private fun NewquotepopupBinding.colorGallery() {
        val colors = ArrayList<Int>()
        val fields = Class.forName(context.packageName + ".R\$color").declaredFields
        for (field in fields) {
            val colorName = field.name
            val colorId = field.getInt(null)
            val color = context.resources.getColor(colorId)
            println("color $colorName $color")
            colors.add(color)
        }

        println("Load " + colors.size + " colors")
        colors.sortedDescending()
        val llm = GridLayoutManager(context, 3, GridLayoutManager.HORIZONTAL, false)
        val recyclerColorAdapter = RecyclerColorAdapter(colors, context) {
            if (it.selectedView == SelectedViewType.BACKGROUND) {
                animateBackground(it)
            } else {
                animateText(it)
            }
        }
        colorlibrary.setHasFixedSize(true)
        colorlibrary.adapter = recyclerColorAdapter
        colorlibrary.layoutManager = llm

    }

    private fun NewquotepopupBinding.animateText(it: PickedColor) {
        val colorFrom = quote?.textcolor
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, it.color)
        colorAnimation.duration = 2000 // milliseconds
        val color: Int = Color.parseColor(it.color)

        colorAnimation.addUpdateListener { valueAnimator ->
            val color = Color.parseColor(quote?.textcolor)
            quoteTextView.setTextColor(color)
            authorTextView.setTextColor(color)
        }

        textcolorfab.backgroundTintList = ColorStateList.valueOf(color)
        colorAnimation.start()
        quote?.textcolor = it.color
        quoteTextView.setTextColor(color)
        authorTextView.setTextColor(color)
    }

    private fun NewquotepopupBinding.animateBackground(it: PickedColor) {
        background.visibility = View.INVISIBLE
        val color = Color.parseColor(it.color)
        background.setBackgroundColor(color)
        val cx = background.right
        val cy = background.top
        val radius = Math.max(background.width, background.height)
        val anim = ViewAnimationUtils.createCircularReveal(background, cx, cy, 15f, radius.toFloat())
        background.visibility = View.VISIBLE
        anim.start()
        background.setBackgroundColor(color)
        backcolorfab.imageTintList = ColorStateList.valueOf(color)
        quote?.backgroundcolor = it.color
    }


    private fun actualday(): Date {
        return Calendar.getInstance().time

    }

    fun salvar() {
        quote?.let {
            it.quote = viewBind.quoteTextView.text.toString()
            it.author = viewBind.authorTextView.text.toString()
            presenter().saveData(it)
        }
    }

    private fun emptyQuote(): Quote {
        val firebaseUser = presenter().currentUser()
        return Quote(
                data = actualday(),
                author = firebaseUser?.displayName ?: "",
                userID = firebaseUser?.displayName ?: ""
        )
    }

}