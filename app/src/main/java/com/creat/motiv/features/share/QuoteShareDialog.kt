package com.creat.motiv.features.share

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.core.content.FileProvider
import com.creat.motiv.BuildConfig
import com.creat.motiv.R
import com.creat.motiv.databinding.ShareQuotePreviewBinding
import com.ilustris.animations.fadeIn
import com.ilustris.animations.fadeOut
import com.ilustris.motiv.base.beans.quote.QuoteShareData
import com.ilustris.motiv.base.utils.defineTextAlignment
import com.ilustris.motiv.base.utils.getTypefaceStyle
import com.ilustris.motiv.base.utils.loadGif
import com.silent.ilustriscore.core.utilities.DialogStyles
import com.silent.ilustriscore.core.utilities.delayedFunction
import com.silent.ilustriscore.core.utilities.showSnackBar
import com.silent.ilustriscore.core.view.BaseAlert
import java.io.File
import java.io.FileOutputStream
import java.util.*

class QuoteShareDialog(context: Context, val quoteShareData: QuoteShareData) :
    BaseAlert(context, R.layout.share_quote_preview, style = DialogStyles.FULL_SCREEN) {


    override fun View.configure() {
        ShareQuotePreviewBinding.bind(this).run {
            quoteTextView.text = quoteShareData.quote.quote
            authorTextView.text = quoteShareData.quote.author
            quoteShareData.style.run {
                quoteImage.loadGif(backgroundURL) {
                    progressBar.fadeOut()
                    quoteContent.fadeIn()
                }
                quoteTextView.setTypeface(typeface, fontStyle.getTypefaceStyle())
                authorTextView.setTypeface(typeface, fontStyle.getTypefaceStyle())
                quoteTextView.setTextColor(Color.parseColor(textColor))
                authorTextView.setTextColor(Color.parseColor(textColor))
                quoteTextView.defineTextAlignment(textAlignment)
                authorTextView.defineTextAlignment(textAlignment)
                shadowStyle.run {
                    quoteTextView.setShadowLayer(radius, dx, dy, Color.parseColor(shadowColor))
                    authorTextView.setShadowLayer(radius, dx, dy, Color.parseColor(shadowColor))
                }
            }
            delayedFunction(3000) {
                generateCardImage { file ->
                    val uri = FileProvider.getUriForFile(
                        context,
                        BuildConfig.APPLICATION_ID + ".fileprovider",
                        file
                    )
                    copyToClipboard()
                    uri?.let {
                        val shareIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            type = "image/*"
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            setDataAndType(uri, context.contentResolver.getType(uri))
                            putExtra(
                                Intent.EXTRA_SUBJECT,
                                context.resources.getString(R.string.app_name)
                            )
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "${quoteShareData.quote.quote}\n - ${quoteShareData.quote.author}"
                            )
                            putExtra(Intent.EXTRA_STREAM, uri)
                        }
                        context.startActivity(
                            Intent.createChooser(
                                shareIntent,
                                "Compartilhar post em..."
                            )
                        )
                        delayedFunction {
                            getScreenshot().delete()
                            dialog.dismiss()
                        }
                    }
                }
            }
        }

    }

    private fun getScreenshot(): File {
        val cachePath = context.cacheDir.path + "/shared_quotes/"
        return File(cachePath + "quote_${quoteShareData.quote.id}.png")
    }

    private fun copyToClipboard() {
        val clipboard: ClipboardManager? =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
        val clip = ClipData.newPlainText(
            "Motiv",
            "${quoteShareData.quote.quote}\uD83E\uDE90\n - ${quoteShareData.quote.author} \nVeja mais em: @motivbr\n#${
                context.getString(R.string.app_name)
                    .lowercase(Locale.getDefault())
            } #${
                quoteShareData.quote.author.replace(" ", "")
                    .lowercase(Locale.getDefault())
            }"
        )
        clipboard?.setPrimaryClip(clip)
    }

    private fun ShareQuotePreviewBinding.generateCardImage(onFileSave: (File) -> Unit) {
        try {
            shareView.run {
                val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                draw(canvas)
                val cachePath = context.cacheDir.path + "/shared_quotes/"
                val cacheDir = File(cachePath)
                if (!cacheDir.exists()) cacheDir.mkdirs()
                val stream = FileOutputStream(cachePath + "quote_${quoteShareData.quote.id}.jpeg")
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                stream.close()
                val file = File(cachePath + "quote_${quoteShareData.quote.id}.jpeg")
                Log.i(javaClass.simpleName, "generateCardImage: file saved ${file.absolutePath}")
                onFileSave.invoke(file)
            }


        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(javaClass.simpleName, "generateCardImage: ${e.message}")
            view.showSnackBar("Ocorreu um erro ao compartilhar a frase", Color.RED)
        }

    }
}