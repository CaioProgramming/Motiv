package com.creat.motiv;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
    public static boolean home = true;
    private Dialog m_dialog;
    RealtimeBlurView blurView;
    Activity activity = this;
    private android.widget.FrameLayout frame;
    private void newquotebutton() {
        if (newquote.getVisibility() == View.GONE) {
            newquote.setVisibility(View.VISIBLE);
            Animation in = AnimationUtils.loadAnimation(context, R.anim.pop_in);
            newquote.startAnimation(in);
        }
    }

    private android.support.design.widget.TabLayout tabs;

    private void NewQuoteDialog() {
        NewQuotepopup newQuotepopup = new NewQuotepopup(this, blurView);
        newQuotepopup.showup();
        blurView.setVisibility(View.VISIBLE);

    }


    private void restart() {
        Intent i = new Intent(this, MainActivity.class);
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
        this.tabs = findViewById(R.id.tabs);
        this.frame = findViewById(R.id.frame);
        findViewById(R.id.offlinemssage);
        newquote = findViewById(R.id.newquote);
        blurView = findViewById(R.id.rootblur);
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

        //theme();

        internetconnection();

        version();
        agree();

        if (!frame.isAttachedToWindow()) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame, new HomeFragment())
                    .commit();
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

                      if (d != null){
                          version.setVersion(Objects.requireNonNull(d.getValue()).toString());
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();


        internetconnection();

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        if (!home) {
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.frame, new HomeFragment())
                                    .commit();
                        }
                        break;
                    case 1:
                        Snacky.builder().setActivity(activity).setText("Em desenvolvimento").info().show();
                        break;
                    case 2:
                        home = false;
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame, new ProfileFragment())
                                .commit();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        home = false;
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame, new AboutFragment())
                    .commit();
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
        super.onBackPressed();
        if (home) {
            this.finish();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.fade_in, R.anim.fab_slide_out_to_left)
                    .replace(R.id.frame, new HomeFragment())
                    .commit();
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
