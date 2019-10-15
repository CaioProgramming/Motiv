package com.creat.motiv.Utils

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.creat.motiv.Adapters.RecyclerColorAdapter
import com.creat.motiv.Model.Beans.Quotes
import com.creat.motiv.Model.QuotesDB
import com.creat.motiv.R
import com.github.mmin18.widget.RealtimeBlurView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.pes.androidmaterialcolorpickerdialog.ColorPicker
import de.hdodenhof.circleimageview.CircleImageView
import de.mateware.snacky.Snacky
import java.text.SimpleDateFormat
import java.util.*

class NewQuotepopup(private val activity: Activity?) {
    private var backcolorfab: ImageButton? = null
    private var texcolorfab: ImageButton? = null
    private val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private var quotesDB: QuotesDB? = null
    private var m_dialog: Dialog? = null
    private var blur: RealtimeBlurView
    private var preferences: Pref? = null
    internal var tuto = true
    private var colorlibrary: RecyclerView? = null
    private var frase: EditText? = null
    private var author: EditText? = null
    private var background: LinearLayout? = null
    private var fontid: TextView? = null
    private var texcolorid: TextView? = null
    private var backcolorid: TextView? = null
    private var f: Int = 0
    private var isfirst = true
    private var font: TextView? = null

    init {
        blur = activity!!.findViewById(R.id.rootblur)

    }

    fun showup() {
        val myDialog = BottomSheetDialog(activity!!, R.style.Bottom_Dialog_No_Border)
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        myDialog.setCanceledOnTouchOutside(true)
        myDialog.setContentView(R.layout.newquotepopup)
        myDialog.window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        myDialog.show()
        font = myDialog.findViewById(R.id.font)
        backcolorfab = myDialog.findViewById(R.id.backcolorfab)
        texcolorfab = myDialog.findViewById(R.id.textcolorfab)
        backcolorid = myDialog.findViewById(R.id.backcolorid)
        texcolorid = myDialog.findViewById(R.id.texcolorid)
        fontid = myDialog.findViewById(R.id.fontid)
        background = myDialog.findViewById(R.id.background)
        author = myDialog.findViewById(R.id.author)
        frase = myDialog.findViewById(R.id.quote)


        val username = myDialog.findViewById<TextView>(R.id.username)
        val userpic = myDialog.findViewById<CircleImageView>(R.id.userpic)
        this.colorlibrary = myDialog.findViewById(R.id.colorlibrary)
        val salvar = myDialog.findViewById<Button>(R.id.salvar)
        username!!.text = user!!.displayName
        Glide.with(activity).load(user.photoUrl).into(userpic!!)
        try {
            colorgallery()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

        backcolorfab!!.setOnClickListener { BackColorpicker() }

        texcolorfab!!.setOnClickListener { Textocolorpicker() }

        salvar!!.setOnClickListener {
            salvar()
            myDialog.dismiss()
        }


        frase!!.setOnKeyListener(View.OnKeyListener { view, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_NEXT) {
                if (!keyEvent.isShiftPressed) {
                    Log.v("AndroidEnterKeyActivity", "Enter Key Pressed!")
                    when (view.id) {
                        1 -> author!!.requestFocus()
                    }
                    return@OnKeyListener true
                }
            }
            false
        })


        author!!.setOnKeyListener(View.OnKeyListener { view, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEARCH ||
                    i == EditorInfo.IME_ACTION_DONE ||
                    keyEvent.action == KeyEvent.ACTION_DOWN && keyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {

                if (!keyEvent.isShiftPressed) {
                    println("Enter key pressed")
                    salvar()
                    return@OnKeyListener true
                }

            }
            false // pass on to other listeners.
        })

        myDialog.setOnDismissListener {
            val out = AnimationUtils.loadAnimation(activity, R.anim.fade_out)
            blur.startAnimation(out)
            blur.visibility = View.GONE
        }

        font!!.setOnClickListener {
            if (!isfirst) {
                f++
            }
            val fonts = Tools.fonts(activity)

            if (f == fonts.size) {
                f = 0
            }
            font!!.typeface = fonts[f]
            frase!!.typeface = fonts[f]
            author!!.typeface = fonts[f]
            isfirst = false
        }
        val `in` = AnimationUtils.loadAnimation(activity, R.anim.fade_in)

        blur.visibility = View.VISIBLE
        blur.startAnimation(`in`)

    }


