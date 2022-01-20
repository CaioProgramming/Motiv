package com.ilustris.motiv.base.beans.quote

import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.firebase.auth.FirebaseUser
import com.ilustris.motiv.base.beans.Style
import com.ilustris.motiv.base.beans.User
import kotlin.collections.ArrayList


data class QuoteAdapterData(
    var quote: Quote,
    var style: Style = Style.defaultStyle,
    var user: User = User(),
    var currentUser: FirebaseUser? = null,
    var likers: ArrayList<User>? = null,
    var users: List<User>? = null,
    var advertise: UnifiedNativeAd? = null
)

