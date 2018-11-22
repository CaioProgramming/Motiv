package com.creat.motiv.Fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import com.creat.motiv.Adapters.RecyclerAdapter;
import com.creat.motiv.Beans.Quotes;
import com.creat.motiv.R;
import com.creat.motiv.Utils.Pref;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.creat.motiv.Database.QuotesDB.path;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {


    private android.widget.EditText search;
    private android.support.v7.widget.Toolbar searchtool;

    private android.support.v7.widget.RecyclerView searchresult;
    Pref preferences;
    FirebaseUser user;
    private ArrayList<Quotes> quotesearch;
    private Query quotesdb;
    private android.widget.ImageView music;
    private android.widget.ImageView motivation;
    private android.widget.ImageView love;
    private android.widget.ImageView citation;
    private android.widget.GridLayout categoriesgrid;
    private android.widget.ImageButton close;


    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        quotesearch = new ArrayList<>();
        quotesdb = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        this.close = (ImageButton) view.findViewById(R.id.close);
        this.categoriesgrid = view.findViewById(R.id.categoriesgrid);
        this.citation = view.findViewById(R.id.citation);
        this.love = view.findViewById(R.id.love);
        this.motivation = view.findViewById(R.id.motivation);
        this.music = view.findViewById(R.id.music);
        searchresult = view.findViewById(R.id.searchresult);
        searchtool = view.findViewById(R.id.searchtool);
        search = view.findViewById(R.id.search);
        ((AppCompatActivity)getActivity()).setSupportActionBar(searchtool);
        preferences = new Pref(getContext());
        quotesdb.keepSynced(true);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation out = AnimationUtils.loadAnimation(getContext(),R.anim.fab_scale_down);
                Animation in = AnimationUtils.loadAnimation(getContext(),R.anim.fab_scale_up);
                search.getText().clear();
                categoriesgrid.setVisibility(View.VISIBLE);
                categoriesgrid.startAnimation(in);
                searchresult.startAnimation(out);
                searchresult.setVisibility(View.GONE);
                quotesearch.clear();

             }
        });


        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Categories("Musica");
            }
        });

        citation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Categories("Citação");
            }
        });

        love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Categories("Amor");
            }
        });

        motivation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Categories("Motivação");
            }
        });


        if (preferences.nightmodestate()){
            view.setBackgroundResource(R.drawable.gradnight);
            searchtool.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));


        }


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Animation out = AnimationUtils.loadAnimation(getContext(), R.anim.pop_out);
                Animation in = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_top);

                if (!editable.toString().isEmpty()){

                    categoriesgrid.startAnimation(out);
                    categoriesgrid.setVisibility(View.GONE);
                    searchresult.setVisibility(View.VISIBLE);
                    searchresult.startAnimation(in);
                    Pesquisar(editable.toString());
                }else{
                    Pesquisar("");
                }
            }
        });

        return view;
    }

    private void Categories(String categorie) {
        quotesdb = FirebaseDatabase.getInstance().getReference().child(path).orderByChild("categoria")
                .startAt(categorie)
                .endAt(categorie + "\uf8ff");
        quotesdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                quotesearch.clear();
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
                        quotesearch.add(quotes);
                        if (q.getUserID().equals(user.getUid())) {
                            quotes.setUsername(user.getDisplayName());
                            quotes.setUserphoto(String.valueOf(user.getPhotoUrl()));
                        }

                        System.out.println("Quotes search " + quotesearch.size());

                    }
                }
                Animation out = AnimationUtils.loadAnimation(getContext(), R.anim.pop_out);
                Animation in = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_top);
                categoriesgrid.startAnimation(out);
                categoriesgrid.setVisibility(View.GONE);
                searchresult.setVisibility(View.VISIBLE);
                GridLayoutManager llm = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
                searchresult.setHasFixedSize(true);
                System.out.println(quotesearch.size());
                RecyclerAdapter myadapter = new RecyclerAdapter(getContext(), quotesearch, getActivity());
                myadapter.notifyDataSetChanged();
                searchresult.setAdapter(myadapter);
                searchresult.setLayoutManager(llm);
                searchresult.startAnimation(in);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Erro " + databaseError.getMessage());
            }
        });
    }

    private void Pesquisar(final String pesquisa) {
        quotesdb =   FirebaseDatabase.getInstance().getReference().child(path).orderByChild("quote")
                .startAt(pesquisa)
                .endAt(pesquisa + "\uf8ff");
        quotesdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                quotesearch.clear();
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
                        if (q.getFont() != null){
                            quotes.setFont(q.getFont());
                        }else{
                            quotes.setFont(null);
                        }


                        if (q.getTextcolor() == 0|| q.getBackgroundcolor() == 0){
                            quotes.setTextcolor(Color.BLACK);
                            quotes.setBackgroundcolor(Color.WHITE);
                        }else{
                            quotes.setTextcolor(q.getTextcolor());
                            quotes.setBackgroundcolor(q.getBackgroundcolor());}
                        quotesearch.add(quotes);
                        if (q.getUserID().equals(user.getUid())){
                            quotes.setUsername(user.getDisplayName());
                            quotes.setUserphoto(String.valueOf(user.getPhotoUrl()));
                        }

                        System.out.println("Quotes search " + quotesearch.size());
                        if (quotesearch.size() == 0){
                            Pesquisarauthor(pesquisa);
                        }
                    }
                }

                searchresult.setVisibility(View.VISIBLE);
                GridLayoutManager llm = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
                searchresult.setHasFixedSize(true);
                System.out.println(quotesearch.size());
                RecyclerAdapter myadapter = new RecyclerAdapter( getContext(), quotesearch,  getActivity());
                myadapter.notifyDataSetChanged();
                searchresult.setAdapter(myadapter);
                searchresult.setLayoutManager(llm);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Erro " + databaseError.getMessage());
            }
        });

    }
    private void Pesquisarauthor(String pesquisa) {
        quotesdb =   FirebaseDatabase.getInstance().getReference().child(path).orderByChild("author")
                .startAt(pesquisa)
                .endAt(pesquisa + "\uf8ff");
        quotesdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                quotesearch.clear();
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
                        if (q.getFont() != null){
                            quotes.setFont(q.getFont());
                        }else{
                            quotes.setFont(null);
                        }


                        if (q.getTextcolor() == 0|| q.getBackgroundcolor() == 0){
                            quotes.setTextcolor(Color.BLACK);
                            quotes.setBackgroundcolor(Color.WHITE);
                        }else{
                            quotes.setTextcolor(q.getTextcolor());
                            quotes.setBackgroundcolor(q.getBackgroundcolor());}
                        quotesearch.add(quotes);
                        if (q.getUserID().equals(user.getUid())){
                            quotes.setUsername(user.getDisplayName());
                            quotes.setUserphoto(String.valueOf(user.getPhotoUrl()));
                        }

                        System.out.println("Quotes search " + quotesearch.size());

                    }
                }

                searchresult.setVisibility(View.VISIBLE);
                GridLayoutManager llm = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
                searchresult.setHasFixedSize(true);
                System.out.println(quotesearch.size());
                RecyclerAdapter myadapter = new RecyclerAdapter( getContext(), quotesearch,  getActivity());
                myadapter.notifyDataSetChanged();
                searchresult.setAdapter(myadapter);
                searchresult.setLayoutManager(llm);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Erro " + databaseError.getMessage());
            }
        });

    }


}