    internal fun showedit(quote: Quotes) {

        val myDialog = BottomSheetDialog(activity!!)
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        myDialog.window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        myDialog.setCanceledOnTouchOutside(true)
        myDialog.setContentView(R.layout.newquotepopup)
        this.font = myDialog.findViewById(R.id.font)
        myDialog.show()
        this.backcolorfab = myDialog.findViewById(R.id.backcolorfab)
        this.texcolorfab = myDialog.findViewById(R.id.textcolorfab)
        this.backcolorid = myDialog.findViewById(R.id.backcolorid)
        this.texcolorid = myDialog.findViewById(R.id.texcolorid)
        this.fontid = myDialog.findViewById(R.id.fontid)
        this.background = myDialog.findViewById(R.id.background)
        this.author = myDialog.findViewById(R.id.author)
        this.frase = myDialog.findViewById(R.id.quote)
        val username = myDialog.findViewById<TextView>(R.id.username)
        val userpic = myDialog.findViewById<CircleImageView>(R.id.userpic)
        this.colorlibrary = myDialog.findViewById(R.id.colorlibrary)
        val salvar = myDialog.findViewById<Button>(R.id.salvar)
        username!!.text = user!!.displayName
        Glide.with(activity).load(user.photoUrl).into(userpic!!)

        //theme();
        try {
            colorgallery()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

        backcolorfab!!.setOnClickListener { BackColorpicker() }

        texcolorfab!!.setOnClickListener { Textocolorpicker() }




        Tutorial()
        LoadQuote(username, userpic, quote)
        salvar!!.setOnClickListener {
            atualizar(quote)
            myDialog.dismiss()
        }


        font!!.setOnClickListener {
            if (!isfirst) {
                f++
            }
            val fonts = Tools.fonts(activity)

            if (f == fonts.size) {
                f = 0
            }
            font!!.typeface = fonts[f]
            frase!!.typeface = fonts[f]
            author!!.typeface = fonts[f]
            isfirst = false
        }

        myDialog.show()
        myDialog.setOnDismissListener {
            val out = AnimationUtils.loadAnimation(activity, R.anim.fade_out)
            blur.startAnimation(out)
            blur.visibility = View.GONE
            val refreshLayout = activity.findViewById<SwipeRefreshLayout>(R.id.refresh)
            if (refreshLayout != null) {
                refreshLayout.isRefreshing = true
            } else {
                val pager = activity.findViewById<ViewPager>(R.id.pager)
                pager.currentItem = 2
            }
        }


    }


    private fun atualizar(quote: Quotes) {
        val fonti = f

        if (author!!.text.toString() == "") {
            quote.author = user!!.displayName!!
        }

        quote.quote = frase!!.text.toString()
        quote.author = author!!.text.toString()
        quote.font = fonti
        quote.backgroundcolor = Integer.parseInt(backcolorid!!.text.toString())
        quote.textcolor = Integer.parseInt(texcolorid!!.text.toString())
        quotesDB = QuotesDB(activity!!, quote)
        quotesDB!!.Editar()
        val refreshLayout = activity.findViewById<SwipeRefreshLayout>(R.id.refresh)
        refreshLayout.isRefreshing = true


    }


    private fun LoadQuote(username: TextView, userpic: CircleImageView, quotes: Quotes) {
        //quoteID.setText(quotes.getId());
        frase!!.setText(quotes.quote)
        author!!.setText(quotes.author)
        username.text = quotes.username
        background!!.setBackgroundColor(quotes.backgroundcolor!!)

        frase!!.setTextColor(quotes.textcolor!!)
        if (quotes.font != null) {
            frase!!.typeface = Tools.fonts(activity!!)[quotes.font!!]
            author!!.typeface = Tools.fonts(activity)[quotes.font!!]
            font!!.typeface = Tools.fonts(activity)[quotes.font!!]
            f = quotes.font!!
        } else {
            frase!!.typeface = Typeface.DEFAULT
            author!!.typeface = Typeface.DEFAULT

        }
        author!!.setTextColor(quotes.textcolor!!)
        texcolorid!!.text = quotes.textcolor.toString()
        backcolorid!!.text = quotes.backgroundcolor.toString()
        backcolorfab!!.imageTintList = ColorStateList.valueOf(quotes.backgroundcolor!!)
        texcolorfab!!.imageTintList = ColorStateList.valueOf(quotes.textcolor!!)

        fontid!!.text = quotes.font.toString()
        Glide.with(activity!!).load(quotes.userphoto).into(userpic)

    }


    private fun BackColorpicker() {
        val cp = ColorPicker(activity)
        cp.show()
        cp.enableAutoClose()
        cp.setCallback { color ->
            background!!.visibility = View.INVISIBLE
            background!!.setBackgroundColor(color)
            val cx = background!!.right
            val cy = background!!.top
            val radius = Math.max(background!!.width, background!!.height)
            val anim = ViewAnimationUtils.createCircularReveal(background, cx, cy,
                    0f, radius.toFloat())
            background!!.visibility = View.VISIBLE
            anim.start()
            backcolorid!!.text = color.toString()
            backcolorfab!!.imageTintList = ColorStateList.valueOf(color)
        }
    }

    private fun Textocolorpicker() {
        val cp = ColorPicker(activity)
        cp.show()
        cp.enableAutoClose()
        cp.setCallback { color ->
            Log.d("Pure Hex", Integer.toHexString(color))
            val colorFrom = frase!!.currentTextColor
            val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, color)
            colorAnimation.duration = 2000 // milliseconds
            colorAnimation.addUpdateListener {
                frase!!.setTextColor(color)
                author!!.setTextColor(color)
            }
            texcolorfab!!.imageTintList = ColorStateList.valueOf(color)
            colorAnimation.start()
            texcolorid!!.text = color.toString()
        }
    }


