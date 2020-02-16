package com.creat.motiv.view.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.creat.motiv.R
import com.creat.motiv.databinding.FragmentProfileBinding
import com.creat.motiv.model.Beans.User
import com.creat.motiv.model.UserDB
import com.creat.motiv.presenter.ProfilePresenter
import com.creat.motiv.utils.Alert
import kotlinx.android.synthetic.main.fragment_profile.*


class UserActivity : AppCompatActivity() {
     private var uid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fragmentbind = DataBindingUtil.inflate<FragmentProfileBinding>(LayoutInflater.from(this), R.layout.fragment_profile,null,false)
        setContentView(fragmentbind.root)

        uid = intent.getStringExtra("uid")

        initView(fragmentbind)

    }

    private fun initView(fragmentbind: FragmentProfileBinding) {
        setSupportActionBar(profiletoolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        myquotesrecycler.postDelayed({
            setupdata(fragmentbind)
        }, 500)

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

    private fun setupdata(profileBinding: FragmentProfileBinding){
        val userdb = UserDB()
        val profilePresenter = ProfilePresenter(this,profileBinding, uid?.let { userdb.getUser(it) }!!)
    }


}
