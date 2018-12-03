package com.creat.motiv.Fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
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

import com.creat.motiv.Adapters.RecyclerAdapter;
import com.creat.motiv.Beans.Quotes;
import com.creat.motiv.R;
import com.creat.motiv.Utils.Pref;
import com.creat.motiv.Utils.Tools;
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
    private android.widget.ImageButton close;
    private android.support.v7.widget.Toolbar toolbar;
    private android.support.design.widget.TabLayout searchoptions;
    private android.support.design.widget.CollapsingToolbarLayout collapsetoolbar;
    private android.support.design.widget.AppBarLayout appbarlayout;


    public SearchFragment() {
        // Required empty public constructor
    }
    String arg = "quote";
    private int[] tabIcons = {
            R.drawable.ic_favorite_white_24dp,
            R.drawable.ic_turned_in_black_24dp,
            R.drawable.ic_music_note_black_24dp,
            R.drawable.ic_filter_vintage_black_24dp

    };

    private int[] tabIconsfocus = {
            R.drawable.ic_author_sign,
            R.drawable.ic_person_white_24dp,

    };



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        quotesearch = new ArrayList<>();
        quotesdb = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        preferences = new Pref(getContext());
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        this.searchoptions = view.findViewById(R.id.searchoptions);
        this.toolbar = view.findViewById(R.id.toolbar);
        this.close = view.findViewById(R.id.close);
        searchresult = view.findViewById(R.id.searchresult);
        search = view.findViewById(R.id.search);
        ((AppCompatActivity)getActivity()).setSupportActionBar(searchtool);
        preferences = new Pref(getContext());
        quotesdb.keepSynced(true);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search.getText().clear();
                quotesearch.clear();
                search.setHint("Pesquisar frases...");
                setupicons();


             }
        });








        search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                   setupiconsfocus();
                   searchoptions.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                       @Override
                       public void onTabSelected(TabLayout.Tab tab) {
                           switch (searchoptions.getSelectedTabPosition()){
                               case 0:
                                   arg = "autor";
                                   search.setHint("Pesquisar autor...");
                                   break;

                               case 1:
                                   arg = "username";
                                   search.setHint("Pesquisar posts de usuário...");
                                   break;
                           }
                       }

                       @Override
                       public void onTabUnselected(TabLayout.Tab tab) {

                       }

                       @Override
                       public void onTabReselected(TabLayout.Tab tab) {

                       }
                   });

                }
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Animation in = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_top);

                if (!editable.toString().isEmpty()){
                    searchresult.setVisibility(View.VISIBLE);
                    searchresult.startAnimation(in);
                    Pesquisar(editable.toString());
                }else{
                    Pesquisar("");
                }
            }
        });
        setupicons();
        if (preferences.nightmodestate()) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        return view;
    }


    private void setupicons(){
        searchoptions.removeAllTabs();
        for (int i =0;i< tabIcons.length;i++){
            searchoptions.addTab(searchoptions.newTab().setIcon(tabIcons[i]));
        }
        searchoptions.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (searchoptions.getSelectedTabPosition()){
                    case 0:
                        Categories("Amor");
                        break;
                    case 1:
                        Categories("Citação");
                        break;
                    case 2:
                        Categories("Musica");
                        break;
                    case 3:
                        Categories("Motivação");
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
    private void setupiconsfocus(){
        searchoptions.removeAllTabs();
        for (int i =0;i< tabIconsfocus.length;i++){
            searchoptions.addTab(searchoptions.newTab().setIcon(tabIconsfocus[i]));
        }
    }

    private void Categories(String categorie) {
        if (getContext() == null || getActivity() == null){
            return;
        }
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
                searchresult.setVisibility(View.VISIBLE);
                GridLayoutManager llm = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
                searchresult.setHasFixedSize(true);
                System.out.println(quotesearch.size());
                RecyclerAdapter myadapter = new RecyclerAdapter(getContext(), quotesearch, getActivity(), searchresult);
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

    private void Pesquisar(final String pesquisa) {
        if (getContext() == null || getActivity() == null){
            return;
        }
        quotesdb =   FirebaseDatabase.getInstance().getReference().child(path).orderByChild(arg)
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
                RecyclerAdapter myadapter = new RecyclerAdapter(getContext(), quotesearch, getActivity(), searchresult);
                myadapter.notifyDataSetChanged();
                searchresult.setAdapter(myadapter);
                searchresult.setLayoutManager(llm);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Erro " + databaseError.getMessage());
            }
        });
        if (quotesearch.size() == 0) {
            Pesquisarauthor(pesquisa);
        }

    }

    private void Pesquisarauthor(String pesquisa) {
        if (getActivity() == null || getContext() == null) {
            return;
        }
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
                GridLayoutManager llm = new GridLayoutManager(getActivity(), Tools.spancount, GridLayoutManager.VERTICAL, false);
                searchresult.setHasFixedSize(true);
                System.out.println(quotesearch.size());
                RecyclerAdapter myadapter = new RecyclerAdapter(getContext(), quotesearch, getActivity(), searchresult);
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
