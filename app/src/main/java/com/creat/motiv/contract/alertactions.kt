package com.creat.motiv.contract

abstract class AlertActions(){

    fun mainaction(){}
    abstract fun mainaction(function: () -> Unit)

}

