package com.creat.motiv.features

import com.ilustris.motiv.base.beans.Radio

sealed class MainViewState {

    data class PlayRadio(val radio: Radio) : MainViewState()
    data class PauseRadio(val radio: Radio) : MainViewState()
}