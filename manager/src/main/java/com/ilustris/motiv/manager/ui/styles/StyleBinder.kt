package com.ilustris.motiv.manager.ui.styles

import android.content.Intent
import android.graphics.Color
import android.util.Log
import androidx.appcompat.widget.SearchView
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide
import com.creat.motiv.quote.beans.NEW_STYLE_ID
import com.creat.motiv.quote.beans.QuoteStyle
import com.creat.motiv.quote.beans.TextAlignment
import com.creat.motiv.quote.beans.TextSize
import com.giphy.sdk.core.models.Media
import com.giphy.sdk.ui.pagination.GPHContent
import com.giphy.sdk.ui.views.GPHGridCallback
import com.ilustris.animations.bounce
import com.ilustris.motiv.base.beans.Cover
import com.ilustris.motiv.base.dialog.BottomSheetAlert
import com.ilustris.motiv.base.presenter.QuoteStylePresenter
import com.ilustris.motiv.base.utils.ColorUtils
import com.ilustris.motiv.base.utils.FontUtils
import com.ilustris.motiv.base.utils.defineTextAlignment
import com.ilustris.motiv.base.utils.defineTextSize
import com.ilustris.motiv.manager.R
import com.ilustris.motiv.manager.databinding.FragmentStylesBinding
import com.ilustris.motiv.manager.ui.covers.CoverBottomSheetAlert
import com.silent.ilustriscore.core.presenter.BasePresenter
import com.silent.ilustriscore.core.view.BaseView

class StyleBinder(override val viewBind: FragmentStylesBinding): SearchView.OnQueryTextListener,BaseView<QuoteStyle>() {
    override val presenter = QuoteStylePresenter(this)

    var style = QuoteStyle()

    override fun initView() {

        viewBind.run {
            searchview.run {
                setOnQueryTextListener(this@StyleBinder)
                setQuery("Aesthetic", true)
            }
            styleFont.setOnClickListener {
                if (style.font < FontUtils.fonts.size) style.font++ else style.font = 0
                updateStyle()
            }
            styleSize.setOnClickListener {
                when(style.textSize) {
                    TextSize.DEFAULT -> style.textSize = TextSize.BIG
                    TextSize.BIG -> style.textSize = TextSize.EXTRASMALL
                    TextSize.SMALL -> style.textSize = TextSize.DEFAULT
                    TextSize.EXTRASMALL -> style.textSize = TextSize.SMALL
                }
                updateStyle()
            }
            styleAlign.setOnClickListener {
                when(style.textAlignment) {
                    TextAlignment.CENTER -> style.textAlignment = TextAlignment.END
                    TextAlignment.START -> style.textAlignment = TextAlignment.START
                    TextAlignment.END -> style.textAlignment = TextAlignment.CENTER
                }
                updateStyle()
            }
            getColorGallery()
        }
        presenter.loadData()
    }

    override fun showListData(list: List<QuoteStyle>) {
        super.showListData(list)
        viewBind.stylesRecycler.stylesRecycler.run {
            adapter = StylePreviewAdapter(list,true) {
                if (it.id != NEW_STYLE_ID) {
                    BottomSheetAlert(context, "Tem certeza?", "Ao remover esse estilo não será possível recuperá-lo", {
                        presenter.deleteData(it)
                    })
                } else {
                    context.startActivity(Intent(context,NewStyleActivity::class.java))
                }
            }
        }
    }

    private fun getColorGallery() {
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
        val recyclerColorAdapter = RecyclerColorAdapter(colors, context) {
            style.textColor = it
            updateStyle()
        }
        viewBind.colorGallery.adapter = recyclerColorAdapter
        viewBind.colorGallery.setHasFixedSize(true)


    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let {
            if (query.isNotBlank()) {
                viewBind.gifsGridView.content = GPHContent.searchQuery(it)
                viewBind.gifsGridView.callback = object : GPHGridCallback {
                    override fun contentDidUpdate(resultCount: Int) {
                        Log.i(javaClass.simpleName, "contentDidUpdate: new gifs $resultCount")
                    }

                    override fun didSelectMedia(media: Media) {
                        media.images.downsizedMedium?.gifUrl?.let { gif ->
                            style.backgroundURL = gif
                            updateStyle()
                        }


                    }
                }
            }
        }
        return false
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        return false
    }

    fun updateStyle() {
        viewBind.run {
            Glide.with(context).asGif().centerCrop().load(style.backgroundURL).into(styleBackground)
            styleSize.text = when(style.textSize) {
                TextSize.DEFAULT -> "Aa"
                TextSize.BIG -> "AA"
                TextSize.SMALL -> "aa"
                TextSize.EXTRASMALL -> "ᵃᵃ"
            }
            styleFont.typeface = FontUtils.getTypeFace(context,style.font)
            styleText.run {
                defineTextAlignment(style.textAlignment)
                defineTextSize(style.textSize)
                typeface = FontUtils.getTypeFace(context,style.font)
                setTextColor(Color.parseColor(style.textColor))
                bounce()
            }
            styleAlign.run {
                val src = when(style.textAlignment) {
                    TextAlignment.CENTER -> R.drawable.ic_baseline_format_align_center_24
                    TextAlignment.START -> R.drawable.ic_baseline_format_align_left_24
                    TextAlignment.END -> R.drawable.ic_baseline_format_align_left_24
                }
                setImageResource(src)
                bounce()
            }

        }
    }
}