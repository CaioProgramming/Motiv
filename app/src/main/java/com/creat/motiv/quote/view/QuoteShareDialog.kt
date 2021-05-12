package com.creat.motiv.quote.view

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.setPadding
import com.bumptech.glide.Glide
import com.creat.motiv.BuildConfig
import com.creat.motiv.R
import com.creat.motiv.databinding.ShareQuotePreviewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ilustris.animations.bounce
import com.ilustris.animations.fadeIn
import com.ilustris.animations.fadeOut
import com.ilustris.motiv.base.beans.Quote
import com.ilustris.motiv.base.beans.Style
import com.ilustris.motiv.base.dialog.BaseAlert
import com.ilustris.motiv.base.utils.*
import com.silent.ilustriscore.core.utilities.*
import com.silent.ilustriscore.core.utilities.ColorUtils
import kotlinx.android.synthetic.main.share_quote_preview.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class QuoteShareDialog(context: Context, val quote: Quote, val quoteStyle: Style) : BaseAlert<ShareQuotePreviewBinding>(context, R.layout.share_quote_preview, style = DialogStyles.FULL_SCREEN) {


    override fun ShareQuotePreviewBinding.configure() {
        sharedQuote = quote
        val paddingVertical = shareView.paddingTop
        val paddingHorizontal = shareView.paddingStart

        quoteStyle.run {

            quoteImage.loadGif(backgroundURL) {
                shareButton.isEnabled = true
                progressBar.fadeOut()
                quoteContent.fadeIn()
            }
            quoteTextView.setTextColor(Color.parseColor(textColor))
            quoteTextView.typeface = FontUtils.getTypeFace(context, font)
            authorTextView.setTextColor(Color.parseColor(textColor))
            authorTextView.typeface = FontUtils.getTypeFace(context, font)
            quoteTextView.defineTextAlignment(quoteStyle.textAlignment)
            authorTextView.defineTextAlignment(quoteStyle.textAlignment)
            shadowStyle.run {
                quoteTextView.setShadowLayer(radius, dx, dy, Color.parseColor(shadowColor))
                authorTextView.setShadowLayer(radius, dx, dy, Color.parseColor(shadowColor))
            }
        }
        shareButton.setOnClickListener {
            shareButton.invisible()
            delayedFunction(2500) {
                generateCardImage { file ->
                    val uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", file)
                    copyToClipboard()
                    uri?.let {
                        val shareIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            type = "image/*"
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            setDataAndType(uri, context.contentResolver.getType(uri))
                            putExtra(Intent.EXTRA_SUBJECT, context.resources.getString(R.string.app_name))
                            putExtra(Intent.EXTRA_TEXT, "${quote.quote}\n - ${quote.author}")
                            putExtra(Intent.EXTRA_STREAM, uri)
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Compartilhar post em..."))
                        delayedFunction {
                            shareButton.fadeIn()
                        }
                    }
                }
            }
        }
    }

    private fun copyToClipboard() {
        val clipboard: ClipboardManager? = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
        val clip = ClipData.newPlainText("Motiv", "${quote.quote}\uD83E\uDE90\n - ${quote.author} \nVeja mais em: @motivbr\n#${context.getString(R.string.app_name).toLowerCase()} #${quote.author.replace(" ", "").toLowerCase()}")
        clipboard?.setPrimaryClip(clip)
    }

    private fun ShareQuotePreviewBinding.generateCardImage(onFileSave: (File) -> Unit) {
        try {
            shareView.run {
                isDrawingCacheEnabled = true
                val bitmap = Bitmap.createBitmap(drawingCache)
                isDrawingCacheEnabled = false
                val cachePath = context.cacheDir.path + "/shared_quotes/"
                val cacheDir = File(cachePath)
                if (!cacheDir.exists()) cacheDir.mkdirs()
                val stream = FileOutputStream(cachePath + "quote_${quote.id}.png")
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                stream.close()
                val file = File(cachePath + "quote_${quote.id}.png")
                Log.i(javaClass.simpleName, "generateCardImage: file saved ${file.absolutePath}")
                onFileSave.invoke(file)
            }


        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(javaClass.simpleName, "generateCardImage: ${e.message}")
            showSnackBar(context, "Ocorreu um erro ao compartilhar a frase", ContextCompat.getColor(context, ColorUtils.ERROR), rootView = shareCard)
        }

    }
}