package com.ilustris.motiv.manager.giphy

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.creat.motiv.manager.R
import com.giphy.sdk.core.models.Media
import com.giphy.sdk.ui.GPHContentType
import com.giphy.sdk.ui.GPHSettings
import com.giphy.sdk.ui.Giphy
import com.giphy.sdk.ui.themes.GPHTheme
import com.giphy.sdk.ui.views.GiphyDialogFragment

class GiphySelectDialog(private val context: Context, private val onSelectGiphy: (String) -> Unit) {

    fun show() {
        val activity = context as AppCompatActivity
        val fragmentManager = activity.supportFragmentManager
        val giphyKey = context.getString(R.string.giphy_api)
        Giphy.configure(context, context.getString(R.string.giphy_api), true)
        val settings = GPHSettings(
            theme = GPHTheme.Automatic,
            mediaTypeConfig = arrayOf(GPHContentType.gif),
            stickerColumnCount = 2,
            selectedContentType = GPHContentType.gif
        )
        GiphyDialogFragment.newInstance(settings, giphyKey)
            .apply {
                gifSelectionListener = object : GiphyDialogFragment.GifSelectionListener {
                    override fun didSearchTerm(term: String) {}

                    override fun onDismissed(selectedContentType: GPHContentType) {}

                    override fun onGifSelected(
                        media: Media,
                        searchTerm: String?,
                        selectedContentType: GPHContentType
                    ) {
                        media.images.downsizedMedium?.gifUrl?.let { url ->
                            onSelectGiphy(url)
                        }
                    }

                }
            }
            .show(fragmentManager, GiphyDialogFragment::class.java.simpleName)
    }

}