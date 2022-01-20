package com.ilustris.motiv.manager.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ilustris.motiv.base.beans.Style
import com.ilustris.motiv.base.beans.User
import com.ilustris.motiv.base.beans.quote.*
import com.ilustris.motiv.base.dialog.listdialog.DialogData
import com.ilustris.motiv.base.service.QuoteService
import com.ilustris.motiv.base.service.StyleService
import com.ilustris.motiv.base.service.UserService
import com.silent.ilustriscore.core.model.BaseViewModel
import com.silent.ilustriscore.core.model.DataException
import com.silent.ilustriscore.core.model.ViewModelBaseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class ManagerViewModel : BaseViewModel<Quote>() {
    override val service = QuoteService()
    private val userService = UserService()
    private val styleService = StyleService()
    val quoteListViewState = MutableLiveData<QuoteListViewState>()

    fun getQuotes() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val quoteRequest = service.getAllData()
                print(quoteRequest)
                if (quoteRequest.isError) {
                    viewModelState.postValue(ViewModelBaseState.ErrorState(quoteRequest.error.errorException))
                    return@launch
                }
                val quotes =
                    (quoteRequest.success.data as quoteList).sortedByDescending { it.isReport }
                val splashQuote = Quote.splashQuote()
                splashQuote.author = "Mais de ${quotes.size - 1} publicações"
                splashQuote.data = SimpleDateFormat("dd/MM/yyyy").parse("19/12/2018")
                val adapterSplash = QuoteAdapterData(splashQuote, Style.splashStyle)
                adapterSplash.user = User.splashUser
                quoteListViewState.postValue(
                    QuoteListViewState.QuoteDataRetrieve(
                        adapterSplash
                    )
                )
                quotes.forEachIndexed { index, quote ->
                    if (index >= 20 && index % 20 == 0) {
                        val usersRequest = userService.getAllData()
                        if (usersRequest.isSuccess) {
                            val users = usersRequest.success.data as ArrayList<User>
                            quoteListViewState.postValue(
                                QuoteListViewState.QuoteDataRetrieve(
                                    QuoteAdapterData(Quote.usersQuote(), users = users)
                                )
                            )
                        }

                    }
                    val styleRequest = styleService.getSingleData(quote.style)
                    val style =
                        if (styleRequest.isError) Style.defaultStyle else styleRequest.success.data as Style
                    val quoteUser = if (quote.userID == currentUser?.uid) {
                        User.fromFirebase(currentUser!!)
                    } else {
                        userService.getSingleData(quote.userID).success.data as User
                    }
                    val likeList = ArrayList<User>()
                    quote.likes.forEach { uid ->
                        val userRequest = userService.getSingleData(uid)
                        Log.d("User Service", "\nRequest -> $userRequest\n")
                        if (userRequest.isSuccess) {
                            val user = userRequest.success.data as User
                            likeList.add(user)
                        }

                    }
                    quoteListViewState.postValue(
                        QuoteListViewState.QuoteDataRetrieve(
                            QuoteAdapterData(quote, style, quoteUser, currentUser, likeList)
                        )
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                viewModelState.postValue(ViewModelBaseState.ErrorState(DataException.UNKNOWN))
            }
        }

    }

    fun fetchQuoteOptions(quoteAdapterData: QuoteAdapterData) {
        val dialogItems = ArrayList<DialogData>()
        val quote = quoteAdapterData.quote
        dialogItems.add(DialogData("Excluir") {
            quoteListViewState.value = QuoteListViewState.RequestDelete(quote)
        })
        dialogItems.add(DialogData("Compartilhar") {
            quoteListViewState.value =
                QuoteListViewState.RequestShare(QuoteShareData(quote, quoteAdapterData.style))
        })
        quoteListViewState.postValue(QuoteListViewState.QuoteOptionsRetrieve(dialogItems.toList()))
    }

    fun report(quote: Quote) {
        quote.isReport = false
        editData(quote)
    }


}