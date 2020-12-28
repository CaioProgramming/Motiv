package com.creat.motiv.view.binders

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Handler
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import com.creat.motiv.R
import com.creat.motiv.databinding.NewquotepopupBinding
import com.creat.motiv.model.DTOMessage
import com.creat.motiv.model.beans.Quote
import com.creat.motiv.presenter.QuotePresenter
import com.creat.motiv.utilities.*
import com.creat.motiv.view.BaseView
import com.creat.motiv.view.adapters.PickedColor
import com.creat.motiv.view.adapters.RecyclerColorAdapter
import com.google.android.material.tabs.TabLayout
import com.pes.androidmaterialcolorpickerdialog.ColorPicker
import com.skydoves.balloon.ArrowOrientation
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.createBalloon
import com.skydoves.balloon.showAlignTop
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
            setArrowOrientation(ArrowOrientation.BOTTOM)
            setAutoDismissDuration(5000)
            setText("Deslize para ver as outras fontes")
            setTextColorResource(R.color.white)
            setBackgroundColorResource(R.color.colorPrimary)
            setBalloonAnimation(BalloonAnimation.ELASTIC)
            setLifecycleOwner(lifecycleOwner)
        }
        viewBind.currentFont.showAlignTop(balloon)
        showData(quote!!)
    }

    override fun showData(data: Quote) {
        getcolorGallery()
        getFonts()
        viewBind.run {
            quoteTextView.setTextColor(data.intTextColor())
            authorTextView.setTextColor(data.intTextColor())
            quoteTextView.setText(data.quote)
            authorTextView.setText(data.author)
            updateFont(data.font)
            textcolorfab.backgroundTintList = ColorStateList.valueOf(data.intTextColor())
            textcolorfab.imageTintList = ColorStateList.valueOf(if (data.intTextColor() == Color.BLACK) Color.WHITE else Color.BLACK)
            backcolorfab.backgroundTintList = if (data.intTextColor() == Color.BLACK) ColorStateList.valueOf(ColorUtils.lighten(data.intBackColor(), 1.5)) else ColorStateList.valueOf(ColorUtils.darken(data.intBackColor(), 1.6))
            backColorpicker()
            textColorPicker()
            saveQuoteButton.setOnClickListener {
                salvar()
            }
            quoteTextView.addTextChangedListener {
                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O) {
                    quoteTextView.autoSizeText()
                }
            }
        }

        UserViewBinder(data.userID, context, viewBind.userTop)
    }


    private fun updateFont(newPosition: Int) {
        quote?.let {
            it.font = newPosition
            val typeface = TextUtils.getTypeFace(context, TextUtils.fonts()[it.font].path)
            viewBind.quoteTextView.typeface = typeface
            viewBind.authorTextView.typeface = typeface
            viewBind.authorTextView.bounce()
            viewBind.authorTextView.bounce()
        }
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
        val llm = GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
        val recyclerColorAdapter = RecyclerColorAdapter(colors, context, ::getSelectedColor)
        viewBind.colorlibrary.layoutManager = llm
        viewBind.colorlibrary.adapter = recyclerColorAdapter
        viewBind.colorlibrary.setHasFixedSize(true)


    }

    private fun getFonts() {
        viewBind.fontTabs.apply {
            TextUtils.fonts().forEach {
                addTab(newTab().apply {
                    text = it.name
                })
            }
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (tab == null) return
                    updateFont(tab.position)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }

            })
            quote?.let {
                getTabAt(it.font)?.select()
            }
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
        textcolorfab.backgroundTintList = ColorStateList.valueOf(color)
        quoteTextView.bounce()
        authorTextView.bounce()
    }

    private fun NewquotepopupBinding.animateBackground(color: Int) {
        background.setBackgroundColor(color)
        backcolorfab.backgroundTintList = ColorStateList.valueOf(color)
        background.fadeIn()
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
                author = firebaseUser?.displayName ?: "",
                userID = firebaseUser?.uid ?: ""
        )
    }

}