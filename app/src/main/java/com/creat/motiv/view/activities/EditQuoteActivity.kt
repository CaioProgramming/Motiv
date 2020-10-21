package com.creat.motiv.view.activities


import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.creat.motiv.R
import com.creat.motiv.databinding.ActivityEditQuoteBinding
import com.creat.motiv.model.Beans.Quote
import com.creat.motiv.presenter.QuoteFormPresenter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_edit_quote.*

class EditQuoteActivity : AppCompatActivity() {

    var user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private var quoteFormPresenter: QuoteFormPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val popupbind: ActivityEditQuoteBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_quote)
        setContentView(popupbind.root)
        quoteFormPresenter = QuoteFormPresenter(this, popupbind.quoteFormView, user)
        val quotedata = intent.getSerializableExtra("Quote") as? Quote
        quotedata?.let { quoteFormPresenter?.setquote(it) }
        setSupportActionBar(toolbar)
        toolbar.title = "Editar post"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.newquotemenu, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.salvar) {
            quoteFormPresenter?.salvar()
        }
        return super.onOptionsItemSelected(item)
    }


}
