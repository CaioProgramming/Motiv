package com.creat.motiv.Database;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.creat.motiv.Adapters.RecyclerAdapter;
import com.creat.motiv.Beans.Likes;
import com.creat.motiv.Beans.Quotes;
import com.creat.motiv.R;
import com.creat.motiv.Splash;
import com.creat.motiv.Utils.Alert;
import com.creat.motiv.Utils.Tools;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import de.mateware.snacky.Snacky;

public class QuotesDB {
    private DatabaseReference quotesdb = Tools.quotesreference;
    private Activity activity;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public QuotesDB(Activity activity, Quotes quotes) {
        this.activity = activity;
        this.quotes = quotes;
    }

    public QuotesDB(Activity activity) {
        this.activity = activity;
    }

    public QuotesDB() {
    }



    private Quotes quotes;

    public QuotesDB( Quotes quotes, @NonNull Activity activity) {
        this.activity = activity;
        this.quotesdb = Tools.quotesreference;
        this.quotes = quotes;
    }


    public void Carregar(final RecyclerView composesrecycler, final SwipeRefreshLayout refreshLayout) {
        quotesdb.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Quotes> quotesArrayList = new ArrayList<>();
                quotesArrayList.clear();
                composesrecycler.removeAllViews();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Quotes quotes = new Quotes();
                    Quotes q = d.getValue(Quotes.class);
                    if (q != null) {
                        quotes.setId(q.getId());
                        quotes.setAuthor(q.getAuthor());
                        quotes.setQuote(q.getQuote());
                        quotes.setUserID(q.getUserID());
                        quotes.setCategoria(q.getCategoria());
                        quotes.setData(q.getData());
                        quotes.setUsername(q.getUsername());
                        quotes.setUserphoto(q.getUserphoto());
                        quotes.setReport(q.isReport());
                        if (q.getFont() != null) {

                            quotes.setFont(q.getFont());
                        } else {
                            quotes.setFont(null);
                        }


                        if (q.getTextcolor() == 0 || q.getBackgroundcolor() == 0) {
                            quotes.setTextcolor(Color.BLACK);
                            quotes.setBackgroundcolor(Color.WHITE);
                        } else {
                            quotes.setTextcolor(q.getTextcolor());
                            quotes.setBackgroundcolor(q.getBackgroundcolor());
                        }
                        quotesArrayList.add(quotes);

                        System.out.println("Quotes " + quotesArrayList.size());
                        System.out.println("Quote  " + quotes.getId());

                    }
                }
                Collections.reverse(quotesArrayList);
                composesrecycler.setVisibility(View.VISIBLE);
                GridLayoutManager llm = new GridLayoutManager(activity, Tools.spancount, GridLayoutManager.VERTICAL, false);
                composesrecycler.setHasFixedSize(true);
                System.out.println(quotesArrayList.size());

