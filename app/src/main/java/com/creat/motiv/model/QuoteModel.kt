package com.creat.motiv.model
import com.creat.motiv.model.Beans.Quote
import com.creat.motiv.presenter.BasePresenter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class QuoteModel(val qPresenter: BasePresenter<Quote>) : BaseModel<Quote>(qPresenter) {


    override fun reference(): DatabaseReference {
        return FirebaseDatabase.getInstance().reference.child("Quotes")
    }


    fun pesquisar(pesquisa: String) {
        query(pesquisa, "quote")
    }

    private fun PesquisarAuthor(pesquisa: String) {
        query(pesquisa, "author")
    }

    private fun PesquisarUsuario(pesquisa: String) {
        query(pesquisa, "username")
    }


    fun denunciar(quote: Quote) {
        quote.isReport = true
        editData(quote, quote.id)

    }


}
