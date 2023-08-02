package com.ilustris.motiv.base

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.res.stringResource
import com.creat.motiv.base.R
import com.ilustris.motiv.base.utils.INITIALACT
import com.ilustris.motiv.foundation.ui.theme.MotivTheme

class ErrorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MotivTheme {
                Column {
                    Text(
                        stringResource(R.string.fatal_error_title),
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Text(
                        stringResource(R.string.fatal_error_message),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Button(onClick = {
                        onBackPressed()
                    }) {
                        Text(stringResource(R.string.fatal_error_button))
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, Class.forName(INITIALACT)))
    }
}
