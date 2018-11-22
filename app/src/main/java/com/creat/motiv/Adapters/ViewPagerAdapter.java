package com.creat.motiv.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.creat.motiv.Beans.Quotes;
import com.creat.motiv.MainActivity;
import com.creat.motiv.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.creat.motiv.Database.QuotesDB.path;

public class ViewPagerAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;
    private Query quotesdb;
    ArrayList<Quotes> quotesArrayList;
    private static FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    public int[] slide_images = {
            R.mipmap.ic_launcher,
            R.drawable.undraw_missed_chances_k3cq,
            R.drawable.undraw_in_sync_xwsa,
            R.drawable.security,
            R.drawable.undraw_wishes_icyp,
            R.drawable.undraw_creativity_wqmm,
            R.drawable.undraw_outer_space_3v6n
    };

    public String[] slide_titles = {
            "Bem-vindo ao Motiv",
            "Motiv",
            "Sempre conectado",
            "Segurança",
            "Seu mundo",
            "Imagine...",
            "Hora de começar!"
    };

    public String[] slide_text = {

            "A sua rede social para os amantes da poesia,você é livre para expressar-se com suas palavras!",
            "O Motiv é sincronizado em tempo real,sempre que um novo usuário posta algo,você pode visualizar no mesmo momento.",
            "Seus dados estão seguros e não são compartilhados, fique tranquilo é um ambiente seguro.",
            "Compartilhe tudo o que imaginar, esse espaço é seu!",
            "Use a imaginação e criatividade sem medo com a ferramenta de edição.",
            "Já são inúmeras  frases compartilhadas no mundo!",
            "Agora que sabe onde se meteu " + user.getDisplayName() + ",é hora de explorar comunidade do motiv, veja o que os usuários estão compartilhando, compartilhe,explore!"
    };

    public ViewPagerAdapter(Context context) {
        this.context = context;
        quotesArrayList = new ArrayList<>();
        Carregar();


    }

    private void Carregar() {




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
                        if (q.getTextcolor() == 0 || q.getBackgroundcolor() == 0) {
                            quotes.setTextcolor(Color.BLACK);
                            quotes.setBackgroundcolor(Color.WHITE);
                        } else {
                            quotes.setTextcolor(q.getTextcolor());
                            quotes.setBackgroundcolor(q.getBackgroundcolor());
                        }
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


    @Override
    public int getCount() {
        return slide_titles.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.newuser, container, false);
        TextView text = view.findViewById(R.id.text);
        RelativeLayout layout = view.findViewById(R.id.layout);
        TextView textView2 = view.findViewById(R.id.textView2);
        ImageView imageView = view.findViewById(R.id.imageView);
        Button start = view.findViewById(R.id.start);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Comecar();
            }
        });
        final Animation myanim = AnimationUtils.loadAnimation(context, R.anim.fade_in);

        view.startAnimation(myanim);
        if (position > 0) {
            Glide.with(context).load(slide_images[position]).into(imageView);

        }


        text.setText(slide_text[position]);
        textView2.setText(slide_titles[position]);
        if (position == 6) { text.setText("Já são " + quotesArrayList.size() + " frases postadas no motiv");}

        container.addView(view);

        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)(object));
    }

    private void Comecar(){
        Intent i = new Intent(context,MainActivity.class);
        i.putExtra("novo",true);
        context.startActivity(i);

    }

}
