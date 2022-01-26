package com.creat.motiv.features.home

import com.ilustris.motiv.base.beans.User


sealed class HomeViewState {
    data class UserRetrieved(val user: User) : HomeViewState()
    data class UsersRetrieved(val users: List<User>) : HomeViewState()
}