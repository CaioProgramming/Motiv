package com.creat.motiv.profile.view


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.creat.motiv.R
import com.creat.motiv.databinding.FragmentProfileBinding
import com.ilustris.motiv.base.utils.hideBackButton
import com.ilustris.motiv.base.utils.showSupportActionBar
import com.creat.motiv.view.fragments.FAVORITES_FRAG_TAG
import com.creat.motiv.view.fragments.FavoritesFragment
import com.google.firebase.auth.FirebaseAuth
import com.ilustris.motiv.base.utils.activity


const val PROFILE_FRAG_TAG = "PROFILE_FRAGMENT"

class ProfileFragment : Fragment() {
    var fragmentbind: FragmentProfileBinding? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        fragmentbind = FragmentProfileBinding.inflate(inflater)
        setHasOptionsMenu(true)
        return fragmentbind?.root

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profilemenu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_settings -> {

                return false
            }
            R.id.navigation_favorites -> {
                activity?.supportFragmentManager?.run {
                    beginTransaction()
                            .setCustomAnimations(R.anim.fui_slide_in_right, R.anim.slide_out_left)
                            .replace(R.id.nav_host_fragment, FavoritesFragment(), FAVORITES_FRAG_TAG)
                            .commit()
                }
              return false
            }
        }

        return super.onOptionsItemSelected(item)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.activity()?.let {
            val user = FirebaseAuth.getInstance().currentUser
            user?.let { user ->
                fragmentbind?.let { profileBind ->


                }
            }
            it.showSupportActionBar()
            it.hideBackButton()
        }
    }



}
