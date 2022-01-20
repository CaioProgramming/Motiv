package com.creat.motiv.features.profile.viewmodel

import com.ilustris.motiv.base.beans.*

sealed class ProfileViewState {

    data class IconsRetrieved(val icons: List<Icon>, val requiredUser: User) : ProfileViewState()
    data class CoversRetrieved(val covers: List<Cover>, val requiredUser: User) : ProfileViewState()
    data class ProfilePageRetrieve(val profileData: ProfileData) : ProfileViewState()

}