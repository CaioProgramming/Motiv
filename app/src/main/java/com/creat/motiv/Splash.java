package com.creat.motiv;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.creat.motiv.Utils.Alert;
import com.creat.motiv.Utils.Pref;
import com.creat.motiv.Utils.Tools;
import com.creat.motiv.Utils.Typewritter;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;

import de.mateware.snacky.Snacky;

public class Splash extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;
    private Activity activity = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);

        Pref preferences = new Pref(this);

        setContentView(R.layout.activity_splash);
        Typewritter applogo = findViewById(R.id.apptext);
        TextView brand = findViewById(R.id.inlustrisbrand);
        Date datenow = Calendar.getInstance().getTime();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(datenow);
        brand.setText("Inlustris 2018 - " + calendar.get(Calendar.YEAR));
        applogo.setDelay(300);
        applogo.animateText(getString(R.string.app_name)+"");
        //setalarm();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        CountDownTimer countDownTimer = new CountDownTimer(1500,100) {
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
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = Tools.userreference;
        reference.child(user.getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    Intent i = new Intent(activity,NewUser.class);
                    startActivity(i);
                    activity.finish();
                }else{
                    Intent i = new Intent(activity,MainActivity.class);
                    startActivity(i);
                    activity.finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}