    private fun Tutorial() {
        val novo = Objects.requireNonNull<Bundle>(Objects.requireNonNull<Activity>(activity).intent.extras).getBoolean("novo")
        if (novo) {
            val preferences = Pref(Objects.requireNonNull<Activity>(activity))
            if (!preferences.writetutorialstate()) {
                preferences.setWriteTutorial(true)
                val a = Alert(activity!!)
                a.Message(activity.getDrawable(R.drawable.ic_choices_monochrome), activity.getString(R.string.new_quoteintro))
            }

            //getData();
        }
        tuto = false
    }


    private fun salvar() {
        agree()
        val datenow = Calendar.getInstance().time
        @SuppressLint("SimpleDateFormat") val df = SimpleDateFormat("dd/MM/yyyy")
        val dia = df.format(datenow)
        println(dia)
        var fonti: Int? = f
        if (fontid!!.text !== "") {
            fonti = Integer.parseInt(fontid!!.text.toString())

        }
        if (texcolorid!!.text === "") {
            texcolorid!!.text = Color.BLACK.toString()
        }
        if (backcolorid!!.text === "") {
            backcolorid!!.text = Color.WHITE.toString()
        }

        val quote = Quotes("", frase!!.text.toString(), author!!.text.toString(),
                dia,
                user!!.uid, user.displayName!!, user.photoUrl.toString(),
                Integer.parseInt(backcolorid!!.text.toString()), Integer.parseInt(texcolorid!!.text.toString()), false, f)



        if (author!!.text.toString() == "") {
            quote.author = user.displayName!!
        }
        if (user.isEmailVerified) {
            val quotesDB = QuotesDB(quote, Objects.requireNonNull<Activity>(activity))
            quotesDB.Inserir()
        } else {

            val builder = AlertDialog.Builder(activity, R.style.Theme_AppCompat_Dialog).setMessage("Email não verificado")
            builder.setMessage("Seu email não foi verificado, então não vai poder compartilhar frases.")
            builder.setPositiveButton("Então me envia esse email meu consagrado") { dialogInterface, i -> user.sendEmailVerification() }
            builder.setNegativeButton("Beleza então, não vou postar nada") { dialogInterface, i -> }

            builder.show()


        }


    }


    private fun agree() {
        if (activity == null) throw AssertionError()
        preferences = Pref(activity)
        m_dialog = Dialog(activity)
        val `in` = AnimationUtils.loadAnimation(activity, R.anim.slide_in_top)
        val m_inflater = LayoutInflater.from(activity)
        val m_view = m_inflater.inflate(R.layout.politics, null)
        m_dialog!!.setContentView(m_view)
        val agreebutton = m_view.findViewById<Button>(R.id.agreebutton)
        agreebutton.setOnClickListener {
            m_dialog!!.dismiss()
            preferences!!.setAgree(true)
        }
        m_view.startAnimation(`in`)
        if (!preferences!!.agreestate()) {
            Snacky.builder().setActivity(activity).warning()
                    .setText("Você precisa concordar com os termos de uso para usar o aplicativo!")
                    .setAction("Ok") { m_dialog!!.show() }.show()
        }
        m_dialog!!.setCanceledOnTouchOutside(false)
        m_dialog!!.setCancelable(false)
    }


    @Throws(ClassNotFoundException::class, IllegalAccessException::class)
    private fun colorgallery() {
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
        Collections.reverse(colors)
        colorlibrary!!.setHasFixedSize(true)
        val llm = GridLayoutManager(activity, 3, GridLayoutManager.HORIZONTAL, false)
        val recyclerColorAdapter = RecyclerColorAdapter(colors, activity!!,
                background!!, frase!!, author!!, texcolorid!!, backcolorid!!, texcolorfab!!, backcolorfab!!)
        recyclerColorAdapter.notifyDataSetChanged()

        colorlibrary!!.adapter = recyclerColorAdapter
        colorlibrary!!.layoutManager = llm

    }


}
