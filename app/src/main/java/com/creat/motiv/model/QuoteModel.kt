package com.creat.motiv.model

import com.creat.motiv.model.Beans.Quote
import com.creat.motiv.presenter.BasePresenter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class QuoteModel(override val presenter: BasePresenter<Quote>) : BaseModel<Quote>() {


    override val path: String = "Quotes"


    fun getFavorites() {
        reference().addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val quotesList: ArrayList<Quote> = ArrayList()
                for (snap in snapshot.children) {
                    if (snap.child("likes").hasChild(currentUser!!.uid)) {
                        val quote = Quote().convertSnapshot(snap)
                        quote?.let {
                            quotesList.add(it)
                        }
                    }
                }
                presenter.onDataRetrieve(quotesList)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
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

    override fun deserializeDataSnapshot(dataSnapshot: DataSnapshot): Quote? {
        return Quote().convertSnapshot(dataSnapshot)
    }


}
