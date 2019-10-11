package com.creat.motiv.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.creat.motiv.Database.QuotesDB;
import com.creat.motiv.R;
import com.creat.motiv.Utils.Alert;
import com.creat.motiv.Utils.NewQuotepopup;
import com.creat.motiv.Utils.Pref;
import com.github.mmin18.widget.RealtimeBlurView;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements SearchView.OnQueryTextListener {

    private RecyclerView composesrecycler;
    private Boolean novo;
    private SwipeRefreshLayout refreshLayout;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_home, container, false);
        v.findViewById(R.id.home);
        v.findViewById(R.id.appbarlayout);
        refreshLayout = v.findViewById(R.id.refresh);
        SearchView search = v.findViewById(R.id.search);
        search.setOnQueryTextListener(this);
        search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Carregar();
                return false;
            }
        });
        final RealtimeBlurView blur = Objects.requireNonNull(getActivity()).findViewById(R.id.rootblur);
        TextView addquote = v.findViewById(R.id.addquote);
        addquote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewQuotepopup newQuotepopup = new NewQuotepopup(getActivity(),blur);
                newQuotepopup.showup();
            }
        });

        novo = Objects.requireNonNull(Objects.requireNonNull(getActivity()).getIntent().getExtras()).getBoolean("novo");


        composesrecycler = v.findViewById(R.id.composesrecycler);

        tutorial();


        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (refreshLayout.isRefreshing()) {
                    Carregar();
                }
            }
        });


        return v;


    }








    @Override
    public void onResume() {

        Carregar();
        show();


        super.onResume();
    }


    private void tutorial() {

        if (novo) {
            Pref preferences = new Pref(Objects.requireNonNull(getContext()));
            if (!preferences.hometutorialstate()) {
                preferences.setHomeTutorial(true);
                Alert alert = new Alert(getActivity());
                alert.Message(getActivity().getDrawable(R.drawable.ic_drawkit_notebook_man_monochrome), getString(R.string.home_intro));

            }


        }
        novo = false;

    }





    private void Carregar() {
        QuotesDB quotesDB = new QuotesDB(getActivity());
        quotesDB.Carregar(composesrecycler, refreshLayout);



    }

    private void show() {
        Alert a = new Alert(Objects.requireNonNull(getActivity()));
        a.loading();

    }



    @Override
    public boolean onQueryTextSubmit(String query) {
        if (!query.isEmpty()) {
            QuotesDB quotesDB = new QuotesDB(getActivity());
            quotesDB.Pesquisar(query, composesrecycler);
            return true;
        } else {
            Carregar();
            return false;
        }
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (!newText.isEmpty()) {
            QuotesDB quotesDB = new QuotesDB(getActivity());
            quotesDB.Pesquisar(newText, composesrecycler);
            return true;
        } else {
            Carregar();
            return false;
        }
    }


}