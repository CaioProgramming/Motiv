package com.ilustris.motiv.base.dialog.listdialog

typealias dialogItems = List<DialogData>

data class DialogData(val text: String, val action: () -> Unit)