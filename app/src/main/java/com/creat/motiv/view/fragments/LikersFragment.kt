package com.creat.motiv.view.fragments

import android.content.Context
import com.creat.motiv.utilities.getSupportFragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class LikersFragment : BottomSheetDialogFragment() {

    lateinit var likeList: List<String>


    companion object {
        const val LIKERS_FRAGMENT = "LIKERS_FRAGMENT"
        fun build(context: Context, likes: List<String>) {
            context.getSupportFragmentManager()?.let {
                LikersFragment().apply {
                    likeList = likes
                }.show(it, LIKERS_FRAGMENT)
            }
        }
    }

}