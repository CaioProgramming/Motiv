package com.creat.motiv;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.creat.motiv.Utils.Pref;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;

import de.mateware.snacky.Snacky;

public class Splash extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);

        Pref preferences = new Pref(this);

        setContentView(R.layout.activity_splash);
        ImageView applogo = findViewById(R.id.applogo);
        TextView brand = findViewById(R.id.inlustrisbrand);
        Date datenow = Calendar.getInstance().getTime();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(datenow);
        brand.setText("Inlustris 2018 - " + calendar.get(Calendar.YEAR));
        Animation popin= AnimationUtils.loadAnimation(this,R.anim.pop_in);
        applogo.startAnimation(popin);
        //setalarm();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        CountDownTimer countDownTimer = new CountDownTimer(3000,100) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                SignIn();

            }
        }.start();



    }


    private void SignIn() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null){
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.FacebookBuilder().build(),
                    //new AuthUI.IdpConfig.TwitterBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build(),
                    new AuthUI.IdpConfig.EmailBuilder().build());
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                    .setLogo(R.mipmap.ic_launcher)
                    .setAvailableProviders(providers)
                    .setTheme(R.style.AppTheme)
                    .build(),RC_SIGN_IN);
        }else{
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("novo",false);
            i.putExtra("notification",true);
            startActivity(i);
            this.finish();
        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                newuser();


            } else {
                if (response != null) {
                    Snacky.builder().setActivity(this).error().setText("Erro " + Objects.requireNonNull(response.getError()).getMessage() + " causa " + response.getError().getCause()).show();
                }

            }

        }
    }

    private void newuser() {
        Intent i = new Intent(this, NewUser.class);
        startActivity(i);
        this.finish();
    }
}