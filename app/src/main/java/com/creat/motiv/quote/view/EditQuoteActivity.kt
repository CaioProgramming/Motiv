package com.creat.motiv.quote.view


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.creat.motiv.R
import com.creat.motiv.databinding.ActivityEditQuoteBinding
import com.creat.motiv.quote.view.binder.EditQuoteBinder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.ilustris.motiv.base.beans.Quote
import kotlinx.android.synthetic.main.activity_edit_quote.*

class EditQuoteActivity : AppCompatActivity(R.layout.activity_edit_quote) {

    var user: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val popupbind: ActivityEditQuoteBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_quote)
        val quotedata = intent.getSerializableExtra("Quote") as? Quote
        EditQuoteBinder(quotedata, popupbind.quoteFormView)
        setSupportActionBar(toolbar)

        supportActionBar?.let { actionBar ->
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
            toolbar.setNavigationOnClickListener { finish() }
            title = if (quotedata == null) "Nova publicação" else "Editar publicação"
        }

    }





}
