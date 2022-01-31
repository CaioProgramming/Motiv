package com.creat.motiv.features.profile.viewmodel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.UserProfileChangeRequest
import com.ilustris.motiv.base.beans.Cover
import com.ilustris.motiv.base.beans.Icon
import com.ilustris.motiv.base.beans.Style
import com.ilustris.motiv.base.beans.User
import com.ilustris.motiv.base.beans.quote.*
import com.ilustris.motiv.base.dialog.listdialog.DialogData
import com.ilustris.motiv.base.service.*
import com.silent.ilustriscore.core.model.BaseViewModel
import com.silent.ilustriscore.core.model.DataException
import com.silent.ilustriscore.core.model.ViewModelBaseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProfileViewModel(application: Application) : BaseViewModel<User>(application) {

    private val fontsService = FontsService(application.applicationContext)
    override val service = UserService()
    private val quoteService = QuoteService()
    private val styleService = StyleService()
    private val iconsService = IconsService()
    private val coversService = CoverService()
    val profileViewState = MutableLiveData<ProfileViewState>()
    val quoteListViewState = MutableLiveData<QuoteListViewState>()

    fun fetchUserPage(uid: String? = getUser()?.uid) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userRequest = service.getSingleData(uid!!)
                if (userRequest.isError) {
                    viewModelState.postValue(ViewModelBaseState.ErrorState(userRequest.error.errorException))
                }
                val user = userRequest.success.data as User
                val postsRequest = quoteService.query(uid, "userID")
                val posts = ArrayList<Quote>()
                val favoritePosts = ArrayList<Quote>()
                if (postsRequest.isSuccess) {
                    posts.addAll(postsRequest.success.data as quoteList)
                }
                val favoritesRequest = quoteService.findOnArray("likes", uid)
                if (favoritesRequest.isSuccess) {
                    favoritePosts.addAll(favoritesRequest.success.data as quoteList)
                }
                profileViewState.postValue(
                    ProfileViewState.ProfilePageRetrieve(
                        ProfileData(
                            user = user,
                            ArrayList(favoritePosts.sortedByDescending { it.data }),
                            posts = ArrayList(posts.sortedByDescending { it.data }),
                            user.uid == getUser()?.uid
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
                val quoteUser = if (quote.userID == getUser()?.uid) {
                    User.fromFirebase(getUser()!!)
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
                requestFontToRetrieveQuote(
                    QuoteAdapterData(
                        quote,
                        style,
                        quoteUser,
                        getUser(),
                        likeList
                    )
                )
            }
        }
    }

    private suspend fun requestFontToRetrieveQuote(
        quoteAdapterData: QuoteAdapterData,

        ) {
        fontsService.requestDownload(
            fontsService.getFamilyName(quoteAdapterData.style.font)
        ) { tpface, s ->
            Log.i(javaClass.simpleName, "requestFontToRetrieveQuote: $s")
            quoteAdapterData.style.typeface = tpface
            quoteListViewState.postValue(
                QuoteListViewState.QuoteDataRetrieve(
                    quoteAdapterData
                )
            )
        }
    }

    fun likeQuote(quote: Quote) {
        viewModelScope.launch(Dispatchers.IO) {
            getUser()?.let {
                if (quote.likes.contains(it.uid)) quote.likes.remove(it.uid) else quote.likes.add(it.uid)
                quoteService.editData(quote)
            }
        }

    }

    fun getQuoteOptions(quoteAdapterData: QuoteAdapterData) {
        val dialogItems = ArrayList<DialogData>()
        val quote = quoteAdapterData.quote
        if (quote.userID == getUser()?.uid) {
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

    fun deleteQuote(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            quoteService.deleteData(id)
        }
    }

    fun requestIcons(requiredUser: User) {
        viewModelScope.launch(Dispatchers.IO) {
            val icons = iconsService.getAllData().success.data as ArrayList<Icon>
            profileViewState.postValue(ProfileViewState.IconsRetrieved(icons, requiredUser))
        }
    }

    fun requestCovers(requiredUser: User) {
        viewModelScope.launch(Dispatchers.IO) {
            val covers = coversService.getAllData().success.data as ArrayList<Cover>
            profileViewState.postValue(ProfileViewState.CoversRetrieved(covers, requiredUser))
        }
    }

    fun updateUserPic(user: User, uri: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val profileChangeRequest =
                    UserProfileChangeRequest.Builder().setPhotoUri(Uri.parse(uri)).build()
                val request = getUser()?.updateProfile(profileChangeRequest)?.await()
                val editFieldRequest = service.editField(uri, user.id, "picurl")
                viewModelState.postValue(ViewModelBaseState.DataUpdateState(user.apply {
                    picurl = uri
                }))
            } catch (e: Exception) {
                e.printStackTrace()
                viewModelState.postValue(ViewModelBaseState.ErrorState(DataException.UNKNOWN))
            }
        }

    }

    fun updateUserCover(user: User, uri: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val editFieldRequest = service.editField(uri, user.id, "cover")
            viewModelState.postValue(ViewModelBaseState.DataUpdateState(user.apply {
                cover = uri
            }))
        }
    }

}