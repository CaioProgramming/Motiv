package com.ilustriscore.core.utilities

import com.ilustriscore.R


enum class SelectedViewType {
    BACKGROUND, TEXT
}

enum class MessageType {
    ERROR, SUCCESS, WARNING, INFO
}

enum class OperationType {
    DATA_SAVED, DATA_UPDATED, DELETE, DATA_RETRIEVED, UNKNOW
}

enum class ErrorType {
    USERNOTFOUND, QUOTENOTFOUND, PICNOTFOUND, UNKNOW, USERDISCONNECTED
}

enum class DialogStyles(val style: Int) {
    DEFAULT_NO_BORDER(R.style.Theme_IlustrisDesign_Dialog),
    BOTTOM_NO_BORDER(R.style.Theme_IlustrisDesign_Dialog_BottomDialog),
    FULL_SCREEN(R.style.Theme_IlustrisDesign_Dialog_FullScreenDialog)
}