package com.ilustris.motiv.manager.styles.ui.form.presentation

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ilustris.motiv.base.data.model.AnimationProperties
import com.ilustris.motiv.base.data.model.ShadowStyle
import com.ilustris.motiv.base.data.model.Style
import com.ilustris.motiv.base.data.model.StyleProperties
import com.ilustris.motiv.base.data.model.TextProperties
import com.ilustris.motiv.base.service.StyleService
import com.silent.ilustriscore.core.model.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewStyleViewModel @Inject constructor(
    application: Application,
    override val service: StyleService
) : BaseViewModel<Style>(application) {

    val newStyle = MutableLiveData(
        Style(
            styleProperties = StyleProperties(),
            animationProperties = AnimationProperties(),
            shadowStyle = ShadowStyle(),
            textProperties = TextProperties()
        )
    )

    fun updateStyleColor(colorItem: String) {
        newStyle.postValue(newStyle.value?.copy(textColor = colorItem))
    }

    fun updateStyleBackColor(colorItem: String) {


        newStyle.postValue(
            newStyle.value?.copy(
                styleProperties = newStyle.value?.styleProperties?.copy(
                    backgroundColor = colorItem
                ) ?: StyleProperties(backgroundColor = colorItem)
            )
        )
    }

    fun updateShadowStyle(shadowStyle: ShadowStyle) {
        newStyle.postValue(newStyle.value?.copy(shadowStyle = shadowStyle))
    }

    fun updateTextProperties(textProperties: TextProperties) {
        newStyle.postValue(newStyle.value?.copy(textProperties = textProperties))
    }

    fun updateStyleBackground(url: String) {
        newStyle.postValue(newStyle.value?.copy(backgroundURL = url))
    }

    fun updateStyleProperties(properties: StyleProperties) {
        newStyle.postValue(newStyle.value?.copy(styleProperties = properties))
    }

    fun updateAnimationProperties(animationProperties: AnimationProperties) {
        newStyle.postValue(newStyle.value?.copy(animationProperties = animationProperties))
    }

}