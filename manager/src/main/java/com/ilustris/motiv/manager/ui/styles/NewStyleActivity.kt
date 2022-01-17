package com.ilustris.motiv.manager.ui.styles

import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.ilustris.motiv.manager.R
import com.ilustris.motiv.manager.databinding.NewStyleFormBinding

class NewStyleActivity : AppCompatActivity(R.layout.new_style_form) {


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        NewStyleFormBinding.inflate(layoutInflater).run {
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
                return false
            }
        }

        return super.onOptionsItemSelected(item)

    }
}