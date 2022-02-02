package com.ilustris.motiv.manager.features.style.newstyle.viewmodel

import android.graphics.Typeface
import com.ilustris.motiv.base.dialog.listdialog.DialogData

sealed class NewStyleState {
    data class FontOptionsRetrieved(val items: List<DialogData>) : NewStyleState()
    data class SelectFont(val index: Int, val typeface: Typeface) : NewStyleState()
}
