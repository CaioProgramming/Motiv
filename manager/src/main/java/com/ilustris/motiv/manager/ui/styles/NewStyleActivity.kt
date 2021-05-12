package com.ilustris.motiv.manager.ui.styles

import android.os.Bundle
import android.os.PersistableBundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.ilustris.motiv.manager.R
import com.ilustris.motiv.manager.databinding.NewStyleFormBinding

class NewStyleActivity : AppCompatActivity(R.layout.new_style_form) {

    var newStyleBinder: NewStyleBinder? = null

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        DataBindingUtil.setContentView<NewStyleFormBinding>(this, R.layout.new_style_form).run {
            newStyleBinder = NewStyleBinder(this, supportFragmentManager)
            newStyleBinder?.initView()
            setSupportActionBar(findViewById(R.id.motiv_toolbar))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.new_style_menu, menu)
        menu?.let {
            val span = SpannableString("Salvar").apply {
                val colorSpan: ForegroundColorSpan =
                    ForegroundColorSpan(getColor(R.color.colorPrimaryDark))
                setSpan(colorSpan, 0, length, 0)
            }
            it.getItem(0).setTitle(span)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save_style -> {
                newStyleBinder?.saveStyle()
                return false
            }
        }

        return super.onOptionsItemSelected(item)

    }
}