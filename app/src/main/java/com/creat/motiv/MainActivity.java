package com.creat.motiv;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.creat.motiv.Adapters.MainAdapter;
import com.creat.motiv.Beans.Version;
import com.creat.motiv.Utils.Alert;
import com.creat.motiv.Utils.Pref;
import com.creat.motiv.Utils.Tools;
import com.github.mmin18.widget.RealtimeBlurView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import de.mateware.snacky.Snacky;

public class MainActivity extends AppCompatActivity {
    Pref preferences;
    protected App app;
    BottomSheetDialog myDialog;
    Version version;
    Context context = this;
    public static ViewPager pager;
    public static boolean home = true;
    public static boolean search = false;
    private Dialog m_dialog;
    RealtimeBlurView blurView;
    Activity activity = this;
    FirebaseUser user;
    private android.support.design.widget.TabLayout tabs;




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
        if (user == null) {
            Intent i = new Intent(this, Splash.class);
            startActivity(i);
            this.finish();
        }
        preferences = new Pref(context);
        setContentView(R.layout.activity_main2);

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

    private void version() {
        final String versionName = BuildConfig.VERSION_NAME;
        version = new Version();

        Query versioncheck;
        versioncheck = FirebaseDatabase.getInstance().getReference().child("version");

        versioncheck.addListenerForSingleValueEvent(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                  for (DataSnapshot d : dataSnapshot.getChildren()) {

                      if (d != null){
                          version.setVersion(Objects.requireNonNull(d.getValue()).toString());
                      }


                  }
                  if (!version.getVersion().equals(versionName)){


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


        internetconnection();

    }


    @Override
    protected void onStart() {
        super.onStart();
        internetconnection();
    }



    @Override
    protected void onRestart() {
        super.onRestart();
        internetconnection();
    }

    @Override
    public void onBackPressed() {

        if (home) {
            this.finish();
            super.onBackPressed();
        } else {
            tabs.getTabAt(0).select();
            home = true;
            pager.setCurrentItem(0, true);
        }
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
        if (myDialog == null) {
            myDialog = new BottomSheetDialog(Objects.requireNonNull(this), R.style.Dialog_No_Border);
            myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


            myDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    Animation out = AnimationUtils.loadAnimation(activity, R.anim.fade_out);
                    blurView.startAnimation(out);
                    blurView.setVisibility(View.GONE);


                }
            });

            if (!isNetworkAvailable()) {
                myDialog.setContentView(R.layout.noconnectioncard);
                myDialog.setCanceledOnTouchOutside(false);
                TextView message = myDialog.findViewById(R.id.offlinemssage);
                message.setText(Tools.offlinemessage());
                if (!myDialog.isShowing()) {
                    myDialog.show();
                    Animation in = AnimationUtils.loadAnimation(activity, R.anim.fade_in);
                    blurView.setVisibility(View.VISIBLE);
                    blurView.startAnimation(in);
                }
            } else {

                myDialog.dismiss();
            }
        }


    }

}
