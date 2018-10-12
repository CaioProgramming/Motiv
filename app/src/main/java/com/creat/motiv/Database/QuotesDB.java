package com.creat.motiv.Database;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.creat.motiv.Beans.Quotes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.mateware.snacky.Snacky;

public class QuotesDB {
    private DatabaseReference quotesdb;
    private Activity qActivity;

    public Quotes getQuotes() {
        return quotes;
    }

    public void setQuotes(Quotes quotes) {
        this.quotes = quotes;
    }

    private Quotes quotes;
    public static String path = "Quotes";
    private ArrayList<Quotes> quotesArrayList;

    public QuotesDB( Quotes quotes, @NonNull Activity activity) {
        this.qActivity = activity;
        this.quotesdb = FirebaseDatabase.getInstance().getReference();
        this.quotes = quotes;
        quotesArrayList = new ArrayList<>();
    }

    public void Inserir(){
        String id = quotesdb.push().getKey();
        this.quotes.setId(id);
        quotesdb.child(path).child(id).setValue(this.quotes).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                Snacky.builder().setActivity(qActivity).success().setText("Frase adicionada com sucesso!").show();
                }else{
                    Snacky.builder().setActivity(qActivity).error().setText("Erro " + task.getException().getMessage()).show();
                }
            }
        });
    }

    public void Remover(){

        quotesdb.child(quotes.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Snacky.builder().setActivity(qActivity).success().setText("Frase removida com sucesso!").show();
                }else{
                    Snacky.builder().setActivity(qActivity).error().setText("Erro " + task.getException().getMessage()).show();
                }
            }
        });
    }

    public void Alterar(){
        quotesdb.child(quotes.getId()).setValue(this.quotes).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Snacky.builder().setActivity(qActivity).success().setText("Frase alterada com sucesso!").show();
                }else{
                    Snacky.builder().setActivity(qActivity).error().setText("Erro " + task.getException().getMessage()).show();
                }
            }
        });
    }

    public void Like(){
        quotesdb.child(quotes.getId()).setValue(this.quotes).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Snacky.builder().setActivity(qActivity).success().setText("Frase adicionada com sucesso!").show();
                }else{
                    Snacky.builder().setActivity(qActivity).error().setText("Erro " + task.getException().getMessage()).show();
                }
            }
        });
    }

    public ArrayList<Quotes> listar(){
        final ArrayList<Quotes> Quoteslist = new ArrayList<>();
         quotesdb = FirebaseDatabase.getInstance().getReference().child(path);
         quotesdb.keepSynced(true);
        quotesdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot d: dataSnapshot.getChildren()){
                    Quotes quotes = new Quotes();

                    Quotes q = d.getValue(Quotes.class);
                    if (q!=null){
                        quotes.setId(d.getKey());
                        quotes.setAuthor(q.getAuthor());
                        quotes.setQuote(q.getQuote());
                        quotes.setUserID(q.getUserID());
                        quotes.setCategoria(q.getCategoria());
                        quotes.setData(q.getData());
                        quotes.setLikes(q.getLikes());
                        Quoteslist.add(quotes);
                        System.out.println("Quotes " + Quoteslist.size());

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                   Snacky.builder().setActivity(qActivity).error().setText("Erro " + databaseError.getMessage()).show();
            }
        });




        return  quotesArrayList;
    }

    public ArrayList<Quotes> listarUsuario(){
        quotesArrayList.clear();
        quotesdb.keepSynced(true);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            quotesdb.child("quotes").orderByChild("userID").equalTo(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot d: dataSnapshot.getChildren()){
                        Quotes q = d.getValue(Quotes.class);
                        Quotes quotes = new Quotes();
                        if (q!=null){
                            quotes.setId(d.getKey());
                            quotes.setQuote(q.getQuote());
                            quotes.setAuthor(q.getAuthor());
                            quotes.setUserID(q.getUserID());
                            quotes.setCategoria(q.getCategoria());
                            quotes.setData(q.getData());
                            quotes.setLikes(q.getLikes());
                            if (quotes.getUserID().equals(user.getUid()))
                            quotesArrayList.add(quotes);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Snacky.builder().setActivity(qActivity).error().setText("Erro " + databaseError.getMessage());
                }
            });
        }


        return  quotesArrayList;
    }

    public ArrayList<Quotes> listarByCategoria(final String categoria){
        final ArrayList<Quotes> quotesArrayList = new ArrayList<>();
        quotesdb.child(path).orderByChild("categoria").equalTo(categoria).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot d: dataSnapshot.getChildren()){
                    Quotes q = d.getValue(Quotes.class);
                    Quotes quotes = new Quotes();
                    if (q!=null){
                        quotes.setId(d.getKey());
                        quotes.setAuthor(q.getAuthor());
                        quotes.setUserID(q.getUserID());
                        quotes.setCategoria(q.getCategoria());
                        quotes.setData(q.getData());
                        quotes.setLikes(q.getLikes());
                        if (quotes.getCategoria().equals(categoria)){
                            quotesArrayList.add(quotes);}
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Snacky.builder().setActivity(qActivity).error().setText("Erro " + databaseError.getMessage());
            }
        });




        return  quotesArrayList;
    }






}
