package com.creat.motiv;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.creat.motiv.Beans.Version;
import com.creat.motiv.Fragments.AboutFragment;
import com.creat.motiv.Fragments.HomeFragment;
import com.creat.motiv.Fragments.NewQuoteFragment;
import com.creat.motiv.Fragments.ProfileFragment;
import com.creat.motiv.Fragments.SearchFragment;
import com.creat.motiv.Utils.BottomNavigationHelper;
import com.creat.motiv.Utils.Pref;
import com.creat.motiv.Utils.Tools;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import de.mateware.snacky.Snacky;

public class MainActivity extends AppCompatActivity {

    private android.widget.FrameLayout frame;
    private BottomNavigationView navigation;
    Version version;
    Context context = this;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    BottomNavigationHelper.disableShiftMode(navigation);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame, new HomeFragment())
                            .commit();
                    return true;
                case R.id.navigation_search:
                    BottomNavigationHelper.disableShiftMode(navigation);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame, new SearchFragment())
                            .commit();
                    return true;
                case R.id.navigation_user:
                    BottomNavigationHelper.disableShiftMode(navigation);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame, new ProfileFragment())
                            .commit();
                    return true;


                case R.id.navigation_about:
                    BottomNavigationHelper.disableShiftMode(navigation);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame, new AboutFragment())
                            .commit();
                    return true;
            }
            return false;
        }
    };
    private com.github.mmin18.widget.RealtimeBlurView rootblur;
    private android.widget.TextView offlinemessage;
    private android.support.v7.widget.AppCompatButton reload;
    private android.widget.ImageView offlineimage;
    private android.widget.LinearLayout offline;
    Pref preferences;
    private android.widget.TextView offlinemssage;
    private android.widget.RelativeLayout container;
    private FloatingActionButton newquote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        this.offlinemssage = findViewById(R.id.offlinemssage);
        this.newquote = findViewById(R.id.newquote);
        this.container = findViewById(R.id.container);
        this.offline = findViewById(R.id.offline);
        this.offlineimage = findViewById(R.id.offlineimage);
        this.reload = findViewById(R.id.reload);
        this.offlinemessage = findViewById(R.id.offlinemssage);
        this.rootblur = findViewById(R.id.rootblur);
        this.navigation = findViewById(R.id.navigation);
        this.frame = findViewById(R.id.frame);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationHelper.disableShiftMode(navigation);
        newquote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame, new NewQuoteFragment())
                        .commit();
            }
        });




        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (!user.isEmailVerified()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this).setMessage("Email não verificado");
            builder.setMessage("Beleza? Verifica o email que você vai poder fazer mais que apenas ver frases");
            builder.setPositiveButton("Manda esse email aí po", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    user.sendEmailVerification();
                }
            });
            builder.setNegativeButton("Não to afim meu camarada", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            builder.show();}

        theme();
        internetconnection();
        navigation.setSelectedItemId(R.id.navigation_home);
        version();


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
                          version.setVersion(d.getValue().toString());
                      }


                  }
                  if (!version.getVersion().equals(versionName)){
                  AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle("Atualize seu app!")

                          .setMessage("Sua versão está desatualizada, não quer atualizar para ter acesso as últimas novidades?" +
                                  " O motiv atualmente está na versão " + version.getVersion() + " enquanto você está na versão  " + versionName)
                          .setNegativeButton("Vo atualiza e te aviso", new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialogInterface, int i) {
                                  dialogInterface.dismiss();
                              }
                          })
                          .setPositiveButton("Sim, quero atualizar!", new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialogInterface, int i) {
                                  Uri uri = Uri.parse("https://motiv-d16d1.firebaseapp.com");
                                  Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                  startActivity(intent);
                              }
                          });
                  builder.show();}


                  }

              @Override
              public void onCancelled(@NonNull DatabaseError databaseError) {

              }
          });
    }

    private boolean verifyversion(String versionName, Version version) {
        if (!version.getVersion().equals(versionName)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("Atualize seu app!")

                    .setMessage("Sua versão está desatualizada, não quer atualizar para ter acesso as últimas novidades?" +
                            " O motiv atualmente está na versão " + version.getVersion() + " enquanto você está na versão  " + versionName)
                    .setNegativeButton("Vo atualiza e te aviso", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .setPositiveButton("Sim, quero atualizar!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Uri uri = Uri.parse("https://motiv-d16d1.firebaseapp.com");
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        }
                    });
            builder.show();
            return false;
        }

        return true;
    }

    private void theme() {
        preferences = new Pref(this);
        if (preferences.nightmodestate()) {
            navigation.setBackgroundColor(Color.BLACK);
            navigation.setItemIconTintList(ColorStateList.valueOf(Color.WHITE));
            navigation.setItemTextColor(ColorStateList.valueOf(Color.WHITE));
            container.setBackgroundColor(getResources().getColor( R.color.grey_900) );
        }else{
            navigation.setBackgroundColor(Color.WHITE);
            navigation.setItemIconTintList(ColorStateList.valueOf(Color.BLACK));
            navigation.setItemTextColor(ColorStateList.valueOf(Color.BLACK));
            container.setBackgroundColor(getResources().getColor( R.color.grey_100) );


        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        internetconnection();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.theme) {
            if (preferences.nightmodestate()){
                preferences.setNight(false);

                theme();


            }else {
                preferences.setNight(true);
                theme();




            }
            // Commit the edits!

        } else if (id == R.id.exit) {
            FirebaseAuth.getInstance().signOut();
            Snacky.builder().setActivity(this).info().setText("Voce encerrou sua sessão, o aplicativo será encerrado").show();
            CountDownTimer timer = new CountDownTimer(3000, 100) {
                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {

                }
            }.start();
            this.finish();
        }

        return super.onOptionsItemSelected(item);
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
        if (navigation.getSelectedItemId() != R.id.navigation_home) {
            navigation.setSelectedItemId(R.id.navigation_home);
        }else{
            this.finish();
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
        if (!isNetworkAvailable()) {
            offline.setVisibility(View.VISIBLE);
            offlinemessage.setText(Tools.offlinemessage());
            Glide.with(this).asGif().load(R.drawable.spaceguy).into(offlineimage);
            reload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    internetconnection();
                }
            });
            navigation.setVisibility(View.GONE);
        } else {
            offline.setVisibility(View.GONE);
            navigation.setVisibility(View.VISIBLE);
        }
    }

}
