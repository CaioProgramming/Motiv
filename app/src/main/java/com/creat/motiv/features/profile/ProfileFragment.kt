package com.creat.motiv.features.profile

import android.animation.ValueAnimator
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
import com.creat.motiv.databinding.ProfileTabBinding
import com.creat.motiv.databinding.QuoteRecyclerBinding
import com.creat.motiv.features.home.adapter.PagerStackTransformer
import com.creat.motiv.features.home.adapter.QuoteAction
import com.creat.motiv.features.home.adapter.QuoteRecyclerAdapter
import com.creat.motiv.features.profile.viewmodel.ProfileData
import com.creat.motiv.features.profile.viewmodel.UserViewModel
import com.creat.motiv.features.profile.viewmodel.ProfileViewState
import com.creat.motiv.features.share.QuoteShareDialog
import com.google.android.material.tabs.TabLayout
import com.ilustris.animations.fadeIn
import com.ilustris.animations.fadeOut
import com.ilustris.motiv.base.beans.Quote
import com.ilustris.motiv.base.beans.QuoteAdapterData
import com.ilustris.motiv.base.dialog.BottomSheetAlert
import com.ilustris.motiv.base.dialog.DefaultAlert
import com.ilustris.motiv.base.dialog.listdialog.ListDialog
import com.ilustris.motiv.base.dialog.listdialog.dialogItems
import com.ilustris.motiv.base.utils.loadGif
import com.ilustris.motiv.base.utils.loadImage
import com.silent.ilustriscore.core.utilities.DialogStyles
import com.silent.ilustriscore.core.utilities.gone
import com.silent.ilustriscore.core.utilities.showSnackBar
import com.silent.ilustriscore.core.utilities.visible
import java.text.NumberFormat

class ProfileFragment : Fragment() {

    private val args: ProfileFragmentArgs? by navArgs()
    private var quoteRecyclerAdapter = QuoteRecyclerAdapter(ArrayList(), ::selectQuote)
    var fragmentProfileBinding: FragmentProfileBinding? = null
    val userViewModel = UserViewModel()
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
        userViewModel.fetchUserPage(args?.uid)
    }

    private fun selectQuote(quoteAdapterData: QuoteAdapterData, quoteAction: QuoteAction) {
        when (quoteAction) {
            QuoteAction.OPTIONS -> userViewModel.getQuoteOptions(quoteAdapterData)
            QuoteAction.LIKE -> likeQuote(quoteAdapterData.quote)
            QuoteAction.USER -> {
                navigateToProfile(quoteAdapterData.user.uid)
            }
        }
    }


    private fun likeQuote(quote: Quote) {
        userViewModel.likeQuote(quote)
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
        profilepic.loadImage(profileData.user.picurl)
        username.text = profileData.user.name
        postsCount.run {
            tabTitle.text = "Posts"
            root.setOnClickListener {
                quotesView.loading.fadeIn()
                quoteRecyclerAdapter.clearAdapter()
                userViewModel.fetchPosts(profileData.posts)
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
                quotesView.loading.fadeIn()
                quoteRecyclerAdapter.clearAdapter()
                userViewModel.fetchPosts(profileData.posts)
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
        usercover.loadGif(profileData.user.cover)
        profileTop.fadeIn()
        postsCount.root.callOnClick()
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
        userViewModel.userViewState.observe(this, {
            when (it) {
                is ProfileViewState.ProfilePageRetrieve -> {
                    fragmentProfileBinding?.setupProfile(it.profileData)
                }
                is ProfileViewState.RetrieveQuote -> {
                    fragmentProfileBinding?.quotesView?.setupQuotes(it.quoteAdapterData)
                }
                is ProfileViewState.QuoteOptionsRetrieve -> openOptions(it.dialogItems)
                is ProfileViewState.RequestDelete -> {
                    BottomSheetAlert(
                        requireContext(),
                        "Remover Post?",
                        "Você está prestes a deletar, seu post", {
                            userViewModel.deleteQuote(it.quote.id)
                        }
                    ).buildDialog()
                }
                is ProfileViewState.RequestEdit -> navigateToNewQuote(it.quote)
                is ProfileViewState.RequestReport -> {
                    DefaultAlert(
                        requireContext(),
                        "Denúnciar publicação",
                        "Você deseja denúnciar essa publicação? Vamos analisá-la e tomar as ações necessárias!",
                        okClick = {
                            view?.showSnackBar("Denúncia enviada com sucesso!")
                        }
                    ).buildDialog()
                }
                is ProfileViewState.RequestShare -> {
                    QuoteShareDialog(requireContext(), it.quoteShareData).buildDialog()
                }
            }
        })
    }

    private fun navigateToNewQuote(quote: Quote? = null) {
        val bundle = bundleOf("quote" to quote)
        findNavController().navigate(R.id.navigation_new_quote, bundle)
    }
}