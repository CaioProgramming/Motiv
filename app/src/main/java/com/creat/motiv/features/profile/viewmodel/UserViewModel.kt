package com.creat.motiv.features.profile.viewmodel

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
import com.silent.ilustriscore.core.model.ViewModelBaseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.ArrayList

class UserViewModel : BaseViewModel<User>() {

    override val service = UserService()
    private val quoteService = QuoteService()
    private val styleService = StyleService()
    val userViewState = MutableLiveData<ProfileViewState>()

    fun fetchUserPage(uid: String? = currentUser?.uid) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userRequest = service.getSingleData(uid!!)
                val user = userRequest.success.data as User
                val postsRequest = quoteService.query(uid, "userID")
                val posts = postsRequest.success.data as quoteList
                val favoritesRequest = quoteService.findOnArray("likes", uid)
                val favoritePosts = favoritesRequest.success.data as quoteList
                userViewState.postValue(
                    ProfileViewState.ProfilePageRetrieve(
                        ProfileData(
                            user = user,
                            favoritePosts,
                            posts = posts
                        )
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
                viewModelState.postValue(ViewModelBaseState.ErrorState(DataException.UNKNOWN))
            }
        }


    }

    fun fetchPosts(quotes: ArrayList<Quote>) {
        viewModelScope.launch {
            quotes.forEachIndexed { index, quote ->

                val styleRequest = styleService.getSingleData(quote.style)
                val style =
                    if (styleRequest.isError) Style.defaultStyle else styleRequest.success.data as Style
                val quoteUser = if (quote.userID == currentUser?.uid) {
                    User.fromFirebase(currentUser!!)
                } else {
                    service.getSingleData(quote.userID).success.data as User
                }
                val likeList = ArrayList<User>()
                quote.likes.forEach { uid ->
                    val userRequest = service.getSingleData(uid)
                    Log.d("User Service", "\nRequest -> $userRequest\n")
                    if (userRequest.isSuccess) {
                        val user = userRequest.success.data as User
                        likeList.add(user)
                    }

                }
                userViewState.postValue(
                    ProfileViewState.RetrieveQuote(
                        QuoteAdapterData(quote, style, quoteUser, currentUser, likeList)
                    )
                )
            }

        }
    }

    fun likeQuote(quote: Quote) {
        viewModelScope.launch(Dispatchers.IO) {
            currentUser?.let {
                if (quote.likes.contains(it.uid)) quote.likes.remove(it.uid) else quote.likes.add(it.uid)
                quoteService.editData(quote)
            }
        }

    }

    fun getQuoteOptions(quoteAdapterData: QuoteAdapterData) {
        val dialogItems = ArrayList<DialogData>()
        val quote = quoteAdapterData.quote
        if (quote.userID == currentUser?.uid) {
            dialogItems.add(DialogData("Excluir") {
                userViewState.value = ProfileViewState.RequestDelete(quote)
            })
            dialogItems.add(DialogData("Editar") {
                userViewState.value = ProfileViewState.RequestEdit(quote)
            })
        }
        dialogItems.add(DialogData("Compartilhar") {
            userViewState.value =
                ProfileViewState.RequestShare(QuoteShareData(quote, quoteAdapterData.style))
        })
        dialogItems.add(DialogData("Denúnciar") {
            userViewState.value = ProfileViewState.RequestReport(quote)
        })

        userViewState.postValue(ProfileViewState.QuoteOptionsRetrieve(dialogItems.toList()))
    }

    fun deleteQuote(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            quoteService.deleteData(id)
        }
    }

}