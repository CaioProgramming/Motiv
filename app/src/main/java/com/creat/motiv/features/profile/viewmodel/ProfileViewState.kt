package com.creat.motiv.features.profile.viewmodel

import com.creat.motiv.features.home.HomeViewState
import com.creat.motiv.features.share.QuoteShareData
import com.ilustris.motiv.base.beans.Quote
import com.ilustris.motiv.base.beans.QuoteAdapterData
import com.ilustris.motiv.base.dialog.listdialog.dialogItems

sealed class ProfileViewState {

    data class RequestDelete(val quote: Quote) : ProfileViewState()
    data class RequestReport(val quote: Quote) : ProfileViewState()
    data class RequestEdit(val quote: Quote) : ProfileViewState()
    data class QuoteOptionsRetrieve(val dialogItems: dialogItems) : ProfileViewState()
    data class RequestShare(val quoteShareData: QuoteShareData) : ProfileViewState()
    data class ProfilePageRetrieve(val profileData: ProfileData) : ProfileViewState()
    data class RetrieveQuote(val quoteAdapterData: QuoteAdapterData) : ProfileViewState()

}