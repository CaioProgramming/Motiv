package com.ilustris.motiv.base.data.model

import com.google.firebase.Timestamp
import com.silent.ilustriscore.core.bean.BaseBean

const val AD_QUOTE = "ADVERTISEMENT_QUOTE"
const val PROFILE_QUOTE = "PROFILE_QUOTE"
const val USERS_QUOTE = "USERS_QUOTE"
const val NO_RESULTS_QUOTE = "NO_RESULTS_QUOTE"
const val FAVORITE_QUOTE = "FAVORITE_QUOTE"
const val SEARCH_QUOTE = "SEARCH_QUOTE"
const val SPLASH_QUOTE = "SPLASH_QUOTE"
const val ADMIN_QUOTE = "ADMIN_QUOTE"


typealias quoteList = ArrayList<Quote>

data class Quote(
    var quote: String = "",
    var author: String = "",
    var style: String = "default",
    var data: Timestamp? = null,
    var userID: String = "",
    var reports: ArrayList<Report> = ArrayList(),
    var likes: ArrayList<String> = ArrayList(),
    override var id: String = ""
) : BaseBean(id)


data class Report(val userID: String = "", val reason: String = "", val data: Timestamp? = null)
