package com.creat.motiv.view.binders

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.creat.motiv.R
import com.creat.motiv.databinding.NewquotepopupBinding
import com.creat.motiv.model.DTOMessage
import com.creat.motiv.model.beans.Quote
import com.creat.motiv.presenter.QuotePresenter
import com.creat.motiv.utilities.*
import com.creat.motiv.utilities.ColorUtils.toHex
import com.creat.motiv.view.BaseView
import com.creat.motiv.view.adapters.FontAdapter
import com.creat.motiv.view.adapters.PickedColor
import com.creat.motiv.view.adapters.RecyclerColorAdapter
import com.google.android.material.tabs.TabLayout
import com.skydoves.balloon.ArrowOrientation
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.createBalloon
import com.skydoves.balloon.showAlignBottom
import java.util.*
import kotlin.math.abs


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
        showData(quote!!)
        showBalloonTip()

    }

    private fun showBalloonTip() {
        val balloon = createBalloon(context) {
            setArrowSize(10)
            setWidthRatio(0.5f)
            setHeight(50)
            setCornerRadius(10f)
            setArrowOrientation(ArrowOrientation.TOP)
            setAutoDismissDuration(50000)
            setText("Deslize para baixo ver as outras fontes")
            setTextColorResource(R.color.white)
            setBackgroundColorResource(R.color.colorPrimary)
            setBalloonAnimation(BalloonAnimation.ELASTIC)
            setOnBalloonDismissListener {
                viewBind.fontSelector.run {
                    if (tabCount > 0 && quote?.font == 0) {
                        val randomFont = Random().nextInt(tabCount)
                        getTabAt(randomFont).select()

                    }
                }
            }
            setLifecycleOwner(lifecycleOwner)
        }
        viewBind.fontSelector.showAlignBottom(balloon)
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
                quoteTextView.autoSizeText()
            }
        }
    }


    private fun updateFont(newPosition: Int) {
        quote?.let {
            it.font = newPosition
            val typeface = TextUtils.getTypeFace(context, TextUtils.fonts()[it.font].path)
            viewBind.fontSelector.getTabAt(newPosition).select()
            viewBind.quoteTextView.typeface = typeface
            viewBind.authorTextView.typeface = typeface
            viewBind.quoteTextView.bounce()
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

    fun setupTab(position: Int, tab: TabLayout.Tab) {
        val f = TextUtils.fonts()[position]
        tab.view.let {
            val viewGroup = it as ViewGroup
            for (i in 0 until viewGroup.childCount) {
                val child = viewGroup.getChildAt(i)
                if (child is TextView) {
                    child.run {
                        typeface = TextUtils.getTypeFace(context, f.path)
                        text = f.name
                        //Toast.makeText(context, "Updated tab at $position font", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    fun buildtabs() {
        for (i in 0..viewBind.fontSelector.tabCount) {
            viewBind.fontSelector.getTabAt(i).let { setupTab(i, it) }
        }
    }

    private fun getFonts() {
        viewBind.fontSelector.apply {
            val fonts = TextUtils.fonts()
            for (i in 0 until fonts.count()) {
                addTab(this.newTab().setText(fonts[i].name))
            }
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab.let {
                        updateFont(it.position)
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }

            })
        }
        buildtabs()
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
            snackmessage(context, message = "Citação salva com sucesso!", backcolor = context.resources.getColor(R.color.material_green500))
            quote = emptyQuote()
            quote?.let { showData(it) }
        } else if (dtoMessage.operationType == OperationType.DATA_UPDATED) {
            snackmessage(context, message = "Citação atualizada com sucesso!", backcolor = context.resources.getColor(R.color.material_green500))
            if (context is Activity) {
                context.finish()
            }
        }
    }

    fun salvar() {
        quote?.let {
            it.quote = viewBind.quoteTextView.text.toString()
            it.author = viewBind.authorTextView.text.toString()
            if (it.quote.isNotEmpty()) presenter().saveData(it, it.id) else snackmessage(context, message = Tools.emptyquote())
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
                author = firebaseUser.displayName ?: "",
                userID = firebaseUser.uid ?: ""
        )
    }

}