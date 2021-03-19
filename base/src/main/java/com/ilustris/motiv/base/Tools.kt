package com.ilustris.motiv.base

import kotlin.random.Random


object Tools {
    val TEST_DEVICES = listOf("0B05DB678D8A74AECB7F8E90C0AC97B5")


    private val offlineStatusMessage = arrayOf("Sem internet é? Acontece né vá aproveitar a vida",
            "Parece que você está desconectado. Quando você se reconectar eu mostro umas frases",
            "Bom... Parece que você tá sem internet, então acho que não tem oque fazer...")

    val empyquotes = listOf("Você não vai escrever nada? Tá achando que é festa?",
            "O vazio da sua existência não necessariamente precisa ser o vazio do bloco de texto, escreva algo!",
            "Você não quer ver o feed e ver um texto vazio né? Então por favor escreve algo aí",
            "Ah qual é qual a necessidade de não escrever nada?")


    fun emptyQuote(): String {
        val x = Random.nextInt(empyquotes.size)
        return empyquotes[x]
    }


    fun offlineMessage(): String {
        val x = Random.nextInt(offlineStatusMessage.size)
        return offlineStatusMessage[x]
    }


}
