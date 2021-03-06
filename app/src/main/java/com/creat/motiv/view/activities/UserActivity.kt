package com.creat.motiv.view.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.creat.motiv.R
import com.creat.motiv.databinding.UserProfileBinding
import com.creat.motiv.model.beans.User
import com.creat.motiv.view.binders.ProfileBinder
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.toolbar
import kotlinx.android.synthetic.main.user_profile.*


class UserActivity : AppCompatActivity(R.layout.user_profile) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fragmentbind: UserProfileBinding = DataBindingUtil.setContentView(this, R.layout.user_profile)
        val userData = intent.getSerializableExtra("USER") as User
        setSupportActionBar(toolbar)
        actionBar?.let {
            it.title = userData.name
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        }
        fragmentbind.run {
            ProfileBinder(this@UserActivity, this.profileView, userData)
        }


    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            // Respond to the action bar's Up/Home button
            android.R.id.home -> {
                supportFinishAfterTransition()
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }



}
