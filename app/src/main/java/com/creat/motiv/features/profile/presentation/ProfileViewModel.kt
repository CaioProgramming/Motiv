package com.creat.motiv.features.profile.presentation

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.creat.motiv.features.home.presentation.ShareState
import com.google.firebase.Timestamp
import com.ilustris.motiv.base.data.model.Quote
import com.ilustris.motiv.base.data.model.QuoteDataModel
import com.ilustris.motiv.base.data.model.Report
import com.ilustris.motiv.base.data.model.User
import com.ilustris.motiv.base.service.QuoteService
import com.ilustris.motiv.base.service.UserService
import com.ilustris.motiv.base.service.helper.QuoteHelper
import com.ilustris.motivcompose.features.profile.ui.ProfileFilter
import com.silent.ilustriscore.core.model.BaseViewModel
import com.silent.ilustriscore.core.model.ViewModelBaseState
import com.silent.ilustriscore.core.utilities.delayedFunction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    application: Application,
    private val quoteHelper: QuoteHelper,
    private val quoteService: QuoteService,
    override val service: UserService
) :
    BaseViewModel<User>(application) {

    val user = MutableLiveData<User>(null)
    val userQuotes = mutableStateListOf<QuoteDataModel>()
    val userFavorites = mutableStateListOf<QuoteDataModel>()
    val postsCount = MutableLiveData(0)
    val favoriteCount = MutableLiveData(0)
    val isOwnUser = MutableLiveData(false)
    val shareState = MutableLiveData<ShareState>()


    fun fetchUser(uid: String? = null) {
        viewModelState.postValue(ViewModelBaseState.LoadingState)
        viewModelScope.launch(Dispatchers.IO) {
            val id = if (uid.isNullOrBlank()) getUser()?.uid else uid
            id?.let {
                val userRequest = service.getSingleData(it)
                if (userRequest.isSuccess) {
                    user.postValue(userRequest.success.data as User)
                    isOwnUser.postValue(it == getUser()?.uid)
                } else {
                    sendErrorState(userRequest.error.errorException)
                }
            } ?: run {
                Log.e(javaClass.simpleName, "fetchUser: no user logged")
            }
        }
    }

    fun getUserQuotes(uid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            quoteService.query(uid, "userID").run {
                if (isSuccess) {
                    val quotes = (success.data as List<Quote>).sortedByDescending { it.data }
                    postsCount.postValue(quotes.size)
                    loadQuoteListExtras(quotes)
                } else {
                    sendErrorState(error.errorException)
                }
            }
        }
    }

    fun getUserFavorites(uid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            quoteService.queryOnArray(uid, "likes").run {

            }
            quoteService.getFavorites(uid).run {
                if (isSuccess) {
                    val favoriteQuotes =
                        (success.data as List<Quote>).sortedByDescending { it.data }
                    favoriteCount.postValue(favoriteQuotes.size)
                    loadQuoteListExtras(favoriteQuotes, true)
                } else {
                    sendErrorState(error.errorException)
                }
            }
        }
    }

    private suspend fun loadQuoteListExtras(
        quotesDataList: List<Quote>,
        isFavorite: Boolean = false
    ) {
        quotesDataList.map {
            quoteHelper.mapQuoteToQuoteDataModel(it).run {
                if (this.isSuccess) {
                    if (!isFavorite) {
                        userQuotes.add(success.data)
                    } else {
                        userFavorites.add(success.data)
                    }
                } else {
                    sendErrorState(error.errorException)
                }
            }
        }
    }


    private fun refreshUser() {
        viewModelState.postValue(ViewModelBaseState.LoadingState)
        user.postValue(null)
        delayedFunction(500) {
            fetchUser()
        }
    }

    fun deleteQuote(quoteDataModel: QuoteDataModel, filter: ProfileFilter) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = quoteService.deleteData(quoteDataModel.quoteBean.id)
            if (result.isSuccess) {
                if (filter == ProfileFilter.POSTS) {
                    userQuotes.remove(quoteDataModel)
                } else {
                    userFavorites.remove(quoteDataModel)
                }
            } else {
                sendErrorState(result.error.errorException)
            }
        }
    }

    fun likeQuote(quoteDataModel: QuoteDataModel, filter: ProfileFilter) {
        viewModelScope.launch(Dispatchers.IO) {
            val quote = quoteDataModel.quoteBean
            getUser()?.let {
                if (quote.likes.contains(it.uid)) {
                    quote.likes.remove(it.uid)
                } else {
                    quote.likes.add(it.uid)
                }
                val result = quoteService.editData(quote)
                if (result.isSuccess) {
                    if (filter == ProfileFilter.POSTS) {
                        userQuotes[userQuotes.indexOf(quoteDataModel)] = quoteDataModel.copy(
                            quoteBean = quote,
                            isFavorite = quote.likes.contains(it.uid)
                        )
                    } else {
                        val isFavorite = quote.likes.contains(it.uid)
                        if (isFavorite) {
                            userFavorites[userFavorites.indexOf(quoteDataModel)] =
                                quoteDataModel.copy(
                                    quoteBean = quote,
                                    isFavorite = quote.likes.contains(it.uid)
                                )
                        } else {
                            userFavorites.remove(quoteDataModel)
                        }
                    }
                } else {
                    sendErrorState(result.error.errorException)
                }
            }
        }
    }

    fun reportQuote(quote: Quote, reason: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getUser()?.let {
                quote.reports.add(Report(it.uid, reason, Timestamp.now()))
                quoteService.editData(quote)
            }
        }
    }

    fun shareQuote(quoteBean: Quote, bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            quoteHelper.generateQuoteImage(
                getApplication<Application>().applicationContext,
                quoteBean,
                bitmap
            ).run {
                if (this.isSuccess) {
                    shareState.postValue(ShareState.ShareSuccess(success.data, quoteBean))
                } else {
                    shareState.postValue(ShareState.ShareError)
                }
            }
        }
    }

}