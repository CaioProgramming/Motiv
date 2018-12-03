package com.creat.motiv.Database;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;

import com.creat.motiv.Beans.Likes;
import com.creat.motiv.Beans.Quotes;
import com.creat.motiv.R;
import com.creat.motiv.Utils.Pref;
import com.creat.motiv.Utils.Tools;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.mateware.snacky.Snacky;

public class QuotesDB {
    private DatabaseReference quotesdb;
    private Activity qActivity;

    public QuotesDB( Activity qActivity, Quotes quotes) {
        this.qActivity = qActivity;
        this.quotes = quotes;
    }

    public QuotesDB() {
    }

    public Quotes getQuotes() {
        return quotes;
    }

    public void setQuotes(Quotes quotes) {
        this.quotes = quotes;
    }

    private Quotes quotes;
    public static String path = "Quotes";
    public static String searcharg = "\uf8ff";
    public QuotesDB( Quotes quotes, @NonNull Activity activity) {
        this.qActivity = activity;
        this.quotesdb = FirebaseDatabase.getInstance().getReference(path);
        this.quotes = quotes;
    }
    private void cancelalarm(Context context) {
        Pref preferences = new Pref(context);
        preferences.setAlarm(false);
    }

    public void Inserir(){
        cancelalarm(qActivity);
        String id = quotesdb.push().getKey();
        this.quotes.setId(id);
        if (quotes.getQuote().isEmpty()){

            Snacky.builder().setActivity(qActivity).error().setText(Tools.emptyquote()).setDuration(5000).show();
        }else{
            quotesdb.child(id).setValue(this.quotes).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()){
                        CountDownTimer timer = new CountDownTimer(3000,100) {
                            @Override
                            public void onTick(long l) {

                            }

                            @Override
                            public void onFinish() {
                                Snacky.builder().setActivity(qActivity).setText("Frase salva!").success().show();
                            }
                        }.start();

                    }else{
                        Snacky.builder().setActivity(qActivity).error().setText("Erro " + task.getException().getMessage()).show();
                    }
                }
            });}
    }

    public void Editar(){
        cancelalarm(qActivity);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (quotes.getQuote().isEmpty()){

            Snacky.builder().setActivity(qActivity).error().setText(Tools.emptyquote()).setDuration(5000).show();
        }else{
            final ProgressDialog progressDialog = new ProgressDialog(qActivity);
            progressDialog.setTitle("Salvando");
            progressDialog.show();
            quotes.setUserphoto(String.valueOf(user.getPhotoUrl()));
            quotes.setUsername(user.getDisplayName());
            quotes.setUserID(user.getUid());
            System.out.println("Edited quote id " + this.quotes.getId());
            quotesdb.child(this.quotes.getId()).setValue(this.quotes).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()){
                        progressDialog.setTitle("Frase editada com sucesso!");
                        CountDownTimer timer = new CountDownTimer(3000,100) {
                            @Override
                            public void onTick(long l) {

                            }

                            @Override
                            public void onFinish() {
                                progressDialog.dismiss();
                            }
                        }.start();

                    }else{
                        Snacky.builder().setActivity(qActivity).error().setText("Erro " + task.getException().getMessage()).show();
                    }
                }
            });}
    }

    public void Denunciar(){
        this.quotes.setReport(true);
        DatabaseReference quotesdb = FirebaseDatabase.getInstance().getReference();
        quotesdb.child(path).child(this.quotes.getId()).child("report").setValue(this.quotes.isReport()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Snacky.builder().setActivity(qActivity).setText("A frase foi denúnciada e será analisada").success().show();

                }else{Snacky.builder().setActivity(qActivity).setText("Erro " + task.getException().getMessage()).success().show();}
            }
        });

    }

    public void like(){
         FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        quotesdb = FirebaseDatabase.getInstance().getReference(path);
        if (  this.quotes == null || user == null){
            Snacky.builder().setActivity(qActivity).error().setText("Objeto nulo!").show();
            return;

        }
        Likes likes = new Likes(user.getUid(),user.getDisplayName(),String.valueOf(user.getPhotoUrl()));
         quotesdb.child(this.quotes.getId()).child("likes").child(user.getUid()).setValue(likes).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Snacky.builder().setActivity(qActivity).setText("Frase curtida")
                        .setBackgroundColor(Color.WHITE).setTextColor(Color.BLACK).setIcon(R.drawable.ic_favorite_black_24dp).build().show();
            }
        });
    }

    public void deslike(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        quotesdb = FirebaseDatabase.getInstance().getReference(path);
        if (  this.quotes == null || user == null){
            Snacky.builder().setActivity(qActivity).error().setText("Objeto nulo!").show();
            return;

        }
        quotesdb.child(this.quotes.getId()).child("likes").child(user.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Snacky.builder().setActivity(qActivity).setText("Frase descurtida")
                        .setBackgroundColor(Color.WHITE).setTextColor(Color.RED).setIcon(R.drawable.ic_favorite_black_24dp).build().show();
            }
        });
    }




    public void AlterarFoto(final Activity activity,String id,String photourl ){


        cancelalarm(activity);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference quotesdb = FirebaseDatabase.getInstance().getReference();

        quotesdb.child(path).child(id).child("userphoto").setValue(photourl).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                    Snacky.builder().setActivity(activity).setText("Foto alterada, " + user.getDisplayName() + "!").
                            success().show();                    }else{
                    Snacky.builder().setActivity(activity).error().setText("Erro " + task.getException().getMessage()).show();
                }
            }
        });

    }

    public void Removerposts(final Activity activity,String id){


        cancelalarm(activity);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference quotesdb = FirebaseDatabase.getInstance().getReference();

        quotesdb.child(path).child(id).child("userphoto").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                    Snacky.builder().setActivity(activity).setText("Posts removidos" + user.getDisplayName() + "!").
                            success().show();                    }else{
                    Snacky.builder().setActivity(activity).error().setText("Erro " + task.getException().getMessage()).show();
                }
            }
        });

    }

    public void Apagarconta(final Activity activity,String id){


        cancelalarm(activity);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference quotesdb = FirebaseDatabase.getInstance().getReference();

        quotesdb.child(path).child(id).child("userphoto").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                }else{
                    Snacky.builder().setActivity(activity).error().setText("Erro " + task.getException().getMessage()).show();
                }
            }
        });

        user.delete();

    }



    public void AlterarNome(final Activity activity,String id){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference quotesdb = FirebaseDatabase.getInstance().getReference();
        quotesdb.child(path).child(id).child("username").setValue(user.getDisplayName()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Snacky.builder().setActivity(activity).success().setText("Alteração conluída").show();
                }else{
                    Snacky.builder().setActivity(activity).error().setText("Erro " + task.getException().getMessage()).show();
                }
            }
        });

    }












}
