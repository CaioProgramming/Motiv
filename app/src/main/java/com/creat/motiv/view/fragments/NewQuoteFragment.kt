package com.creat.motiv.view.fragments

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.creat.motiv.R
import com.creat.motiv.adapters.RecyclerColorAdapter
import com.creat.motiv.databinding.NewquotepopupBinding
import com.creat.motiv.model.Beans.Quotes
import com.creat.motiv.model.QuotesDB
import com.creat.motiv.utils.Alert
import com.creat.motiv.utils.Tools
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.pes.androidmaterialcolorpickerdialog.ColorPicker
import java.text.SimpleDateFormat
import java.util.*

class NewQuoteFragment: Fragment() {
    private val popupbind:NewquotepopupBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.newquotepopup,null,false)
    var user:FirebaseUser? = null
    private var f: Int = 0
    private var isfirst = true
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        showup()
        return popupbind.root
    }


    fun showup() {
        val user = FirebaseAuth.getInstance().currentUser
        popupbind.username.text = user!!.displayName
        Glide.with(this).load(user.photoUrl).into( popupbind.userpic)
        try {
            colorgallery(popupbind)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        popupbind.backcolorfab!!.setOnClickListener { backColorpicker(popupbind) }

        popupbind.textcolorfab!!.setOnClickListener { textocolorpicker(popupbind) }

        popupbind.salvar!!.setOnClickListener {
            salvar(createQuote())
        }
        popupbind.font!!.setOnClickListener {
            if (!isfirst) {
                f++
            }
            val fonts = Tools.fonts(activity!!)

            if (f == fonts.size) {
                f = 0
            }
            popupbind!!.font.typeface = fonts[f]
            popupbind!!.quote!!.typeface = fonts[f]
            popupbind!!.author!!.typeface = fonts[f]
            isfirst = false
        }
        popupbind.toolbar.visibility = View.GONE


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
            val radius = editbind.background!!.width.coerceAtLeast(editbind.background.height)
            val anim = ViewAnimationUtils.createCircularReveal(editbind.background, cx, cy,
                    0f, radius.toFloat())
            editbind.background.visibility = View.VISIBLE
            anim.start()
            editbind.backcolorid!!.text = color.toString()
            editbind.backcolorfab!!.backgroundTintList = ColorStateList.valueOf(color)
        }
    }
    private fun textocolorpicker(editbind: NewquotepopupBinding) {
        val cp = ColorPicker(activity)
        cp.show()
        cp.enableAutoClose()
        cp.setCallback { color ->
            Log.d("Pure Hex", Integer.toHexString(color))
            val colorFrom = editbind.quote!!.currentTextColor
            val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, color)
            colorAnimation.duration = 2000 // milliseconds
            colorAnimation.addUpdateListener {
                editbind.quote.setTextColor(color)
                editbind.author.setTextColor(color)
            }
            editbind.textcolorfab!!.backgroundTintList = ColorStateList.valueOf(color)
            colorAnimation.start()
            editbind.texcolorid!!.text = color.toString()
        }
    }
    @Throws(ClassNotFoundException::class, IllegalAccessException::class)
    private fun colorgallery(editbind: NewquotepopupBinding) {
        val colors = ArrayList<Int>()
        val fields = Class.forName(Objects.requireNonNull<Activity>(activity).packageName + ".R\$color").declaredFields
        for (field in fields) {
            val colorName = field.name
            val colorId = field.getInt(null)
            val color = activity!!.resources.getColor(colorId)
            println("color $colorName $color")
            colors.add(color)
        }

        println("Load " + colors.size + " colors")
        colors.reverse()
        editbind.colorlibrary.setHasFixedSize(true)
        val llm = GridLayoutManager(activity, 3, GridLayoutManager.HORIZONTAL, false)
        val recyclerColorAdapter = RecyclerColorAdapter(colors, activity!!,
                editbind.background!!, editbind.quote!!, editbind.author!!,
                editbind.texcolorid!!, editbind.backcolorid!!,
                editbind.textcolorfab!!, editbind.backcolorfab!!)
        recyclerColorAdapter.notifyDataSetChanged()

        editbind.colorlibrary.adapter = recyclerColorAdapter
        editbind.colorlibrary.layoutManager = llm

    }
    private fun actualday(): String {
        val datenow = Calendar.getInstance().time
        @SuppressLint("SimpleDateFormat") val df = SimpleDateFormat("dd/MM/yyyy")
        val dia = df.format(datenow)
        println(dia)
        return dia
    }
    private fun salvar(quote: Quotes) {
        if (user?.isEmailVerified!!) {
            val quotesDB = QuotesDB(activity!!, quote)
            quotesDB.inserir()
        } else {
            Alert.builder(activity!!).mailmessage()
        }
    }

    private fun createQuote():Quotes{
        if ( popupbind.texcolorid!!.text.isEmpty()) {
            popupbind.texcolorid.text = Color.BLACK.toString()
        }
        if ( popupbind.backcolorid!!.text.isEmpty()) {
            popupbind.backcolorid.text = Color.WHITE.toString()
        }
        if( popupbind.author.text.isBlank()){
            popupbind.author.setText(user!!.displayName)

        }
        return Quotes("",
                popupbind.quote!!.text.toString(),
                popupbind.author!!.text.toString(),
                actualday(),
                user!!.uid, user!!.displayName!!, user!!.photoUrl.toString(),
                popupbind.backcolorid.text.toString().toIntOrNull()?: 0,
                popupbind.texcolorid.text.toString().toIntOrNull()?: 0,
                false, f)
    }

}