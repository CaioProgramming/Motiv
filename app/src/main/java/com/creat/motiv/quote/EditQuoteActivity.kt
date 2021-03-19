package com.creat.motiv.quote


import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.creat.motiv.R
import com.creat.motiv.databinding.ActivityEditQuoteBinding
import com.creat.motiv.quote.view.binder.EditQuoteBinder
import com.creat.motiv.utilities.MotivPreferences
import com.creat.motiv.utilities.NEW_QUOTE_TUTORIAL
import com.creat.motiv.tutorial.HomeTutorialDialog
import com.creat.motiv.tutorial.NewPostTutorialDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.ilustris.motiv.base.beans.Quote

class EditQuoteActivity : AppCompatActivity(R.layout.activity_edit_quote) {

    var user: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN)
        val popupbind: ActivityEditQuoteBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_quote)
        val quotedata = intent.getSerializableExtra("Quote") as? Quote
        EditQuoteBinder(quotedata, popupbind.quoteFormView)
        setSupportActionBar(findViewById(R.id.motiv_toolbar))

        supportActionBar?.let { actionBar ->
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
            val motivToolbar: Toolbar = findViewById(R.id.motiv_toolbar)
            motivToolbar.setNavigationOnClickListener { finish() }
            title = if (quotedata == null) "Nova publicação" else "Editar publicação"
        }
        showTutorial()
    }

    private fun showTutorial() {
        val motivPreferences = MotivPreferences(this)
        if (!motivPreferences.checkTutorial(NEW_QUOTE_TUTORIAL)) {
            NewPostTutorialDialog(this).buildDialog()
        }
    }

}
