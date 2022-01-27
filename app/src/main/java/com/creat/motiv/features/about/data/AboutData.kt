package com.creat.motiv.features.about.data

import com.ilustris.motiv.base.beans.Developer

data class AboutData(
    var header: String? = null,
    var developers: List<Developer>? = null,
    var references: List<Reference>? = null
)