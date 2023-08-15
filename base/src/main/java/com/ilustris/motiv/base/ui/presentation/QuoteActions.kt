package com.ilustris.motiv.base.ui.presentation

import android.graphics.Bitmap
import com.ilustris.motiv.base.data.model.QuoteDataModel

interface QuoteActions {

    fun onClickUser(uid: String)
    fun onLike(dataModel: QuoteDataModel)
    fun onShare(dataModel: QuoteDataModel, bitmap: Bitmap)
    fun onDelete(dataModel: QuoteDataModel)
    fun onEdit(dataModel: QuoteDataModel)
    fun onReport(dataModel: QuoteDataModel)
}