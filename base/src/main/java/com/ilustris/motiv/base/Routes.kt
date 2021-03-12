package com.ilustris.motiv.base

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import com.ilustris.motiv.base.beans.Quote
import com.ilustris.motiv.base.beans.User

const val USER_ACTIVITY = "com.ilustris.profile.view.UserActivity"
const val MANAGER_ACTIVITY = "com.ilustris.motiv.manager.ManagerActivity"
const val PROFILE_PIC_TRANSACTION = "profilepic"

class Routes(private val context: Context) {

    fun openUserProfile(user: User, sharedView: View) {
        val i = Intent(context, Class.forName(USER_ACTIVITY))
        i.putExtra("USER", user)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(context as Activity, sharedView, PROFILE_PIC_TRANSACTION)
        context.startActivity(i, options.toBundle())
    }

    fun openManager(user: User, transactionView: View) {
        val i = Intent(context, Class.forName(MANAGER_ACTIVITY)).apply {
            putExtra("User", user)
        }
        i.putExtra("User", user)
        val options = ActivityOptions.makeSceneTransitionAnimation(context as Activity,
                android.util.Pair(transactionView, context.getString(R.string.quote_transaction)))
        context.startActivity(i, options.toBundle())
    }


}