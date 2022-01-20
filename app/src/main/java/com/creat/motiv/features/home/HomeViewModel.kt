package com.creat.motiv.features.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.creat.motiv.features.share.QuoteShareData
import com.ilustris.motiv.base.beans.*
import com.ilustris.motiv.base.dialog.listdialog.DialogData
import com.ilustris.motiv.base.service.QuoteService
import com.ilustris.motiv.base.service.StyleService
import com.ilustris.motiv.base.service.UserService
import com.silent.ilustriscore.core.model.BaseViewModel
import com.silent.ilustriscore.core.model.DataException
import com.silent.ilustriscore.core.model.ErrorType
import com.silent.ilustriscore.core.model.ViewModelBaseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat

class HomeViewModel : BaseViewModel<Quote>() {
    override val service = QuoteService()
    private val styleService = StyleService()
    private val userService = UserService()

    val homeViewState = MutableLiveData<HomeViewState>()
    val quoteListViewState = MutableLiveData<QuoteListViewState>()

    fun fetchQuoteOptions(quoteAdapterData: QuoteAdapterData) {
        val dialogItems = ArrayList<DialogData>()
        val quote = quoteAdapterData.quote
        if (quote.userID == currentUser?.uid) {
            dialogItems.add(DialogData("Excluir") {
                quoteListViewState.value = QuoteListViewState.RequestDelete(quote)
            })
            dialogItems.add(DialogData("Editar") {
                quoteListViewState.value = QuoteListViewState.RequestEdit(quote)
            })
        }
        dialogItems.add(DialogData("Compartilhar") {
            quoteListViewState.value =
                QuoteListViewState.RequestShare(QuoteShareData(quote, quoteAdapterData.style))
        })
        dialogItems.add(DialogData("Denúnciar") {
            quoteListViewState.value = QuoteListViewState.RequestReport(quote)
        })

        quoteListViewState.postValue(QuoteListViewState.QuoteOptionsRetrieve(dialogItems.toList()))
    }

    fun fetchUser() {
        viewModelScope.launch {
            if (currentUser == null) {
                viewModelState.postValue(ViewModelBaseState.ErrorState(DataException.AUTH))
            } else {
                val userRequest = userService.getSingleData(currentUser!!.uid)
                if (userRequest.isError) {
                    if (userRequest.error.errorException.code == ErrorType.NOT_FOUND) {
                        val tokenRequest = currentUser!!.getIdToken(false).await()
                        val user = User.fromFirebase(currentUser!!).apply {
                            tokenRequest.token?.let {
                                token = it
                            }
                        }
                        userService.editData(User.fromFirebase(currentUser!!))
                    } else {
                        viewModelState.postValue(ViewModelBaseState.ErrorState(DataException.NOTFOUND))
                    }
                } else {
                    homeViewState.postValue(HomeViewState.UserRetrieved(userRequest.success.data as User))
                }
            }
        }
    }

    fun getHomeQuotes() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val quoteRequest = service.getAllData()
                print(quoteRequest)
                if (quoteRequest.isError) {
                    viewModelState.postValue(ViewModelBaseState.ErrorState(quoteRequest.error.errorException))
                    return@launch
                }
                val quotes = (quoteRequest.success.data as quoteList).sortedByDescending { it.data }
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

    fun favoriteQuote(quote: Quote) {
        currentUser?.let {
            if (quote.likes.contains(it.uid)) quote.likes.remove(it.uid) else quote.likes.add(it.uid)
            editData(quote)
        }
    }

}