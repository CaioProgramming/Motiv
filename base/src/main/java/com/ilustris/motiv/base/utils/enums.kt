package com.ilustris.motiv.base


enum class SelectedViewType {
    BACKGROUND, TEXT
}

enum class DialogStyles(val style: Int) {
    DEFAULT_NO_BORDER(R.style.Dialog_No_Border),
    BOTTOM_NO_BORDER(R.style.Bottom_Dialog_No_Border),
    FULL_SCREEN(R.style.Full_Screen_Dialog)
}