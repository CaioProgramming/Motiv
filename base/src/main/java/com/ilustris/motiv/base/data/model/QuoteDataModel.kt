package com.ilustris.motiv.base.data.model

data class QuoteDataModel(
    val quoteBean: Quote,
    val user: User?,
    var style: Style,
    val isFavorite: Boolean = false,
    val isUserQuote: Boolean = false
)
