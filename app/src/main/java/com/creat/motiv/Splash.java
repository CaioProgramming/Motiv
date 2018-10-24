package com.creat.motiv;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import de.mateware.snacky.Snacky;

public class Splash extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;
     @Override
    protected void onCreate(Bundle savedInstanceState) {

         Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,10);
        calendar.set(Calendar.SECOND,30);
         Intent intent = new Intent(getApplicationContext(),Notification_reciever.class);
         PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),100,intent,PendingIntent.FLAG_UPDATE_CURRENT);

         AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
         alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final RelativeLayout layout = findViewById(R.id.layout);
        ImageView applogo = findViewById(R.id.applogo);
        Animation popin= AnimationUtils.loadAnimation(this,R.anim.pop_in);
        applogo.startAnimation(popin);


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
                    new AuthUI.IdpConfig.EmailBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build());
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                    .setLogo(R.mipmap.ic_launcher)
                    .setAvailableProviders(providers)
                    .setTheme(R.style.AppTheme)
                    .build(),RC_SIGN_IN);
        }else{
            Intent i = new Intent(this,MainActivity.class);
            i.putExtra("novo",false);
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
                 newuser();



           }else{
               Snacky.builder().setActivity(this).error().setText("Erro " + response.getError().getErrorCode()).show();

           }

        }
    }

     private void newuser() {
        Intent i = new Intent(this,NewUser.class);
         startActivity(i);
        this.finish();
    }
}
