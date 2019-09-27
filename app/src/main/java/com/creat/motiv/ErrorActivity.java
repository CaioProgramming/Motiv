package com.creat.motiv;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.creat.motiv.Utils.Tools;

public class ErrorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
        ProgressBar loading = findViewById(R.id.loading);


        Tools.load(loading);


        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        this.finish();


    }
}
