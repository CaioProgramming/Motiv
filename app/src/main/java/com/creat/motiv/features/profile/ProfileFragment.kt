package com.creat.motiv.features.profile

import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.creat.motiv.R
import com.creat.motiv.databinding.FragmentProfileBinding
import com.creat.motiv.features.home.adapter.QuoteAction
import com.creat.motiv.features.home.adapter.QuoteRecyclerAdapter
import com.creat.motiv.features.profile.alerts.CoverPickerDialog
import com.creat.motiv.features.profile.alerts.IconPickerDialog
import com.creat.motiv.features.profile.viewmodel.ProfileData
import com.creat.motiv.features.profile.viewmodel.ProfileViewModel
import com.creat.motiv.features.profile.viewmodel.ProfileViewState
import com.creat.motiv.features.share.QuoteShareDialog
import com.ilustris.animations.fadeIn
import com.ilustris.animations.fadeOut
import com.ilustris.motiv.base.beans.User
import com.ilustris.motiv.base.beans.quote.Quote
import com.ilustris.motiv.base.beans.quote.QuoteAdapterData
import com.ilustris.motiv.base.beans.quote.QuoteListViewState
import com.ilustris.motiv.base.databinding.QuoteRecyclerBinding
import com.ilustris.motiv.base.dialog.BottomSheetAlert
import com.ilustris.motiv.base.dialog.DefaultAlert
import com.ilustris.motiv.base.dialog.listdialog.ListDialog
import com.ilustris.motiv.base.dialog.listdialog.dialogItems
import com.ilustris.motiv.base.utils.PagerStackTransformer
import com.ilustris.motiv.base.utils.loadGif
import com.ilustris.motiv.base.utils.loadImage
import com.silent.ilustriscore.core.model.DataException
import com.silent.ilustriscore.core.model.ErrorType
import com.silent.ilustriscore.core.model.ViewModelBaseState
import com.silent.ilustriscore.core.utilities.DialogStyles
import com.silent.ilustriscore.core.utilities.gone
import com.silent.ilustriscore.core.utilities.showSnackBar
import com.silent.ilustriscore.core.utilities.visible
import java.text.NumberFormat

class ProfileFragment : Fragment() {

