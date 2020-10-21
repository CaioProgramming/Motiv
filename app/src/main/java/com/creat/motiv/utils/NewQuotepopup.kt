package com.creat.motiv.utils

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.creat.motiv.R
import com.creat.motiv.databinding.NewquotepopupBinding
import com.creat.motiv.model.Beans.Quote
import com.creat.motiv.model.QuoteModel
import com.creat.motiv.view.adapters.RecyclerColorAdapter
import com.github.mmin18.widget.RealtimeBlurView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.pes.androidmaterialcolorpickerdialog.ColorPicker
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.*

class NewQuotepopup(private val activity: Activity): DialogInterface.OnShowListener, DialogInterface.OnDismissListener {
    private val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private var f: Int = 0
    private var isfirst = true
    private var blur: RealtimeBlurView = activity.findViewById(R.id.rootblur)
    private var tuto = true

    fun showup() {

        val newquotebind = NewquotepopupBinding.inflate(LayoutInflater.from(activity),null,false)
        val myDialog = dialog(newquotebind)
        newquotebind.username.text = user!!.displayName
        Glide.with(activity).load(user.photoUrl).into( newquotebind.userpic)
        try {
            colorgallery(newquotebind)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

        newquotebind.backcolorfab.setOnClickListener { backColorpicker(newquotebind) }

        newquotebind.textcolorfab.setOnClickListener { textocolorpicker(newquotebind) }

        newquotebind.font.setOnClickListener {
            if (!isfirst) {
                f++
            }
            val fonts = Tools.fonts(activity)

            if (f == fonts.size) {
                f = 0
            }
            newquotebind.font.typeface = fonts[f]
            newquotebind.quote.typeface = fonts[f]
            newquotebind.author.typeface = fonts[f]
            isfirst = false
        }
        myDialog.show()


    }

    fun dialog(editbind: NewquotepopupBinding):Dialog{
        val myDialog = Dialog(activity,R.style.AppTheme)
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        myDialog.window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        myDialog.setCanceledOnTouchOutside(true)
        myDialog.setOnShowListener(this)
        myDialog.setOnDismissListener(this)
        myDialog.setContentView(editbind.root)
        return myDialog
    }


    fun showedit(quote: Quote) {
        val editbind = NewquotepopupBinding.inflate(LayoutInflater.from(activity), null, false)

        val myDialog = dialog(editbind)

        editbind.username.text = user!!.displayName
        Glide.with(activity).load(user.photoUrl).into(editbind.userpic)

        //theme();
        try {
            colorgallery(editbind)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

         editbind.backcolorfab.setOnClickListener { backColorpicker(editbind) }

         editbind.textcolorfab.setOnClickListener { textocolorpicker(editbind) }
        loadQuote(editbind.username, editbind.userpic, quote,editbind)




        tutorial()


         editbind.font.setOnClickListener {
            if (!isfirst) {
                f++
            }
            val fonts = Tools.fonts(activity)

            if (f == fonts.size) {
                f = 0
            }
            editbind.font.typeface = fonts[f]
             editbind.quote.typeface = fonts[f]
             editbind.author.typeface = fonts[f]
            isfirst = false
        }

        myDialog.show()


         myDialog.show()

    }


    private fun atualizar(quote: Quote) {

        if (quote.author.isBlank()) {
            quote.author = user!!.displayName!!
        }


        val quotesDB = QuoteModel(activity, quote)

        quotesDB.editar()
        val refreshLayout = activity.findViewById<SwipeRefreshLayout>(R.id.refresh)
        refreshLayout.isRefreshing = true


    }


    private fun loadQuote(username: TextView, userpic: CircleImageView, quote: Quote, editbind: NewquotepopupBinding) {
        //quoteID.setText(quotes.getId());
        editbind.quote.setText(quote.phrase)
        editbind.author.setText(quote.author)
        username.text = quote.username
        editbind.background.setBackgroundColor(quote.backgroundcolor)

        editbind.quote.setTextColor(quote.textcolor)
        if (quote.font != null) {
            editbind.quote.typeface = Tools.fonts(activity)[quote.font]
            editbind.quote.typeface = Tools.fonts(activity)[quote.font]
            editbind.font.typeface = Tools.fonts(activity)[quote.font]
            f = quote.font
        } else {
            editbind.quote.typeface = Typeface.DEFAULT
            editbind.author.typeface = Typeface.DEFAULT

        }
        editbind.author.setTextColor(quote.textcolor)
        editbind.textcolorid.text = quote.textcolor.toString()
        editbind.backcolorid.text = quote.backgroundcolor.toString()
        editbind.backcolorfab.backgroundTintList = ColorStateList.valueOf(quote.backgroundcolor)
        editbind.textcolorfab.backgroundTintList = ColorStateList.valueOf(quote.textcolor)

        editbind.fontid.text = quote.font.toString()
        Glide.with(activity).load(quote.userphoto).into(userpic)

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


    private fun tutorial() {
        val novo = Objects.requireNonNull<Bundle>(Objects.requireNonNull<Activity>(activity).intent.extras).getBoolean("novo")
        if (novo) {
            val preferences = Pref(Objects.requireNonNull<Activity>(activity))
            if (!preferences.writetutorialstate()) {
                preferences.setWriteTutorial(true)
                val a = Alert(activity)
                a.message(activity.getDrawable(R.drawable.ic_choices_monochrome), activity.getString(R.string.new_quoteintro))
            }

            //getData();
        }
        tuto = false
    }

    private fun actualday(): String {
        val datenow = Calendar.getInstance().time
        @SuppressLint("SimpleDateFormat") val df = SimpleDateFormat("dd/MM/yyyy")
        val dia = df.format(datenow)
        println(dia)
        return dia
    }

    private fun salvar(quote: Quote) {
        if (user?.isEmailVerified!!) {
            val quotesDB = QuoteModel(activity, quote)
            quotesDB.inserir()
        } else {
            Alert.builder(activity).mailmessage()


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
        colors.reverse()
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

    override fun onShow(p0: DialogInterface?) {
        Tools.fadeIn(blur, 300) //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDismiss(p0: DialogInterface?) {
        Tools.fadeOut(blur, 300) //To change body of created functions use File | Settings | File Templates.
    }


}
