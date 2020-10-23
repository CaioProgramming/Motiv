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


class UserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fragmentbind: UserProfileBinding = DataBindingUtil.setContentView(this, R.layout.user_profile)
        setContentView(fragmentbind.root)
        val quserData = intent.getSerializableExtra("USER") as User
        setSupportActionBar(toolbar).run {
            title = quserData.name
        }
        fragmentbind.run {
            ProfileBinder(this@UserActivity, this.profileView, quserData)
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
