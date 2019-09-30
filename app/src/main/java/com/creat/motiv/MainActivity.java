package com.creat.motiv;

import android.animation.Animator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.creat.motiv.Adapters.MainAdapter;
import com.creat.motiv.Beans.User;
import com.creat.motiv.Beans.Version;
import com.creat.motiv.Utils.Alert;
import com.creat.motiv.Utils.Pref;
import com.creat.motiv.Utils.Tools;
import com.github.mmin18.widget.RealtimeBlurView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import de.mateware.snacky.Snacky;

public class MainActivity extends AppCompatActivity {
    Pref preferences;
    protected App app;
    Version version;
    Alert a;
    Context context = this;
    public static ViewPager pager;
    public static boolean home = true;
    public static boolean search = false;
    private Dialog m_dialog;
    RealtimeBlurView blurView;
    Activity activity = this;
    FirebaseUser user;
    private TabLayout tabs;
    CoordinatorLayout container;



    private void agree() {
        m_dialog = new Dialog(this, R.style.Dialog_No_Border);
        Animation in = AnimationUtils.loadAnimation(context, R.anim.slide_in_top);
        LayoutInflater m_inflater = LayoutInflater.from(context);
        View m_view = m_inflater.inflate(R.layout.politics, null);
        m_dialog.setContentView(m_view);
        Button agreebutton = m_view.findViewById(R.id.agreebutton);
        agreebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_dialog.dismiss();
                preferences.setAgree(true);

            }
        });
        m_view.startAnimation(in);
        if (!preferences.agreestate()) {
            Snacky.builder().setActivity(this).warning()
                    .setText("Você precisa concordar com os termos de uso para usar o aplicativo!")
                    .setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            m_dialog.show();
                        }
                    }).setDuration(20000).show();
        }
        m_dialog.setCanceledOnTouchOutside(false);
        m_dialog.setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (App) getApplication();
        user = FirebaseAuth.getInstance().getCurrentUser();
        checkUser();
        preferences = new Pref(context);
        setContentView(R.layout.activity_main2);
        container = findViewById(R.id.container);

        if (savedInstanceState == null){
            container.setVisibility(View.INVISIBLE);
            ViewTreeObserver observer = container.getViewTreeObserver();
            if (observer.isAlive()){
                CircularReveal();
                observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        CircularReveal();
                        container.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    }
                });
            }
        }


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.tabs = findViewById(R.id.tabs);
        pager = findViewById(R.id.pager);
        blurView = findViewById(R.id.rootblur);
        final CircleImageView profilepic = findViewById(R.id.profilepic);
        Glide.with(this).load(user.getPhotoUrl()).into(profilepic);
        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pager.setCurrentItem(3);
            }
        });
        assert user != null;
        if (!user.isEmailVerified()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this).setMessage("Email não verificado");
            builder.setMessage("Eai Beleza? Verifica o email que você vai poder fazer mais que apenas ver frases");

            builder.setPositiveButton("Manda esse email aí po", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    user.sendEmailVerification();
                }
            });
            builder.setNegativeButton("Não to afim meu camarada", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            builder.show();}
        //theme();
        internetconnection();
        version();
        agree();
        MainAdapter mainAdapter = new MainAdapter(getSupportFragmentManager());
        pager.setAdapter(mainAdapter);
        tabs.setupWithViewPager(pager);
        tabs.getTabAt(0).setText("Home");
        tabs.getTabAt(1).setText("Favoritos");
        tabs.getTabAt(2).setText("Perfil");
        AdView adView = findViewById(R.id.adView);
        MobileAds.initialize(this,
                "ca-app-pub-4979584089010597/9177000416");

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

    }


    private void checkUser() {
        if (user != null) {

            final DatabaseReference userreference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
            userreference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        final User u = new User();
                        u.setName(user.getDisplayName());
                        u.setEmail(user.getEmail());
                        u.setPicurl(String.valueOf(user.getPhotoUrl()));
                        u.setPhonenumber(user.getPhoneNumber());
                        u.setUid(user.getUid());
                        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                            @Override
                            public void onSuccess(InstanceIdResult instanceIdResult) {
                                u.setToken(instanceIdResult.getToken());
                                userreference.setValue(u);
                            }
                        });


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            Intent i = new Intent(this, Splash.class);
            startActivity(i);
            this.finish();
        }
    }

    private void version() {
        final String versionName = BuildConfig.VERSION_NAME;
        version = new Version();

        Query versioncheck;
        versioncheck = FirebaseDatabase.getInstance().getReference().child("version");

        versioncheck.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {

                    if (d != null) {
                        version.setVersion(Objects.requireNonNull(d.getValue()).toString());
                    }


                }
                if (!version.getVersion().equals(versionName)) {


                    Snackbar snackbar = Snacky.builder().setActivity(activity).setText("Sua versão está desatualizada " +
                            " o motiv atualmente está na versão " + version.getVersion() + " enquanto você está na versão  " + versionName)
                            .setIcon(R.drawable.ic_autorenew_black_24dp)
                            .setTextColor(Color.BLACK)
                            .setActionTextColor(getResources().getColor(R.color.colorPrimaryDark))
                            .setBackgroundColor(Color.WHITE)
                            .setDuration(10000)
                            .build();

                    snackbar.setAction("Atualizar", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.creat.motiv");
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        }
                    });
                    snackbar.show();

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.navigation_about) {
            Alert alert = new Alert(this);
            alert.about();
        } else {
            settings();
        }
        return super.onOptionsItemSelected(item);
    }

    private void settings() {
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkUser();
        internetconnection();

    }


    @Override
    protected void onStart() {
        super.onStart();
        checkUser();
        internetconnection();
    }



    @Override
    protected void onRestart() {
        super.onRestart();
        checkUser();
        internetconnection();
    }

    @Override
    public void onBackPressed() {

        if (pager.getCurrentItem() == 0) {
            this.finish();
        } else {
            pager.setCurrentItem(0, true);
        }
        super.onBackPressed();

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void internetconnection() {
        if (a == null) {
            if (!isNetworkAvailable()) {
                a.Message(getDrawable(R.drawable.ic_broken_link), Tools.offlinemessage());
            }
        }


    }


    private void CircularReveal(){
        Rect bounds = new Rect();
        container.getDrawingRect(bounds);
        int cx = bounds.centerX();
        int cy = bounds.centerY();
        float fradius = Math.max(container.getWidth(),container.getHeight());
        Animator circulareveal = ViewAnimationUtils.createCircularReveal(container,cx,cy,0,fradius);
        circulareveal.setDuration(1500);
        container.setVisibility(View.VISIBLE);
        circulareveal.start();



    }




}
