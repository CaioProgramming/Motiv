package com.ilustris.motiv.manager.styles.utils

object StyleUtils {

    private val exampleQuotes = listOf(
        "I try to change but I don't know how\nI hit the brakes but can't slow down.",
        "I'm the greatest love that you wasted.",
        "How's the castle buit off people you pretend to care about?",
        "Não te achei em mais ninguém\nmas era óbvio que não ia funcionar.",
        "A gente já ta um tempo junto, fico no escuro\ntudo meio turvo tudo meio mudo."
    )

    fun getExampleQuote(currentQuote: String? = null) =
        if (currentQuote == null) exampleQuotes.random() else exampleQuotes.filter { it != currentQuote }
            .random()

}