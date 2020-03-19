package com.creat.motiv.view.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.creat.motiv.R
import com.creat.motiv.databinding.FragmentProfileBinding
import com.creat.motiv.model.Beans.User
import com.creat.motiv.model.UserDB
import com.creat.motiv.presenter.ProfilePresenter
import com.creat.motiv.utils.Alert
import com.creat.motiv.utils.ColorUtils.ERROR
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class UserActivity : AppCompatActivity() {
    private var uid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fragmentbind: FragmentProfileBinding = DataBindingUtil.setContentView(this,R.layout.fragment_profile)
        setContentView(fragmentbind.root)

        uid = intent.getStringExtra("uid")
        setupdata(fragmentbind)
        //initView(fragmentbind)

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
        uid?.let {
            userdb.getUser(it, object : ValueEventListener {
                override fun onCancelled(dataSnapshot: DatabaseError) {

                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val user = dataSnapshot.getValue(User::class.java)
                        user.let {
                            val profilePresenter = ProfilePresenter(this@UserActivity,profileBinding,user!!)
                            supportActionBar?.title = user.name
                            supportActionBar?.setDisplayShowHomeEnabled(true)
                        }

                    } else {
                        Alert.builder(this@UserActivity).snackmessage(ERROR, "Usuário não encontrado")
                        finish()
                    }
                }
            })
        }
    }


}
