package com.creat.motiv.presenter

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import android.view.View
import android.view.ViewAnimationUtils
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.creat.motiv.R
import com.creat.motiv.databinding.NewquotepopupBinding
import com.creat.motiv.model.Beans.Gradient
import com.creat.motiv.model.Beans.Quote
import com.creat.motiv.model.QuoteModel
import com.creat.motiv.utils.Alert
import com.creat.motiv.utils.ColorUtils
import com.creat.motiv.utils.Tools
import com.creat.motiv.view.adapters.RecyclerColorAdapter
import com.google.firebase.auth.FirebaseUser
import com.pes.androidmaterialcolorpickerdialog.ColorPicker
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class QuoteFormPresenter(val activity: Activity, val popupbind: NewquotepopupBinding, val user: FirebaseUser?) {
    private var f: Int = 0
    private var isfirst = true
    private var quote: Quote? = null

    fun setquote(quote: Quote) {
        showup()
        this.quote = quote
        loadQuote(quote)
    }


    private fun loadQuote(quote: Quote) {

        //quoteID.setText(quotes.getId());
        popupbind.quote.setText(quote.phrase)
        popupbind.author.setText(quote.author)
        popupbind.username.text = quote.username
        popupbind.background.setCardBackgroundColor(quote.backgroundcolor)
        popupbind.quote.setTextColor(quote.textcolor)
        if (quote.font != null) {
            popupbind.quote.typeface = Tools.fonts(activity)[quote.font]
            popupbind.author.typeface = Tools.fonts(activity)[quote.font]
            popupbind.font.typeface = Tools.fonts(activity)[quote.font]
            f = quote.font
        } else {
            popupbind.quote.typeface = Typeface.DEFAULT
            popupbind.author.typeface = Typeface.DEFAULT
        }
        popupbind.author.setTextColor(quote.textcolor)
        popupbind.textcolorid.text = quote.textcolor.toString()
        popupbind.backcolorid.text = quote.backgroundcolor.toString()
        popupbind.backcolorfab.backgroundTintList = ColorStateList.valueOf(quote.backgroundcolor)
        popupbind.textcolorfab.backgroundTintList = ColorStateList.valueOf(quote.textcolor)
        popupbind.fontid.text = quote.font.toString()
        Glide.with(activity).load(user!!.photoUrl).error(R.drawable.notfound).into(popupbind.userpic)

    }


    fun showup() {
        popupbind.username.text = user!!.displayName
        Glide.with(activity).load(user.photoUrl).into(popupbind.userpic)
        try {
            colorgallery(popupbind)
            gradientgallery()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        popupbind.backcolorfab.setOnClickListener { backColorpicker(popupbind) }

        popupbind.textcolorfab.setOnClickListener { textocolorpicker(popupbind) }

        popupbind.font.setOnClickListener {
            if (!isfirst) {
                f++
            }
            val fonts = Tools.fonts(activity)

            if (f == fonts.size) {
                f = 0
            }
            popupbind.font.typeface = fonts[f]
            popupbind.quote.typeface = fonts[f]
            popupbind.author.typeface = fonts[f]
            isfirst = false
        }
    }

    private fun backColorpicker(editbind: NewquotepopupBinding) {
        val cp = ColorPicker(activity)
        cp.show()
        cp.enableAutoClose()
        cp.setCallback { color ->
            editbind.background.visibility = View.INVISIBLE
            editbind.background.setBackgroundColor(color)
            val cx = editbind.background.right
            val cy = editbind.background.top
            val radius = editbind.background.width.coerceAtLeast(editbind.background.height)
            val anim = ViewAnimationUtils.createCircularReveal(editbind.background, cx, cy,
                    0f, radius.toFloat())
            editbind.background.visibility = View.VISIBLE
            anim.start()
            editbind.backcolorid.text = color.toString()
            editbind.backcolorfab.backgroundTintList = ColorStateList.valueOf(color)
        }
    }

    private fun textocolorpicker(editbind: NewquotepopupBinding) {
        val cp = ColorPicker(activity)
        cp.show()
        cp.enableAutoClose()
        cp.setCallback { color ->
            Log.d("Pure Hex", Integer.toHexString(color))
            val colorFrom = editbind.quote.currentTextColor
            val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, color)
            colorAnimation.duration = 2000 // milliseconds
            colorAnimation.addUpdateListener {
                editbind.quote.setTextColor(color)
                editbind.author.setTextColor(color)
            }
            editbind.textcolorfab.backgroundTintList = ColorStateList.valueOf(color)
            colorAnimation.start()
            editbind.textcolorid.text = color.toString()
        }
    }

    @Throws(ClassNotFoundException::class, IllegalAccessException::class)
    private fun colorgallery(editbind: NewquotepopupBinding) {
        val colors = ArrayList<Int>()
        val fields = Class.forName(Objects.requireNonNull<Activity>(activity).packageName + ".R\$color").declaredFields
        for (field in fields) {
            val colorName = field.name
            val colorId = field.getInt(null)
            val color = activity.resources.getColor(colorId)
            println("color $colorName $color")
            colors.add(color)
        }

        println("Load " + colors.size + " colors")
        colors.sortedDescending()
        editbind.colorlibrary.setHasFixedSize(true)
        val llm = GridLayoutManager(activity, 3, GridLayoutManager.HORIZONTAL, false)
        val recyclerColorAdapter = RecyclerColorAdapter(colors, activity,
                editbind.background, editbind.quote, editbind.author,
                editbind.textcolorid, editbind.backcolorid,
                editbind.textcolorfab, editbind.backcolorfab)
        recyclerColorAdapter.notifyDataSetChanged()

        editbind.colorlibrary.adapter = recyclerColorAdapter
        editbind.colorlibrary.layoutManager = llm

    }


    private fun gradientgallery() {
        val gradientList: ArrayList<Gradient> = ArrayList()
        for (i in 0..25) {
            gradientList.add(Gradient(ColorUtils.randomColor, ColorUtils.randomColor))
        }
        //popupbind!!.gradientlibrary.adapter = RecyclerGradientAdapter(gradientList, activity!!, popupbind!!.gradientview)
        popupbind.gradientlibrary.layoutManager = GridLayoutManager(activity, 2, GridLayoutManager.HORIZONTAL, false)


    }

    private fun actualday(): String {
        val datenow = Calendar.getInstance().time
        @SuppressLint("SimpleDateFormat") val df = SimpleDateFormat("dd/MM/yyyy")
        val dia = df.format(datenow)
        println(dia)
        return dia
    }

    fun salvar() {
        if (user?.isEmailVerified!!) {
            if (quote == null) {
                QuoteModel(activity, createQuote()).inserir()
            } else {
                quote = updateQuote()
                QuoteModel(activity, quote!!).editar()
            }
        } else {
            Alert.builder(activity).mailmessage()
        }
    }


    private fun createQuote(): Quote {
        if (popupbind.textcolorid.text.isEmpty()) {
            popupbind.textcolorid.text = Color.BLACK.toString()
        }
        if (popupbind.backcolorid.text.isEmpty()) {
            popupbind.backcolorid.text = Color.WHITE.toString()
        }
        if (popupbind.author.text.isBlank()) {
            popupbind.author.setText(user!!.displayName)

        }
        return Quote("",
                popupbind.quote.text.toString(),
                popupbind.author.text.toString(),
                actualday(),
                user!!.uid, user.displayName!!, user.photoUrl.toString(),
                popupbind.backcolorid.text.toString().toIntOrNull() ?: 0,
                popupbind.textcolorid.text.toString().toIntOrNull() ?: 0,
                false, f)
    }

    private fun updateQuote(): Quote {
        if (popupbind.textcolorid.text.isEmpty()) {
            popupbind.textcolorid.text = Color.BLACK.toString()
        }
        if (popupbind.backcolorid.text.isEmpty()) {
            popupbind.backcolorid.text = Color.WHITE.toString()
        }
        if (popupbind.author.text.isBlank()) {
            popupbind.author.setText(user!!.displayName)

        }
        return Quote(quote!!.id,
                popupbind.quote.text.toString(),
                popupbind.author.text.toString(),
                actualday(),
                user!!.uid, user.displayName!!, user.photoUrl.toString(),
                popupbind.backcolorid.text.toString().toIntOrNull() ?: 0,
                popupbind.textcolorid.text.toString().toIntOrNull() ?: 0,
                false, f)
    }

}
