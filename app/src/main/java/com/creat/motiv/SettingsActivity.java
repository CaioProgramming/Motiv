package com.creat.motiv;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.creat.motiv.Beans.Quotes;
import com.creat.motiv.Database.QuotesDB;
import com.creat.motiv.Utils.Alert;
import com.creat.motiv.Utils.Tools;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.mateware.snacky.Snacky;

import static com.creat.motiv.Utils.Tools.searcharg;


public class SettingsActivity extends AppCompatActivity {
    protected App app;

    Activity activity = this;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private androidx.appcompat.widget.Toolbar toolbar;
    private TextView changename, deleteposts, deleteaccount, exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        app = (App) getApplication();

        this.exit = findViewById(R.id.exit);
        this.deleteaccount = findViewById(R.id.deleteaccount);
        this.deleteposts = findViewById(R.id.deleteposts);
        this.changename = findViewById(R.id.changename);
        this.toolbar = findViewById(R.id.toolbar);


    }

    private void Removeposts() {


        Query quotesdb = Tools.quotesreference;
        quotesdb.orderByChild("userID")
                .startAt(user.getUid())
                .endAt(user.getUid() + searcharg);
        quotesdb.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final ArrayList<Quotes> myquotes = new ArrayList<>();
                myquotes.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Quotes q = d.getValue(Quotes.class);
                    myquotes.add(q);
                }
                final QuotesDB quotesDB = new QuotesDB();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity)
                        .setTitle("Tem certeza?")
                        .setMessage("Suas " + myquotes.size() + " frases serão removidas para sempre! S E M P R E")
                        .setNeutralButton("Tenho certeza sim, cliquei porque quis!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                for (Quotes quotes : myquotes) {
                                    quotesDB.Removerposts(activity, quotes.getId());
                                }
                            }
                        })
                        .setNegativeButton("Cliquei errado calma", null);
                alertDialog.show();


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void RemoverAccount() {
        Query quotesdb = Tools.quotesreference;
        quotesdb.orderByChild("userID").startAt(user.getUid())
                .endAt(user.getUid() + searcharg);
        quotesdb.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final ArrayList<Quotes> myquotes = new ArrayList<>();
                myquotes.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Quotes q = d.getValue(Quotes.class);
                    myquotes.add(q);
                }
                final QuotesDB quotesDB = new QuotesDB();
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity)
                        .setTitle("Tem certeza?")
                        .setMessage("Você e suas " + myquotes.size() + " frases serão removidos para sempre! S E M P R E")
                        .setNeutralButton("Sim me tira daqui agora", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                final ProgressDialog progressDialog = new ProgressDialog(activity, R.style.Dialog_No_Border);
                                progressDialog.show();
                                for (Quotes quotes : myquotes) {
                                    quotesDB.Apagarconta(activity, quotes.getId());
                                }
                                progressDialog.setMessage("Apagando tudo...");
                                CountDownTimer timer = new CountDownTimer(2000, 100) {
                                    @Override
                                    public void onTick(long l) {

                                    }

                                    @Override
                                    public void onFinish() {
                                        progressDialog.dismiss();
                                    }
                                }.start();
                            }
                        }).setNegativeButton("Cliquei errado calma", null);
                alertDialog.show();


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




    private void changename() {
        Alert a = new Alert(activity);
        a.changename();
    }

    @Override
    protected void onResume() {
        changename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changename();
            }
        });



        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Snackbar snackbar = Snacky.builder().setActivity(activity).setBackgroundColor(Color.WHITE).setTextColor(activity.getResources().getColor(R.color.colorPrimaryDark)).build();
                snackbar.setText("Você saiu do aplicativo");
                snackbar.setDuration(BaseTransientBottomBar.LENGTH_INDEFINITE).show();
                CountDownTimer timer = new CountDownTimer(3500, 100) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        Intent intent = new Intent(getApplicationContext(), Splash.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }.start();


            }
        });


        deleteposts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Removeposts();
            }
        });

        deleteaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RemoverAccount();
            }
        });
        super.onResume();
    }
}
