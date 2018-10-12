package com.creat.motiv;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

import de.mateware.snacky.Snacky;

public class Splash extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final RelativeLayout layout = findViewById(R.id.layout);
        ImageView applogo = findViewById(R.id.applogo);
        final TextView appname = findViewById(R.id.appname);
        final Animation animation = AnimationUtils.loadAnimation(this,R.anim.slide_in_bottom);
        Animation popin= AnimationUtils.loadAnimation(this,R.anim.pop_in);
        applogo.startAnimation(popin);
        int colorFrom = getResources().getColor(R.color.black);
        int colorTo = getResources().getColor(R.color.white);
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(5000);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                layout.setBackgroundColor((Integer) valueAnimator.getAnimatedValue());
            }
        });
        // milliseconds
        colorAnimation.start();

        CountDownTimer countDownTimer = new CountDownTimer(6000,100) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                appname.setVisibility(View.VISIBLE);
                appname.startAnimation(animation);
                CountDownTimer countDownTimer1 = new CountDownTimer(2000,100) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        SignIn();
                    }
                }.start();
            }
        }.start();



    }

    private void SignIn() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null){
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build());
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                    .setLogo(R.mipmap.ic_launcher)
                    .setAvailableProviders(providers)
                    .setTheme(R.style.AppTheme)
                    .build(),RC_SIGN_IN);
        }else{
            Snacky.builder().setActivity(this).success().setText("Bem vindo " + user.getDisplayName()).show();
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
            this.finish();
        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN){
           IdpResponse response = IdpResponse.fromResultIntent(data);
           if (resultCode == RESULT_OK){
               FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
               Snacky.builder().setActivity(this).success().setText("Bem vindo " + user.getDisplayName()).show();
               Intent i = new Intent(this,MainActivity.class);
               startActivity(i);
               this.finish();


           }else{
               Snacky.builder().setActivity(this).error().setText("Erro " + response.getError().getErrorCode()).show();

           }

        }
    }
}
