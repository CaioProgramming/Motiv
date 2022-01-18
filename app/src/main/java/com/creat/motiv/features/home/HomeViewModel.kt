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

    fun fetchQuoteOptions(quoteAdapterData: QuoteAdapterData) {
        val dialogItems = ArrayList<DialogData>()
        val quote = quoteAdapterData.quote
        if (quote.userID == currentUser?.uid) {
            dialogItems.add(DialogData("Excluir") {
                homeViewState.value = HomeViewState.RequestDelete(quote)
            })
            dialogItems.add(DialogData("Editar") {
                homeViewState.value = HomeViewState.RequestEdit(quote)
            })
        }
        dialogItems.add(DialogData("Compartilhar") {
            homeViewState.value =
                HomeViewState.RequestShare(QuoteShareData(quote, quoteAdapterData.style))
        })
        dialogItems.add(DialogData("Denúnciar") {
            homeViewState.value = HomeViewState.RequestReport(quote)
        })

        homeViewState.postValue(HomeViewState.QuoteOptionsRetrieve(dialogItems.toList()))
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
                        throw Exception("Unknow error fetching the user")
                    }
                } else {
                    homeViewState.postValue(HomeViewState.UserRetrieved(userRequest.success.data as User))
                }
            }
        }
    }

    fun getUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            val users = userService.getAllData().success
        }
    }

    fun requestDelete() {

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
                val quotes =
                    (quoteRequest.success.data as quoteList).sortedByDescending { it.data }
                val splashQuote = Quote.splashQuote()
                splashQuote.author = "Mais de ${quotes.size - 1} publicações"
                splashQuote.data = SimpleDateFormat("dd/MM/yyyy").parse("19/12/2018")
                val adapterSplash = QuoteAdapterData(splashQuote, Style.splashStyle)
                adapterSplash.user = User.splashUser
                homeViewState.postValue(
                    HomeViewState.QuoteDataRetrieve(
                        adapterSplash
                    )
                )
                quotes.forEachIndexed { index, quote ->
                    if (index >= 20 && index % 20 == 0) {
                        val usersRequest = userService.getAllData()
                        if (usersRequest.isSuccess) {
                            val users = usersRequest.success.data as ArrayList<User>
                            homeViewState.postValue(
                                HomeViewState.QuoteDataRetrieve(
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
                    homeViewState.postValue(
                        HomeViewState.QuoteDataRetrieve(
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

    fun getSearchQuotes() {
        viewModelScope.launch {
            val quotes = service.getAllData() as quoteList
            val quoteDatas = ArrayList<QuoteAdapterData>()
            quoteDatas.add(QuoteAdapterData(Quote.searchQuote(), Style.searchStyle))
            quotes.forEach {
                val style = styleService.getSingleData(it.style).success.data as Style
                val quoteUser = if (it.userID == currentUser?.uid) {
                    User.fromFirebase(currentUser!!)
                } else {
                    userService.getSingleData(it.userID).success.data as User
                }
                quoteDatas.add(QuoteAdapterData(it, style, quoteUser))
                homeViewState.postValue(
                    HomeViewState.QuoteDataRetrieve(
                        QuoteAdapterData(it, style, quoteUser)
                    )
                )

            }
        }

    }

    fun getProfileQuotes() {
        viewModelScope.launch {
            val quotes = service.getAllData() as quoteList
            val quoteDatas = ArrayList<QuoteAdapterData>()
            quoteDatas.add(QuoteAdapterData(Quote.profileQuote(), Style.splashStyle))
            quotes.forEach {
                val style = styleService.getSingleData(it.style).success.data as Style
                val quoteUser = if (it.userID == currentUser?.uid) {
                    User.fromFirebase(currentUser!!)
                } else {
                    userService.getSingleData(it.userID).success.data as User
                }
                homeViewState.postValue(
                    HomeViewState.QuoteDataRetrieve(
                        QuoteAdapterData(it, style, quoteUser)
                    )
                )
            }
        }

    }

    fun getFavoriteQuotes() {
        viewModelScope.launch {
            val quotes = service.getAllData() as quoteList
            val quoteDatas = ArrayList<QuoteAdapterData>()
            quoteDatas.add(QuoteAdapterData(Quote.splashQuote(), Style.splashStyle))
            quotes.forEach {
                val style = styleService.getSingleData(it.style).success.data as Style
                val quoteUser = if (it.userID == currentUser?.uid) {
                    User.fromFirebase(currentUser!!)
                } else {
                    userService.getSingleData(it.userID).success.data as User
                }
                homeViewState.postValue(
                    HomeViewState.QuoteDataRetrieve(
                        QuoteAdapterData(it, style, quoteUser)
                    )
                )
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