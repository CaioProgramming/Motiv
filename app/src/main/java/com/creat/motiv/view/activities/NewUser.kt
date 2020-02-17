package com.creat.motiv.View.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.creat.motiv.R
import com.creat.motiv.adapters.ViewPagerAdapter
import com.creat.motiv.view.activities.MainActivity
import com.google.android.material.tabs.TabLayout

class NewUser : AppCompatActivity() {
    var adapter: ViewPagerAdapter?? = null
    var tabs: TabLayout?? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_user)
        val start = findViewById<Button>(R.id.start)
        tabs = findViewById(R.id.tabs)
        val slides = findViewById<ViewPager>(R.id.slides)
        tabs!!.setupWithViewPager(slides, true)
        adapter = ViewPagerAdapter(this, this)
        slides.adapter = adapter
        setupTabIcons()
        start.visibility = View.INVISIBLE
        start.setOnClickListener { Comecar() }


        slides.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                if (position == 7) {
                    start.visibility = View.VISIBLE
                }
            }

            override fun onPageSelected(position: Int) {
                if (position == 7) {
                    start.visibility = View.VISIBLE
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

    }

    private fun setupTabIcons() {
        tabs!!.setSelectedTabIndicatorColor(Color.TRANSPARENT)

        for (i in 0..6) {
            tabs!!.getTabAt(i)!!.icon = getDrawable(R.drawable.dot)
        }

    }

    private fun Comecar() {

        val i = Intent(this, MainActivity::class.java)
        i.putExtra("novo", true)
        startActivity(i)
        this.finish()

    }
}
