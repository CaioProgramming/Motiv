package com.creat.motiv.features.profile.settings

import com.creat.motiv.features.profile.viewmodel.ProfileViewState
import com.ilustris.motiv.base.beans.Cover
import com.ilustris.motiv.base.beans.Icon
import com.ilustris.motiv.base.beans.User

sealed class SettingsViewState {
    data class IconsRetrieved(val icons: List<Icon>, val requiredUser: User) : SettingsViewState()
    data class CoversRetrieved(val covers: List<Cover>, val requiredUser: User) :
        SettingsViewState()


}