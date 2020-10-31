package com.creat.motiv.model.beans

import com.creat.motiv.R

data class Reference(val background: Int, val name: String, val description: String, val url: String) {


    companion object {

        val references = listOf<Reference>(
                Reference(
                        R.drawable.flaticon_gradient,
                        "Flaticon",
                        "Milhares de ícones gratuitos no maior banco de dados de icones vetoriais gratuitos.", "flaticon.com"),

                Reference(R.drawable.dribble_gradient,
                        "Dribble",
                        "A maior comunidade de designers, para compartilhar criações, inspirar-se e evoluir na carreira de design", "dribbble.com"),
                Reference(R.drawable.undraw_gradient,
                        "Undraw.co",
                        "O projeto de código aberto com diversas ilustrações para usar sua criatividade e criar incríveis websites, produtos e aplicativos a sua maneira.", "undraw.co"),
                Reference(
                        R.drawable.mixkit_gradient,
                        "MixKit.co",
                        "Uma galeria gratuita com incríveis clipes de vídeos, músicas, efeitos sonoros e ilustrações.", "mixkit.co"),
                Reference(
                        R.drawable.ouch_gradient,
                        "Icons8/Ouch",
                        "Milhares de ícones gratuitos no maior banco de dados de icones vetoriais.", "wwww.icons8/ouch")
        )


    }


}