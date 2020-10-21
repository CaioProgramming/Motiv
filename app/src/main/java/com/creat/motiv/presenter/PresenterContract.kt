package com.creat.motiv.presenter

interface PresenterContract<T> {

    fun onLoading()
    fun onLoadFinish()
    fun onDataRetrieve(data: List<T>)
    fun onSingleData(data: T)
    fun onError()
    fun onSuccess()
}