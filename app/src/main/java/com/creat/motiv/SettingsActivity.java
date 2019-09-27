package com.creat.motiv;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.creat.motiv.Beans.Quotes;
import com.creat.motiv.Database.QuotesDB;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.mateware.snacky.Snacky;

import static com.creat.motiv.Database.QuotesDB.path;
import static com.creat.motiv.Database.QuotesDB.searcharg;

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
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });


    }

    private void Removeposts() {


        Query quotesdb = FirebaseDatabase.getInstance().getReference().child(path).orderByChild("userID")
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
        Query quotesdb = FirebaseDatabase.getInstance().getReference().child(path).orderByChild("userID")
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


    private void Alterarnome() {


        Query quotesdb = FirebaseDatabase.getInstance().getReference().child(path).orderByChild("userID")
                .startAt(user.getUid())
                .endAt(user.getUid() + searcharg);
        quotesdb.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Quotes> myquotes = new ArrayList<>();
                myquotes.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Quotes q = d.getValue(Quotes.class);
                    myquotes.add(q);
                }
                QuotesDB quotesDB = new QuotesDB();

                for (Quotes quotes : myquotes) {
                    quotesDB.AlterarNome(quotes.getId());
                }


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void changename() {
        final BottomSheetDialog namedialog = new BottomSheetDialog(this, R.style.Dialog_No_Border);
        namedialog.setContentView(R.layout.changename);
        final LinearLayout saving = namedialog.findViewById(R.id.saving);
        final TextView message = namedialog.findViewById(R.id.message);
        final LinearLayout namelayout = namedialog.findViewById(R.id.namelayout);
        Button changename = namedialog.findViewById(R.id.changename);
        Button cancel = namedialog.findViewById(R.id.cancel);
        final EditText username = namedialog.findViewById(R.id.username);
        username.setText(user.getDisplayName());
        username.requestFocus();
        namedialog.setCanceledOnTouchOutside(true);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                namedialog.dismiss();
            }
        });
        changename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(username.getText().toString()).build();
                user.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        saving.setVisibility(View.VISIBLE);
                        namelayout.setVisibility(View.GONE);
                        Alterarnome();
                        message.setText("Nome alterado com sucesso!");
                        CountDownTimer timer = new CountDownTimer(1500, 100) {
                            @Override
                            public void onTick(long l) {

                            }

                            @Override
                            public void onFinish() {
                                namedialog.dismiss();
                            }
                        }.start();
                    }
                });


            }
        });
        namedialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(username, InputMethodManager.SHOW_IMPLICIT);
            }
        });
        namedialog.show();
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
                snackbar.setDuration(Snacky.LENGTH_INDEFINITE).show();
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
