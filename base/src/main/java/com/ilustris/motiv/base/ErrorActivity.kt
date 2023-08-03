package com.ilustris.motiv.base

import ai.atick.material.MaterialColor
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.creat.motiv.base.R
import com.ilustris.motiv.base.ui.component.MotivLoader
import com.ilustris.motiv.base.utils.INITIALACT
import com.ilustris.motiv.foundation.ui.theme.MotivTheme

class ErrorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MotivTheme {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    MotivLoader(modifier = Modifier.size(50.dp))

                    Text(
                        stringResource(R.string.fatal_error_title),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        stringResource(R.string.fatal_error_message),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(8.dp)
                    )
                    Button(
                        onClick = {
                            onBackPressed()
                        }, colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialColor.DeepPurpleA400
                        )
                    ) {
                        Text(
                            stringResource(R.string.fatal_error_button),
                            color = MaterialColor.DeepPurpleA400
                        )
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