                RecyclerAdapter myadapter = new RecyclerAdapter(quotesArrayList, activity);
                composesrecycler.setAdapter(myadapter);
                composesrecycler.setLayoutManager(llm);
                refreshLayout.setRefreshing(false);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println(databaseError.getMessage());
            }
        });
    }

    public void CarregarUserQuotes(final ArrayList<Quotes> quotesArrayList, final RecyclerView composesrecycler, final TextView usercount) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        quotesdb.orderByChild("userID").equalTo(user.getUid());
        quotesdb.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                quotesArrayList.clear();
                composesrecycler.removeAllViews();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Quotes quotes = new Quotes();
                    Quotes q = d.getValue(Quotes.class);
                    if (q != null) {
                        quotes = q;
                        if (q.getFont() != null) {

                            quotes.setFont(q.getFont());
                        } else {
                            quotes.setFont(null);
                        }


                        if (q.getTextcolor() == 0 || q.getBackgroundcolor() == 0) {
                            quotes.setTextcolor(Color.BLACK);
                            quotes.setBackgroundcolor(Color.WHITE);
                        } else {
                            quotes.setTextcolor(q.getTextcolor());
                            quotes.setBackgroundcolor(q.getBackgroundcolor());
                        }

                        quotes.setId(d.getKey());
                        if (quotes.getUserID().equals(user.getUid())) {
                            quotesArrayList.add(quotes);
                        }

                        System.out.println("Quotes " + quotesArrayList.size());
                        System.out.println("Quote  " + quotes.getId());

                    }
                }
                Collections.reverse(quotesArrayList);
                composesrecycler.setVisibility(View.VISIBLE);
                GridLayoutManager llm = new GridLayoutManager(activity, Tools.spancount, GridLayoutManager.VERTICAL, false);
                composesrecycler.setHasFixedSize(true);
                System.out.println(quotesArrayList.size());

                RecyclerAdapter myadapter = new RecyclerAdapter(quotesArrayList, activity);
                composesrecycler.setAdapter(myadapter);
                composesrecycler.setLayoutManager(llm);
                usercount.setText(quotesArrayList.size() + " publicações");


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println(databaseError.getMessage());
            }
        });
    }

    public void Pesquisar(final String pesquisa, final RecyclerView composesrecycler) {


        quotesdb.startAt(pesquisa)
                .endAt(pesquisa + "\uf8ff");
        quotesdb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Quotes> quotesArrayList = new ArrayList<>();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Quotes quotes = new Quotes();
                    Quotes q = d.getValue(Quotes.class);
                    if (q != null) {
                        quotes.setId(d.getKey());
                        quotes.setAuthor(q.getAuthor());
                        quotes.setQuote(q.getQuote());
                        quotes.setUserID(q.getUserID());
                        quotes.setCategoria(q.getCategoria());
                        quotes.setData(q.getData());
                        quotes.setUsername(q.getUsername());
                        quotes.setUserphoto(q.getUserphoto());
                        if (q.getFont() != null) {
                            quotes.setFont(q.getFont());
                        } else {
                            quotes.setFont(null);
                        }


                        if (q.getTextcolor() == 0 || q.getBackgroundcolor() == 0) {
                            quotes.setTextcolor(Color.BLACK);
                            quotes.setBackgroundcolor(Color.WHITE);
                        } else {
                            quotes.setTextcolor(q.getTextcolor());
                            quotes.setBackgroundcolor(q.getBackgroundcolor());
                        }
                        quotesArrayList.add(quotes);
                        if (q.getUserID().equals(user.getUid())) {
                            quotes.setUsername(user.getDisplayName());
                            quotes.setUserphoto(String.valueOf(user.getPhotoUrl()));
                        }
                        System.out.println("Quotes search " + quotesArrayList.size());

                    }
                }


                if (quotesArrayList.size() > 0) {
                    composesrecycler.setVisibility(View.VISIBLE);
                    GridLayoutManager llm = new GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false);
                    composesrecycler.setHasFixedSize(true);
                    System.out.println(quotesArrayList.size());
                    RecyclerAdapter myadapter = new RecyclerAdapter(quotesArrayList, activity);
                    myadapter.notifyDataSetChanged();
                    composesrecycler.setAdapter(myadapter);
                    composesrecycler.setLayoutManager(llm);

                } else {
                    PesquisarAuthor(pesquisa, composesrecycler);

                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Erro " + databaseError.getMessage());
            }
        });


    }

    private void PesquisarAuthor(final String pesquisa, final RecyclerView composesrecycler) {

        quotesdb.orderByChild("author").startAt(pesquisa)
                .endAt(pesquisa + "\uf8ff");
        quotesdb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Quotes> quotesArrayList = new ArrayList<>();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Quotes quotes = new Quotes();
                    Quotes q = d.getValue(Quotes.class);
                    if (q != null) {
                        quotes.setId(d.getKey());
                        quotes.setAuthor(q.getAuthor());
                        quotes.setQuote(q.getQuote());
                        quotes.setUserID(q.getUserID());
                        quotes.setCategoria(q.getCategoria());
                        quotes.setData(q.getData());
                        quotes.setUsername(q.getUsername());
                        quotes.setUserphoto(q.getUserphoto());
                        if (q.getFont() != null) {
                            quotes.setFont(q.getFont());
                        } else {
                            quotes.setFont(null);
                        }


                        if (q.getTextcolor() == 0 || q.getBackgroundcolor() == 0) {
                            quotes.setTextcolor(Color.BLACK);
                            quotes.setBackgroundcolor(Color.WHITE);
                        } else {
                            quotes.setTextcolor(q.getTextcolor());
                            quotes.setBackgroundcolor(q.getBackgroundcolor());
                        }
                        quotesArrayList.add(quotes);
                        if (q.getUserID().equals(user.getUid())) {
                            quotes.setUsername(user.getDisplayName());
                            quotes.setUserphoto(String.valueOf(user.getPhotoUrl()));
                        }

                        System.out.println("Quotes search " + quotesArrayList.size());

                    }
                }

                if (quotesArrayList.size() > 0) {
                    composesrecycler.setVisibility(View.VISIBLE);
                    GridLayoutManager llm = new GridLayoutManager(activity, Tools.spancount, GridLayoutManager.VERTICAL, false);
                    composesrecycler.setHasFixedSize(true);
                    System.out.println(quotesArrayList.size());
                    RecyclerAdapter myadapter = new RecyclerAdapter(quotesArrayList, activity);
                    myadapter.notifyDataSetChanged();
                    composesrecycler.setAdapter(myadapter);
                    composesrecycler.setLayoutManager(llm);
                } else {
                    PesquisarUsuario(pesquisa, composesrecycler);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Erro " + databaseError.getMessage());
            }
        });

    }

    private void PesquisarUsuario(final String pesquisa, final RecyclerView composesrecycler) {

        quotesdb.orderByChild("author").startAt(pesquisa)
                .endAt(pesquisa + "\uf8ff");
        quotesdb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Quotes> quotesArrayList = new ArrayList<>();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Quotes quotes = new Quotes();
                    Quotes q = d.getValue(Quotes.class);
                    if (q != null) {
                        quotes.setId(d.getKey());
                        quotes.setAuthor(q.getAuthor());
                        quotes.setQuote(q.getQuote());
                        quotes.setUserID(q.getUserID());
                        quotes.setCategoria(q.getCategoria());
                        quotes.setData(q.getData());
                        quotes.setUsername(q.getUsername());
                        quotes.setUserphoto(q.getUserphoto());
                        if (q.getFont() != null) {
                            quotes.setFont(q.getFont());
                        } else {
                            quotes.setFont(null);
                        }


                        if (q.getTextcolor() == 0 || q.getBackgroundcolor() == 0) {
                            quotes.setTextcolor(Color.BLACK);
                            quotes.setBackgroundcolor(Color.WHITE);
                        } else {
                            quotes.setTextcolor(q.getTextcolor());
                            quotes.setBackgroundcolor(q.getBackgroundcolor());
                        }
                        quotesArrayList.add(quotes);
                        if (q.getUserID().equals(user.getUid())) {
                            quotes.setUsername(user.getDisplayName());
                            quotes.setUserphoto(String.valueOf(user.getPhotoUrl()));
                        }

                        System.out.println("Quotes search " + quotesArrayList.size());

                    }
                }

                if (quotesArrayList.size() > 0) {
                    composesrecycler.setVisibility(View.VISIBLE);
                    GridLayoutManager llm = new GridLayoutManager(activity, Tools.spancount, GridLayoutManager.VERTICAL, false);
                    composesrecycler.setHasFixedSize(true);
                    System.out.println(quotesArrayList.size());
                    RecyclerAdapter myadapter = new RecyclerAdapter(quotesArrayList, activity);
                    myadapter.notifyDataSetChanged();
                    composesrecycler.setAdapter(myadapter);
                    composesrecycler.setLayoutManager(llm);
                } else {
                    Categories(pesquisa, composesrecycler);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Erro " + databaseError.getMessage());
            }
        });


    }


    private void Categories(String categorie, final RecyclerView composesrecycler) {

        quotesdb.orderByChild("categoria").startAt(categorie)
                .endAt(categorie + "\uf8ff");
        quotesdb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Quotes> quotesArrayList = new ArrayList<>();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Quotes quotes = new Quotes();
                    Quotes q = d.getValue(Quotes.class);
                    if (q != null) {
                        quotes.setId(d.getKey());
                        quotes.setAuthor(q.getAuthor());
                        quotes.setQuote(q.getQuote());
                        quotes.setUserID(q.getUserID());
                        quotes.setCategoria(q.getCategoria());
                        quotes.setData(q.getData());
                        quotes.setUsername(q.getUsername());
                        quotes.setUserphoto(q.getUserphoto());
                        if (q.getFont() != null) {
                            quotes.setFont(q.getFont());
                        } else {
                            quotes.setFont(null);
                        }


                        if (q.getTextcolor() == 0 || q.getBackgroundcolor() == 0) {
                            quotes.setTextcolor(Color.BLACK);
                            quotes.setBackgroundcolor(Color.WHITE);
                        } else {
                            quotes.setTextcolor(q.getTextcolor());
                            quotes.setBackgroundcolor(q.getBackgroundcolor());
                        }
                        quotesArrayList.add(quotes);
                        if (q.getUserID().equals(user.getUid())) {
                            quotes.setUsername(user.getDisplayName());
                            quotes.setUserphoto(String.valueOf(user.getPhotoUrl()));
                        }

                        System.out.println("Quotes search " + quotesArrayList.size());

                    }
                }
                composesrecycler.setVisibility(View.VISIBLE);
                GridLayoutManager llm = new GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false);
                composesrecycler.setHasFixedSize(true);
                System.out.println(quotesArrayList.size());
                RecyclerAdapter myadapter = new RecyclerAdapter(quotesArrayList, activity);
                myadapter.notifyDataSetChanged();
                composesrecycler.setAdapter(myadapter);
                composesrecycler.setLayoutManager(llm);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Erro " + databaseError.getMessage());
            }
        });
    }





    public void Inserir(){
        final Alert alert = new Alert(activity);
        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Salvando");
        progressDialog.setIndeterminate(true);
        String id = quotesdb.push().getKey();
        this.quotes.setId(id);
        if (quotes.getQuote().isEmpty()){
            alert.Message(activity.getDrawable(R.drawable.ic_error), Tools.emptyquote());
            return;
        }else{
            if (id != null) {
                quotesdb.child(id).setValue(this.quotes).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()){
                            alert.Message(activity.getDrawable(R.drawable.ic_success), "Sua frase foi compartilhada!");
                        }else{
                            alert.Message(activity.getDrawable(R.drawable.ic_success), "Erro ao publicar! \n" + task.getException().getMessage());

                        }
                    }
                });
            }
        }
        progressDialog.show();
    }

    public void Editar(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (quotes.getQuote().isEmpty()){

            Snacky.builder().setActivity(activity).error().setText(Tools.emptyquote()).setDuration(5000).show();
        }else{
            final ProgressDialog progressDialog = new ProgressDialog(activity);
            progressDialog.setTitle("Salvando");
            progressDialog.show();
            if (user != null) {
                quotes.setUserphoto(String.valueOf(user.getPhotoUrl()));
            }
            if (user != null) {
                quotes.setUsername(user.getDisplayName());
            }
            assert user != null;
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
                        };timer.start();

                    }else{
                        Snacky.builder().setActivity(activity).error().setText("Erro " + Objects.requireNonNull(task.getException()).getMessage()).show();
                    }
                }
            });}
    }

    public void Denunciar(){
        this.quotes.setReport(true);
        DatabaseReference quotesdb = Tools.quotesreference;
        quotesdb.child(this.quotes.getId()).child("report").setValue(this.quotes.isReport()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Alert a = new Alert(activity);


                if (task.isSuccessful()){
                    a.Message(a.succesicon,"Frase denunciada com sucesso");
                } else {
                    a.Message(a.erroricon,"Erro ao processar denuncia..." + task.getException().getMessage());
                }
            }
        });

    }

    public void like(){
         FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        quotesdb = Tools.quotesreference;
        if (  this.quotes == null || user == null){
            Snacky.builder().setActivity(activity).error().setText("Objeto nulo!").show();
            return;

        }
        Likes likes = new Likes(user.getUid(),user.getDisplayName(),String.valueOf(user.getPhotoUrl()));
         quotesdb.child(this.quotes.getId()).child("likes").child(user.getUid()).setValue(likes).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Snacky.builder().setActivity(activity).setText("Frase curtida")
                        .setBackgroundColor(Color.WHITE).setTextColor(Color.BLACK).setIcon(R.drawable.ic_favorite_black_24dp).build().show();
            }
        });
    }

    public void deslike(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        quotesdb = Tools.quotesreference;
        if (  this.quotes == null || user == null){
            Snacky.builder().setActivity(activity).error().setText("Objeto nulo!").show();
            return;

        }
        quotesdb.child(this.quotes.getId()).child("likes").child(user.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Snacky.builder().setActivity(activity).setText("Frase descurtida")
                        .setBackgroundColor(Color.WHITE).setTextColor(Color.RED).setIcon(R.drawable.ic_favorite_black_24dp).build().show();
            }
        });
    }


    public void AlterarFoto(Quotes quote) {

        Log.println(Log.INFO, "User", "Changing quote: " + quote.getId());
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        quote.setUserphoto(String.valueOf(user.getPhotoUrl()));
        DatabaseReference quotesdb = FirebaseDatabase.getInstance().getReference();
        quotesdb.child(quote.getId()).setValue(quote);

    }

    public void Removerposts(final Activity activity,String id){


        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference quotesdb = FirebaseDatabase.getInstance().getReference();

        quotesdb.child(id).removeValue();

    }

    public void Apagarconta(final Activity activity, final String id) {


        DatabaseReference quotesdb = FirebaseDatabase.getInstance().getReference();

        quotesdb.child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Snacky.builder().setActivity(activity).success().setText("Conta apagada").show();
                    FirebaseAuth.getInstance().signOut();
                    if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                        Snackbar snackbar = Snacky.builder().setActivity(activity).build();
                        snackbar.setText("Você saiu do aplicativo");
                        snackbar.setDuration(5000).show();

                        if (!snackbar.isShown()) {
                            Intent intent = new Intent(activity, Splash.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            activity.startActivity(intent);
                        }
                    }
                } else {
                    Snacky.builder().setActivity(activity).error().setText("Erro " + Objects.requireNonNull(task.getException()).getMessage()).show();
                }
            }
        });


    }

    private void Apagarlikes(final Activity activity, String id) {


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference quotesdb = FirebaseDatabase.getInstance().getReference();

        assert user != null;
        quotesdb.child("likes").child(user.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    Snacky.builder().setActivity(activity).error().setText("Erro " + Objects.requireNonNull(task.getException()).getMessage()).show();
                }
            }
        });


        user.delete();
        FirebaseAuth.getInstance().signOut();

    }


    public void AlterarNome(final String id) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference quotesdb = FirebaseDatabase.getInstance().getReference();
        assert user != null;
        quotesdb.child(id).child("username").setValue(user.getDisplayName()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                System.out.println("quote" + id + "username changed to: " + user.getDisplayName());
            }
        });

    }












}
