package com.creat.motiv;

import android.animation.Animator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.creat.motiv.Beans.Version;
import com.creat.motiv.Fragments.AboutFragment;
import com.creat.motiv.Fragments.HomeFragment;
import com.creat.motiv.Fragments.ProfileFragment;
import com.creat.motiv.Utils.NewQuotepopup;
import com.creat.motiv.Utils.Pref;
import com.creat.motiv.Utils.Tools;
import com.github.mmin18.widget.RealtimeBlurView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import de.mateware.snacky.Snacky;

public class MainActivity extends AppCompatActivity {
    Pref preferences;
    BottomSheetDialog myDialog;
    Version version;
    Context context = this;
    FloatingActionButton newquote;
    private RelativeLayout container;
    FirebaseUser user;
    private BottomNavigationView navigation;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.fui_slide_in_right, R.anim.fab_slide_out_to_left)
                            .replace(R.id.frame, new HomeFragment())
                            .commit();
                    return true;

                case R.id.navigation_user:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.fab_slide_in_from_left, R.anim.fab_slide_out_to_right)
                            .replace(R.id.frame, new ProfileFragment())
                            .commit();
                    return true;


            }
            return false;
        }
    };
    private Dialog m_dialog;
    RealtimeBlurView blurView;
    private void newquotebutton() {
        if (newquote.getVisibility() == View.GONE) {
            newquote.setVisibility(View.VISIBLE);
            Animation in = AnimationUtils.loadAnimation(context, R.anim.pop_in);
            newquote.startAnimation(in);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Intent i = new Intent(this, Splash.class);
            startActivity(i);
            this.finish();
        }
        preferences = new Pref(context);
        setContentView(R.layout.activity_main2);
        findViewById(R.id.offlinemssage);
        newquote = findViewById(R.id.newquote);
        blurView = findViewById(R.id.rootblur);
        this.navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        newquote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               NewQuoteDialog();
            }
        });
        container = findViewById(R.id.container);

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
        if (preferences.nightmodestate()){
            setTheme(R.style.AppTheme_Night);
        }
        //theme();

        internetconnection();
        navigation.setSelectedItemId(R.id.navigation_home);
        version();
        agree();

    }

    private void NewQuoteDialog(){
        NewQuotepopup newQuotepopup = new NewQuotepopup(this, blurView);
        newQuotepopup.showup();
        blurView.setVisibility(View.VISIBLE);

    }



    private void restart(){
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);

    }

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
                      AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppTheme).setTitle("Atualize seu app!")

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


    @Override
    protected void onResume() {
        super.onResume();
        container.post(new Runnable() {
            @Override
            public void run() {
                Reveal();
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fab_slide_out_to_left)
                        .replace(R.id.frame, new HomeFragment())
                        .commit();
            }
        });

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
            preferences = new Pref(context);
            if (!preferences.nightmodestate()){
                preferences.setNight(true);
                restart();
                return false;
            }else {
                preferences.setNight(false);
                restart();
                return false;
                //theme();




            }
            // Commit the edits!

        } else if (id == R.id.navigation_about) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.pop_in, R.anim.fab_slide_out_to_left)

                    .replace(R.id.frame, new AboutFragment())
                    .commit();
            return false;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        internetconnection();
    }


    private void Reveal() {
        Animation fade = AnimationUtils.loadAnimation(this,R.anim.fade_in);
        container.startAnimation(fade);
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
        if (myDialog == null) {
            myDialog = new BottomSheetDialog(Objects.requireNonNull(this), R.style.Dialog_No_Border);
            myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (!isNetworkAvailable()) {
                myDialog.setContentView(R.layout.noconnectioncard);
                myDialog.setCanceledOnTouchOutside(true);
                TextView message = myDialog.findViewById(R.id.offlinemssage);
                message.setText(Tools.offlinemessage());
                if (!myDialog.isShowing()) {
                    myDialog.show();
                }
            } else {

                myDialog.dismiss();
            }
        }


    }

}
