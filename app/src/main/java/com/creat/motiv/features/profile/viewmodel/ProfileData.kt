package com.creat.motiv.features.profile.viewmodel

import com.ilustris.motiv.base.beans.Quote
import com.ilustris.motiv.base.beans.User

data class ProfileData(val user: User, val likes: ArrayList<Quote>, val posts: ArrayList<Quote>)
