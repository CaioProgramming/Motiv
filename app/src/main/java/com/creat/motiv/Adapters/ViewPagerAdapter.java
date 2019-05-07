package com.creat.motiv.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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
import com.creat.motiv.Beans.Pics;
import com.creat.motiv.Beans.Quotes;
import com.creat.motiv.MainActivity;
import com.creat.motiv.R;
import com.creat.motiv.Utils.Pref;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import de.mateware.snacky.Snacky;

import static com.creat.motiv.Database.QuotesDB.path;

public class ViewPagerAdapter extends PagerAdapter {
    Dialog m_dialog;
    String uri;
    private Context context;
    private Activity activity;
    private ArrayList<Quotes> quotesArrayList;
    private Query quotesdb;
    private static FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    private int[] slide_images = {
            R.mipmap.ic_launcher,
            R.drawable.undraw_missed_chances_k3cq,
            R.drawable.undraw_in_sync_xwsa,
            R.drawable.security,
            R.drawable.undraw_wishes_icyp,
            R.drawable.undraw_creativity_wqmm,
            R.drawable.undraw_outer_space_3v6n
    };

    private String[] slide_titles = {
            "Bem-vindo ao Motiv",
            "Motiv",
            "Sempre conectado",
            "Segurança",
            "Seu mundo",
            "Imagine...",
            "Hora de começar!"
    };

    private String[] slide_text = {

            "A sua rede social para os amantes da poesia,você é livre para expressar-se com suas palavras!",
            "O Motiv é sincronizado em tempo real,sempre que um novo usuário posta algo,você pode visualizar no mesmo momento.",
            "Seus dados estão seguros e não são compartilhados, fique tranquilo é um ambiente seguro.",
            "Compartilhe tudo o que imaginar, esse espaço é seu!",
            "Use a imaginação e criatividade sem medo com a ferramenta de edição.",
            "Já são inúmeras  frases compartilhadas no mundo!",
            "Agora que sabe onde se meteu " + user.getDisplayName() + ",é hora de explorar comunidade do motiv, veja o que os usuários estão compartilhando, compartilhe,explore!"
    };

    public ViewPagerAdapter(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
        quotesArrayList = new ArrayList<>();
        Carregar();


    }

    private Pref preferences;

    private void Carregar() {


        Query quotesdb = FirebaseDatabase.getInstance().getReference().child(path);
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
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = null;
        if (layoutInflater != null) {
            view = layoutInflater.inflate(R.layout.newuser, container, false);
        }
        assert view != null;
        TextView politics = view.findViewById(R.id.politics);
        TextView text = view.findViewById(R.id.text);
        TextView textView2 = view.findViewById(R.id.textView2);
        ImageView imageView = view.findViewById(R.id.imageView);
        Button start = view.findViewById(R.id.start);
        politics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Comecar();
            }
        });
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
        if (position < 6){
            politics.setVisibility(View.GONE);
            start.setVisibility(View.GONE);
        }else{
            start.setVisibility(View.VISIBLE);
            politics.setVisibility(View.VISIBLE);

        }


        text.setText(slide_text[position]);
        textView2.setText(slide_titles[position]);
        if (position == 6) {
            text.setText(MessageFormat.format("Já são {0} frases postadas no motiv", quotesArrayList.size()));


            final Intent i = new Intent(context, MainActivity.class);

            preferences = new Pref(context);
            m_dialog = new Dialog(activity, R.style.Dialog_No_Border);
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

            if (preferences.agreestate()) {
                start.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        i.putExtra("novo", true);
                        context.startActivity(i);
                        activity.finish();
                    }
                });
            } else {
                m_dialog.show();
                Snacky.builder().setActivity(activity).warning()
                        .setText("Você precisa concordar com os termos de uso para prosseguir")
                        .setAction("Ok", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                m_dialog.show();
                            }
                        }).show();
            }

        }
        container.addView(view);

        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)(object));
    }


    private void Comecar(){
        final Intent i = new Intent(context, MainActivity.class);

        preferences = new Pref(context);
        m_dialog = new Dialog(activity, R.style.Dialog_No_Border);
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
        if (preferences.agreestate()) {

            quotesdb = FirebaseDatabase.getInstance().getReference();
            quotesdb.keepSynced(false);

            quotesdb = FirebaseDatabase.getInstance().getReference().child("images");

            quotesdb.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Random random = new Random();
                    int questionCount = (int) dataSnapshot.getChildrenCount();
                    int rand = random.nextInt(questionCount);
                    Iterator itr = dataSnapshot.getChildren().iterator();
                    for (int i = 0; i < rand; i++) {
                        itr.next();
                    }
                    DataSnapshot childSnapshot = (DataSnapshot) itr.next();

                    Pics p = childSnapshot.getValue(Pics.class);
                    uri = p.getUri();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setPhotoUri(Uri.parse(uri)).build();
            user.updateProfile(profileChangeRequest);
            i.putExtra("novo", true);
            context.startActivity(i);
            activity.finish();
        } else {
            Snacky.builder().setActivity(activity).warning()
                    .setText("Você precisa concordar com os termos de uso para prosseguir")
                    .setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            m_dialog.show();
                        }
                    }).show();
        }


    }

}
