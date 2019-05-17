package com.creat.motiv.Fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import java.util.Objects;

import static com.creat.motiv.Database.QuotesDB.path;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener {


    Pref preferences;
    FirebaseUser user;
    private ArrayList<Quotes> quotesearch;
    private Query quotesdb;
    private android.widget.LinearLayout empty;
    private android.support.v7.widget.RecyclerView searchresult;
    private android.support.v7.widget.SearchView search;
    private TabLayout searchoptions;
    private Toolbar toolbar;
    private android.widget.ImageView icon;


    public SearchFragment() {
        // Required empty public constructor
    }
    String arg = "quote";




    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        quotesearch = new ArrayList<>();
        quotesdb = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
         View view = inflater.inflate(R.layout.fragment_search, container, false);
         this.toolbar = view.findViewById(R.id.toolbar);
        this.searchoptions = view.findViewById(R.id.searchoptions);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        this.search = view.findViewById(R.id.search);
        this.empty = view.findViewById(R.id.empty);
        this.searchresult = view.findViewById(R.id.searchresult);
        TabLayout searchoptions = view.findViewById(R.id.searchoptions);

        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        preferences = new Pref(getContext());
        quotesdb.keepSynced(false);


        searchoptions.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        arg = "author";
                        search.setQueryHint("Pesquisar author...");
                        //Pesquisar(search.getText().toString());
                        break;
                    case 1:
                        arg = "username";
                        search.setQueryHint("Pesquisar posts de usuário...");
                        //Pesquisar(search.getText().toString());
                        break;
                    case 2:
                        Categories("Amor");
                        break;
                    case 3:
                        Categories("Citação");
                        break;
                    case 4:
                        Categories("Musica");
                        break;
                    case 5:
                        Categories("Motivação");
                        break;
                    default:
                        arg = "author";
                        search.setQueryHint("Pesquisar author...");
                        break;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Pesquisar("");
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



        Categories("Amor");

        search.setOnQueryTextListener(this);
        return view;
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
                empty.setVisibility(View.GONE);
                if (quotesearch.size() == 0) {
                    empty.setVisibility(View.VISIBLE);
                }
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
                empty.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Erro " + databaseError.getMessage());
            }
        });
        if (quotesearch.size() == 0) {
            empty.setVisibility(View.VISIBLE);
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
                empty.setVisibility(View.GONE);
                if (quotesearch.size() == 0) {
                    empty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Erro " + databaseError.getMessage());
            }
        });

    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        if (!query.isEmpty()) {
            Pesquisar(query);
            return true;
        } else {
            Pesquisar("");
            return false;
        }
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (!newText.isEmpty()) {
            Pesquisar(newText);
            return true;
        } else {
            Pesquisar("");
            return false;
        }
    }
}
