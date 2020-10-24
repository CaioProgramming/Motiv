package com.creat.motiv.view.binders

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.app.ActivityOptionsCompat
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.creat.motiv.R
import com.creat.motiv.databinding.UserQuoteCardViewBinding
import com.creat.motiv.model.beans.User
import com.creat.motiv.presenter.UserPresenter
import com.creat.motiv.utils.gone
import com.creat.motiv.utils.visible
import com.creat.motiv.view.BaseView
import com.creat.motiv.view.activities.UserActivity

class UserViewBinder(
        val userID: String,
        override val context: Context,
        override val viewBind: UserQuoteCardViewBinding) : BaseView<User>() {

    override fun presenter() = UserPresenter(this)

    override fun onLoading() {
        viewBind.userShimmer.startShimmer()
    }


    fun displayView() {
        viewBind.userContainer.visible()
        val inAnim = AnimationUtils.loadAnimation(context, R.anim.slide_in_bottom)
        viewBind.userContainer.startAnimation(inAnim)
    }

    fun hideView() {
        val outAnim = AnimationUtils.loadAnimation(context, R.anim.slide_out)
        viewBind.userContainer.startAnimation(outAnim)
        viewBind.userContainer.gone()
    }

    override fun showData(data: User) {
        viewBind.run {
            userData = data
            Glide.with(context).load(data.picurl).into(viewBind.userpic)
            userContainer.setOnClickListener {
                showUserProfile(data)
            }
        }
    }

    private fun showUserProfile(user: User) {
        if (user.uid != presenter().currentUser()?.uid) {
            val i = Intent(context, UserActivity::class.java)
            i.putExtra("USER", user)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(context as Activity,
                    viewBind.userpic as View, "profilepic")
            context.startActivity(i, options.toBundle())
        } else {
            if (context is Activity) {
                val activity: Activity = context
                activity.run {
                    val pager = findViewById<ViewPager>(R.id.pager)
                    pager.currentItem = 2
                }
            }
        }

    }

    init {
        initView()
    }

    override fun onLoadFinish() {
        viewBind.userShimmer.run {
            stopShimmer()
            hideShimmer()
        }
    }

    override fun initView() {
        presenter().getUser(userID)
    }

}