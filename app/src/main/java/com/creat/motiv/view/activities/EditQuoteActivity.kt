package com.creat.motiv.view.activities

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewAnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.creat.motiv.R
import com.creat.motiv.databinding.NewquotepopupBinding
import com.creat.motiv.model.Beans.Quotes
import com.creat.motiv.model.QuotesDB
import com.creat.motiv.utils.Alert
import com.creat.motiv.utils.Tools
import com.creat.motiv.view.adapters.RecyclerColorAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.pes.androidmaterialcolorpickerdialog.ColorPicker
import kotlinx.android.synthetic.main.newquotepopup.*
import java.text.SimpleDateFormat
import java.util.*

class EditQuoteActivity : AppCompatActivity() {

    var user: FirebaseUser? = null
    private var f: Int = 0
    private var isfirst = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       val popupbind:NewquotepopupBinding = DataBindingUtil.setContentView(this, R.layout.newquotepopup)
        setContentView(popupbind.root)
        showup()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.newquotemenu, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.salvar) {
            salvar(createQuote())
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadQuote() {

        val quotes = intent.getSerializableExtra("Quote") as? Quotes
        //quoteID.setText(quotes.getId());
        if (quotes != null) {
            quote.setText(quotes.quote)
            author.setText(quotes.author)
            username.text = quotes.username
            background!!.setBackgroundColor(quotes.backgroundcolor!!)

            quote.setTextColor(quotes.textcolor!!)
            if (quotes.font != null) {
                quote.typeface = Tools.fonts(this)[quotes.font!!]
                quote.typeface = Tools.fonts(this)[quotes.font!!]
                font.typeface = Tools.fonts(this)[quotes.font!!]
                f = quotes.font!!
            } else {
                quote.typeface = Typeface.DEFAULT
                author.typeface = Typeface.DEFAULT

            }
            author.setTextColor(quotes.textcolor!!)
            textcolorid!!.text = quotes.textcolor.toString()
            backcolorid!!.text = quotes.backgroundcolor.toString()
            backcolorfab!!.backgroundTintList = ColorStateList.valueOf(quotes.backgroundcolor!!)
            textcolorfab!!.backgroundTintList = ColorStateList.valueOf(quotes.textcolor!!)

            fontid!!.text = quotes.font.toString()
        }
        Glide.with(this).load(user!!.photoUrl).error(R.drawable.notfound).into(userpic)

    }


    fun showup() {
        val user = FirebaseAuth.getInstance().currentUser
        username.text = user!!.displayName
        Glide.with(this).load(user.photoUrl).into(userpic)
        try {
            colorgallery()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        backcolorfab.setOnClickListener { backColorpicker() }

        textcolorfab.setOnClickListener { textocolorpicker() }

        font.setOnClickListener {
            if (!isfirst) {
                f++
            }
            val fonts = Tools.fonts(this)

            if (f == fonts.size) {
                f = 0
            }
            font.typeface = fonts[f]
            quote.typeface = fonts[f]
            author.typeface = fonts[f]
            isfirst = false
        }
        loadQuote()

    }

    private fun backColorpicker() {
        val cp = ColorPicker(this)
        cp.show()
        cp.enableAutoClose()
        cp.setCallback { color ->
            background.visibility = View.INVISIBLE
            background.setBackgroundColor(color)
            val cx = background.right
            val cy = background.top
            val radius = background.width.coerceAtLeast(background.height)
            val anim = ViewAnimationUtils.createCircularReveal(background, cx, cy,
                    0f, radius.toFloat())
            background.visibility = View.VISIBLE
            anim.start()
            backcolorid.text = color.toString()
            backcolorfab.backgroundTintList = ColorStateList.valueOf(color)
        }
    }

    private fun textocolorpicker() {
        val cp = ColorPicker(this)
        cp.show()
        cp.enableAutoClose()
        cp.setCallback { color ->
            Log.d("Pure Hex", Integer.toHexString(color))
            val colorFrom = quote.currentTextColor
            val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, color)
            colorAnimation.duration = 2000 // milliseconds
            colorAnimation.addUpdateListener {
               quote.setTextColor(color)
               author.setTextColor(color)
            }
            textcolorfab.backgroundTintList = ColorStateList.valueOf(color)
            colorAnimation.start()
            textcolorid.text = color.toString()
        }
    }

    @Throws(ClassNotFoundException::class, IllegalAccessException::class)
    private fun colorgallery() {
        val colors = ArrayList<Int>()
        val fields = Class.forName(Objects.requireNonNull<Activity>(this).packageName + ".R\$color").declaredFields
        for (field in fields) {
            val colorName = field.name
            val colorId = field.getInt(null)
            val color = resources.getColor(colorId)
            println("color $colorName $color")
            colors.add(color)
        }

        println("Load " + colors.size + " colors")
        colors.reverse()
        colorlibrary.setHasFixedSize(true)
        val llm = GridLayoutManager(this, 3, GridLayoutManager.HORIZONTAL, false)
        val recyclerColorAdapter = RecyclerColorAdapter(colors, this,
                background, quote, author,
                textcolorid, backcolorid,
                textcolorfab, backcolorfab)
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

    private fun salvar(quote: Quotes) {
        if (user?.isEmailVerified!!) {
            val quotesDB = QuotesDB(this, quote)
            quotesDB.inserir()
        } else {
            Alert.builder(this).mailmessage()
        }
    }

    private fun createQuote(): Quotes {
        if (textcolorid.text.isEmpty()) {
            textcolorid.text = Color.BLACK.toString()
        }
        if (backcolorid.text.isEmpty()) {
            backcolorid.text = Color.WHITE.toString()
        }
        if (author.text.isBlank()) {
            author.setText(user!!.displayName)

        }
        return Quotes("",
                quote.text.toString(),
                author.text.toString(),
                actualday(),
                user!!.uid, user!!.displayName!!, user!!.photoUrl.toString(),
                backcolorid.text.toString().toIntOrNull() ?: 0,
                textcolorid.text.toString().toIntOrNull() ?: 0,
                false, f)
    }
}
