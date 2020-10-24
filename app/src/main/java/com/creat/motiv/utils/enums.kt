package com.creat.motiv.utils

import com.creat.motiv.R

enum class SelectedViewType {
    BACKGROUND, TEXT
}

enum class MessageType {
    ERROR, SUCCESS, WARNING, INFO
}

enum class ErrorType {
    USERNOTFOUND, QUOTENOTFOUND, PICNOTFOUND, UNKNOW, USERDISCONNECTED
}

enum class DialogStyles(val style: Int) {
    DEFAULT_NO_BORDER(R.style.Dialog_No_Border),
    BOTTOM_NO_BORDER(R.style.Bottom_Dialog_No_Border),
    FULL_SCREEN(R.style.Full_Screen_Dialog)
}