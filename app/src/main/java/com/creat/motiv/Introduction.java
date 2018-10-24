package com.creat.motiv;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.creat.motiv.Beans.Quotes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

import java.util.ArrayList;

import static com.creat.motiv.Database.QuotesDB.path;

public class Introduction extends IntroActivity{
    private Query quotesdb;
    ArrayList<Quotes> quotesArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        quotesdb = FirebaseDatabase.getInstance().getReference();
        quotesArrayList = new ArrayList<>();
        Carregar();
        criarslides();
    }

    private void Carregar() {

        quotesdb.keepSynced(true);


        quotesdb = FirebaseDatabase.getInstance().getReference().child(path);
        quotesdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                quotesArrayList.clear();
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
                        quotes.setLikes(q.getLikes());
                        quotes.setUsername(q.getUsername());
                        quotes.setUserphoto(q.getUserphoto());
                        if (q.getTextcolor() == 0|| q.getBackgroundcolor() == 0){
                            quotes.setTextcolor(Color.BLACK);
                            quotes.setBackgroundcolor(Color.WHITE);
                        }else{
                            quotes.setTextcolor(q.getTextcolor());
                            quotes.setBackgroundcolor(q.getBackgroundcolor());}
                        quotesArrayList.add(quotes);


                        System.out.println("Quotes " + quotesArrayList.size());

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println(databaseError.getMessage());
            }
        });

    }

    private void Comecar(){
        Intent i = new Intent(this,MainActivity.class);
        i.putExtra("novo",true);
        startActivity(i);

    }

    private void criarslides(){
        addSlide(new SimpleSlide.Builder()
                .title(R.string.Welcome)
                .description(R.string.titleone)
                .image(R.mipmap.ic_launcher)
                .background(R.color.white)
                .backgroundDark(R.color.grey_300)
                 .build());
            setButtonBackVisible(false);

        addSlide(new SimpleSlide.Builder()
                .title(R.string.app_name)
                .description("Já são mais de " + quotesArrayList.size() + "frases compartilhadas!")
                .image(R.drawable.undraw_missed_chances_k3cq)
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title(R.string.connected)
                .description(R.string.sync)
                .image(R.drawable.undraw_in_sync_xwsa)
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title(R.string.security)
                .description(R.string.safe)
                .image(R.drawable.security)
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title(R.string.you)
                .description(R.string.share)
                .image(R.drawable.undraw_wishes_icyp)
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .build());

        String nome = "estranho";
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        addSlide(new SimpleSlide.Builder()
                .title(R.string.Imagine)
                .description(R.string.create)
                .image(R.drawable.undraw_creativity_wqmm)
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .build());
        addSlide(new SimpleSlide.Builder()
                .title(R.string.start)
                .description("Agora que sabe onde se meteu" + nome +",é hora de explorar comunidade do motiv, veja o que os usuários estão compartilhando, compartilhe,explore!")
                .image(R.drawable.undraw_outer_space_3v6n)
                .background(R.color.white)
                .backgroundDark(R.color.black)
                .canGoForward(false)
                .buttonCtaClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Comecar();
                    }
                })
                .buttonCtaLabel("Iniciar")
                .build());

    }
}
