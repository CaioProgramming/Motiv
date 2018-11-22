package com.creat.motiv;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.creat.motiv.Beans.Quotes;
import com.creat.motiv.Database.QuotesDB;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.creat.motiv.Database.QuotesDB.path;
import static com.creat.motiv.Database.QuotesDB.searcharg;

public class Updateuseract extends AppCompatActivity {

    private android.widget.TextView message;
    private android.widget.ImageView loadingif;
    FirebaseUser user;
    ArrayList<Quotes> myquotes = new ArrayList<>();
    Activity activity;
    String nome = "";
    String photouri = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (getIntent().getStringExtra("photouri") != null){
            photouri = getIntent().getStringExtra("photouri");
        }
        if (getIntent().getStringExtra("nome") != null){
            nome = getIntent().getStringExtra("nome");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateuseract);
        this.loadingif = findViewById(R.id.loadingif);
        this.message = findViewById(R.id.message);
        activity = this;
        Glide.with(this)
                .asGif()
                .load("https://firebasestorage.googleapis.com/v0/b/motiv-d16d1.appspot.com/o/flying.gif?alt=media&token=47076e17-367e-4e23-abf9-f0cec1717469")
                .into(loadingif);


        carregar();
        user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileChangeRequest;

        if (!nome.equals("")){
            profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(nome).build();
            if (user != null) {
                user.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            for (int i = 0; i< myquotes.size(); i++){

                                    QuotesDB quotesDB = new QuotesDB();
                                    quotesDB.AlterarNome(activity, myquotes.get(i).getId());
                            }
                            message.setText(R.string.sucess_update);
                            CountDownTimer timer = new CountDownTimer(6000,100) {
                                @Override
                                public void onTick(long l) {

                                }

                                @Override
                                public void onFinish() {
                                    Intent back = new Intent(activity,Splash.class);
                                    back.putExtra("novo",false);
                                    startActivity(back);
                                    finish();

                                }
                            }.start();

                        }
                    }
                });
            }
        }

        if (!photouri.equals("")){
            profileChangeRequest = new UserProfileChangeRequest.Builder().setPhotoUri(Uri.parse(photouri)).build();
            if (user != null) {
                user.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            for (int i = 0; i< myquotes.size(); i++){
                                carregar();
                                    QuotesDB quotesDB = new QuotesDB();
                                    quotesDB.AlterarFoto(activity, myquotes.get(i).getId(), String.valueOf(user.getPhotoUrl()));
                            }
                            message.setText(R.string.sucess_update);
                            CountDownTimer timer = new CountDownTimer(6000,100) {
                                @Override
                                public void onTick(long l) {

                                }

                                @Override
                                public void onFinish() {
                                    Intent back = new Intent(activity,Splash.class);
                                    back.putExtra("novo",false);
                                    startActivity(back);
                                    finish();


                                }
                            }.start();

                        }
                    }
                });
            }
        }








    }

    private void carregar(){
         Query quotesdb;
        quotesdb = FirebaseDatabase.getInstance().getReference().child(path).orderByChild("userID")
                .startAt(user.getUid())
                .endAt(user.getUid() + searcharg);
        quotesdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myquotes.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Quotes quotes = new Quotes();
                    Quotes q = d.getValue(Quotes.class);
                    if (q != null) {
                        quotes.setId(d.getKey());
                        quotes.setAuthor(q.getAuthor());
                        quotes.setQuote(q.getQuote());
                        quotes.setUserID(q.getUserID());
                        quotes.setUserphoto(q.getUserphoto());
                        quotes.setUsername(q.getUsername());
                        quotes.setCategoria(q.getCategoria());
                        quotes.setData(q.getData());
                        quotes.setReport(q.isReport());
                        quotes.setLikes(q.getLikes());
                        quotes.setTextcolor(q.getTextcolor());
                        quotes.setBackgroundcolor(q.getBackgroundcolor());
                        myquotes.add(quotes);
                        System.out.println("Your quotes " + myquotes.size());

                    }
                    }
                }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