    private val args: ProfileFragmentArgs? by navArgs()
    private var quoteRecyclerAdapter = QuoteRecyclerAdapter(ArrayList(), ::selectQuote)
    var fragmentProfileBinding: FragmentProfileBinding? = null
    val profileViewModel = ProfileViewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentProfileBinding = FragmentProfileBinding.inflate(inflater)
        return fragmentProfileBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        profileViewModel.fetchUserPage(args?.uid)
    }

    private fun selectQuote(quoteAdapterData: QuoteAdapterData, quoteAction: QuoteAction) {
        when (quoteAction) {
            QuoteAction.OPTIONS -> profileViewModel.getQuoteOptions(quoteAdapterData)
            QuoteAction.LIKE -> likeQuote(quoteAdapterData.quote)
            QuoteAction.USER -> {
                navigateToProfile(quoteAdapterData.user.uid)
            }
        }
    }

    private fun likeQuote(quote: Quote) {
        profileViewModel.likeQuote(quote)
    }

    private fun navigateToProfile(uid: String) {
        val bundle = bundleOf("uid" to uid)
        findNavController().navigate(R.id.action_navigation_home_to_navigation_profile, bundle)
    }

    private fun QuoteRecyclerBinding.setupQuotes(quotedata: QuoteAdapterData) {
        if (quotesrecyclerview.adapter == null) {
            quotesrecyclerview.run {
                adapter = quoteRecyclerAdapter
                offscreenPageLimit = 3
                setPageTransformer(PagerStackTransformer(3))
            }
        }
        loading.fadeOut()
        quoteRecyclerAdapter.refreshData(quotedata)
        quotesrecyclerview.visible()
        quotesrecyclerview.run {
            visible()
        }
    }

    private fun openOptions(dialogItems: dialogItems) {
        ListDialog(
            requireContext(), dialogItems,
            { index, dialogItem ->
                dialogItem.action.invoke()
            }, DialogStyles.BOTTOM_NO_BORDER
        ).buildDialog()
    }

    private fun FragmentProfileBinding.setupProfile(profileData: ProfileData) {
        if (profileData.user.admin) profilepic.borderColor =
            ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark)
        username.text = profileData.user.name
        profilepic.loadImage(profileData.user.picurl)
        usercover.loadGif(profileData.user.cover)
        postsCount.run {
            tabTitle.text = "Posts"
            root.setOnClickListener {
                //quotesView.loading.fadeIn()
                quoteRecyclerAdapter.clearAdapter()
                profileViewModel.fetchPosts(profileData.posts)
                itemCount.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorPrimary
                    )
                )
                fragmentProfileBinding?.likesCount?.itemCount?.setTextColor(username.currentTextColor)

            }
            itemCount.counterAnimation(profileData.posts.size)
        }
        likesCount.run {
            tabTitle.text = "Favoritos"
            root.setOnClickListener {
                //quotesView.loading.fadeIn()
                quoteRecyclerAdapter.clearAdapter()
                profileViewModel.fetchPosts(profileData.posts)
                itemCount.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorPrimary
                    )
                )
                fragmentProfileBinding?.postsCount?.itemCount?.setTextColor(username.currentTextColor)
            }
            itemCount.counterAnimation(profileData.likes.size)

        }
        backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        optionsButton.setOnClickListener {
            val bundle = bundleOf("user" to profileData.user)
            findNavController().navigate(
                R.id.action_navigation_profile_to_navigation_settings,
                bundle
            )
        }
        profileTop.fadeIn()
        postsCount.root.callOnClick()
        if (profileData.isOwner) {
            profilepic.setOnClickListener {
                profileViewModel.requestIcons(profileData.user)
            }
            usercover.setOnClickListener {
                profileViewModel.requestCovers(profileData.user)
            }
            optionsButton.visible()
        } else {
            optionsButton.gone()
        }

    }

    private fun TextView.counterAnimation(count: Int) {
        val animator = ValueAnimator()
        animator.run {
            setObjectValues(0, count)
            addUpdateListener {
                text = NumberFormat.getInstance().format(it.animatedValue)
            }
            duration = 5000
            start()
        }
    }

    private fun observeViewModel() {
        profileViewModel.quoteListViewState.observe(viewLifecycleOwner) {
            when (it) {
                is QuoteListViewState.QuoteDataRetrieve -> {
                    fragmentProfileBinding?.quotesView?.setupQuotes(it.quotedata)
                }
                is QuoteListViewState.QuoteOptionsRetrieve -> openOptions(it.dialogItems)
                is QuoteListViewState.RequestDelete -> {
                    BottomSheetAlert(
                        requireContext(),
                        "Remover Post?",
                        "Você está prestes a deletar, seu post", {
                            profileViewModel.deleteQuote(it.quote.id)
                            profileViewModel.fetchUserPage(args?.uid)
                        }
                    ).buildDialog()
                }
                is QuoteListViewState.RequestEdit -> navigateToNewQuote(it.quote)
                is QuoteListViewState.RequestReport -> {
                    DefaultAlert(
                        requireContext(),
                        "Denúnciar publicação",
                        "Você deseja denúnciar essa publicação? Vamos analisá-la e tomar as ações necessárias!",
                        okClick = {
                            view?.showSnackBar("Denúncia enviada com sucesso!")
                        }
                    ).buildDialog()
                }
                is QuoteListViewState.RequestShare -> {
                    QuoteShareDialog(requireContext(), it.quoteShareData).buildDialog()
                }
            }

        }
        profileViewModel.profileViewState.observe(viewLifecycleOwner) {
            when (it) {
                is ProfileViewState.ProfilePageRetrieve -> {
                    fragmentProfileBinding?.setupProfile(it.profileData)
                }
                is ProfileViewState.CoversRetrieved -> {
                    CoverPickerDialog(requireContext(), it.covers) { cover ->
                        profileViewModel.updateUserCover(it.requiredUser, cover.url)
                    }.buildDialog()
                }
                is ProfileViewState.IconsRetrieved -> {
                    IconPickerDialog(requireContext(), it.icons) { icon ->
                        profileViewModel.updateUserPic(it.requiredUser, icon.uri)
                    }.buildDialog()
                }
            }
        }
        profileViewModel.viewModelState.observe(viewLifecycleOwner) {
            when (it) {
                is ViewModelBaseState.DataUpdateState -> {
                    val user = it.data as User
                    profileViewModel.fetchUserPage(user.uid)
                }
                is ViewModelBaseState.ErrorState -> {
                    handleError(it.dataException)
                    view?.showSnackBar(it.dataException.code.message, backColor = Color.RED)
                }
            }
        }
    }

    private fun handleError(dataException: DataException) {
        when (dataException.code) {
            ErrorType.NOT_FOUND -> {

            }

        }
    }

    private fun navigateToNewQuote(quote: Quote? = null) {
        val bundle = bundleOf("quote" to quote)
        findNavController().navigate(R.id.navigation_new_quote, bundle)
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentProfileBinding = null
    }
}