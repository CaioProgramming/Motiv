package com.creat.motiv.view.binders

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.util.Log
import android.view.View
import android.view.ViewAnimationUtils
import androidx.recyclerview.widget.GridLayoutManager
import com.creat.motiv.databinding.NewquotepopupBinding
import com.creat.motiv.model.beans.Quote
import com.creat.motiv.presenter.QuotePresenter
import com.creat.motiv.utils.Tools
import com.creat.motiv.utils.invisible
import com.creat.motiv.utils.visible
import com.creat.motiv.view.BaseView
import com.creat.motiv.view.adapters.RecyclerColorAdapter
import com.creat.motiv.view.adapters.SelectedViewType
import com.pes.androidmaterialcolorpickerdialog.ColorPicker
import java.text.SimpleDateFormat
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
        TODO("Not yet implemented")
    }

    override fun onLoadFinish() {
        TODO("Not yet implemented")
    }

    override fun initView() {
        if (quote == null) quote = emptyQuote()
        showData(quote!!)
    }

    override fun showData(data: Quote) {
        viewBind.qData = data
        viewBind.run {
            updateFont(data)
            backColorpicker()
            textColorPicker()
            colorGallery()
            saveQuoteButton.setOnClickListener {
                salvar()
            }
        }
    }


    private fun NewquotepopupBinding.updateFont(quote: Quote) {
        quoteTextView.typeface = Tools.fonts(context)[quote.font]
        author.typeface = Tools.fonts(context)[quote.font]
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
                    quote?.backgroundcolor = color
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
                    author.setTextColor(color)
                }
                textcolorfab.backgroundTintList = ColorStateList.valueOf(color)
                colorAnimation.start()
                quote?.textcolor = color
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
        colorlibrary.setHasFixedSize(true)
        val llm = GridLayoutManager(context, 3, GridLayoutManager.HORIZONTAL, false)
        val recyclerColorAdapter = RecyclerColorAdapter(colors, context) {
            if (it.selectedView == SelectedViewType.BACKGROUND) {

                background.visibility = View.INVISIBLE
                background.setBackgroundColor(it.color)
                val cx = background.right
                val cy = background.top
                val radius = Math.max(background.width, background.height)
                val anim = ViewAnimationUtils.createCircularReveal(background, cx, cy, 15f, radius.toFloat())
                background.visibility = View.VISIBLE
                anim.start()
                background.setBackgroundColor(it.color)
                backcolorfab.imageTintList = ColorStateList.valueOf(it.color)
                quote?.backgroundcolor = it.color

            } else {

                val colorFrom = quote?.textcolor
                val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, it.color)
                colorAnimation.duration = 2000 // milliseconds
                colorAnimation.addUpdateListener { valueAnimator ->
                    quoteTextView.setTextColor(it.color)
                    author.setTextColor(it.color)
                }
                textcolorfab.backgroundTintList = ColorStateList.valueOf(it.color)
                colorAnimation.start()
                quote?.textcolor = it.color
                quoteTextView.setTextColor(it.color)
                author.setTextColor(it.color)
            }
        }
        recyclerColorAdapter.notifyDataSetChanged()
        colorlibrary.adapter = recyclerColorAdapter
        colorlibrary.layoutManager = llm

    }


    private fun actualday(): String {
        val datenow = Calendar.getInstance().time
        @SuppressLint("SimpleDateFormat") val df = SimpleDateFormat("dd/MM/yyyy")
        val dia = df.format(datenow)
        println(dia)
        return dia
    }

    fun salvar() {
        quote?.let {
            presenter().saveData(it)
        }
    }

    private fun emptyQuote(): Quote {
        val firebaseUser = presenter().currentUser()
        return Quote(
                data = actualday(),
                author = firebaseUser?.displayName ?: "",
                username = firebaseUser?.displayName ?: "",
                userID = firebaseUser?.displayName ?: "",
                userphoto = firebaseUser?.photoUrl?.path ?: ""
        )
    }

}