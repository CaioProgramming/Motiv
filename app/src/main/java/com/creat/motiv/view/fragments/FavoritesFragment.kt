package com.creat.motiv.view.fragments


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.creat.motiv.R
import com.creat.motiv.databinding.FragmentHomeBinding
import com.creat.motiv.profile.view.ProfileFragment
import com.google.firebase.auth.FirebaseAuth


const val FAVORITES_FRAG_TAG = "FAVORITES_FRAGMENT"

class FavoritesFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_home, container, false).rootView
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.navigation_add -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (context is AppCompatActivity) {
            val activity: AppCompatActivity = context as AppCompatActivity
            activity.run {
                supportActionBar?.show()
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }
        }

        FragmentHomeBinding.bind(view).run {
            val user = FirebaseAuth.getInstance().currentUser
            user?.let {

            }
        }
    }


}