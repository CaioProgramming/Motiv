package com.creat.motiv.Fragments;


import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.creat.motiv.Adapters.RecyclerAdapter;
import com.creat.motiv.Beans.Quotes;
import com.creat.motiv.MainActivity;
import com.creat.motiv.R;
import com.creat.motiv.Utils.Info;
import com.creat.motiv.Utils.Pref;
import com.creat.motiv.Utils.Tools;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.creat.motiv.Database.QuotesDB.path;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements SearchView.OnQueryTextListener {


    private RecyclerView composesrecycler;
    String arg = "quote";
    ArrayList<Quotes> quotesArrayList;
    private Toolbar toolbar;
    FirebaseUser user;
    Boolean novo;
    Query quotesdb;
    private ProgressBar loading;
    private CoordinatorLayout home;
    private AppBarLayout appbarlayout;
    private CollapsingToolbarLayout collapsetoolbar;
    private AdView adView;

    public HomeFragment() {
        // Required empty public constructor
    }

    Pref preferences;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        user = FirebaseAuth.getInstance().getCurrentUser();
        preferences = new Pref(Objects.requireNonNull(getContext()));
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        initView(v);
        setHasOptionsMenu(true);
        v.findViewById(R.id.home);
        v.findViewById(R.id.appbarlayout);

        this.loading = v.findViewById(R.id.loading);

        AdView adView = v.findViewById(R.id.adView);

        MobileAds.initialize(getContext(),
                "ca-app-pub-4979584089010597/9177000416");

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


        novo = Objects.requireNonNull(Objects.requireNonNull(getActivity()).getIntent().getExtras()).getBoolean("novo");

        toolbar = v.findViewById(R.id.toolbar);

        composesrecycler = v.findViewById(R.id.composesrecycler);

        tutorial();


        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);


        collapsetoolbar.setTitle(getActivity().getResources().getString(R.string.app_name));
        collapsetoolbar.setExpandedTitleTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/Nunito-Regular.ttf"));
        collapsetoolbar.setCollapsedTitleTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/Nunito-Regular.ttf"));
        CircleImageView profilepic = v.findViewById(R.id.profilepic);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Glide.with(this).load(user.getPhotoUrl()).into(profilepic);
        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.home = false;
                ProfileFragment nextFrag = new ProfileFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame, nextFrag, "profilefragment")
                        .addToBackStack(null)
                        .commit();
            }
        });
        return v;


    }


    private void Pesquisar(final String pesquisa) {
        if (getContext() == null || getActivity() == null) {
            return;
        }
        quotesdb = FirebaseDatabase.getInstance().getReference().child(path).orderByChild(arg)
                .startAt(pesquisa)
                .endAt(pesquisa + "\uf8ff");

        quotesdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (quotesArrayList != null) {
                    quotesArrayList.clear();
                } else {
                    quotesArrayList = new ArrayList<>();
                }
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
                        if (quotesArrayList.size() == 0) {
                            Pesquisarauthor(pesquisa);
                        }
                    }
                }

                composesrecycler.setVisibility(View.VISIBLE);
                GridLayoutManager llm = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
                composesrecycler.setHasFixedSize(true);
                System.out.println(quotesArrayList.size());
                RecyclerAdapter myadapter = new RecyclerAdapter(getContext(), quotesArrayList, getActivity(), composesrecycler);
                myadapter.notifyDataSetChanged();
                composesrecycler.setAdapter(myadapter);
                if (arg.equals("")) {
                    FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
                    layoutManager.setFlexDirection(FlexDirection.COLUMN);
                    layoutManager.setJustifyContent(JustifyContent.CENTER);
                    composesrecycler.setLayoutManager(layoutManager);
                } else {
                    composesrecycler.setLayoutManager(llm);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Erro " + databaseError.getMessage());
            }
        });


    }

    private void Pesquisarauthor(String pesquisa) {
        if (getActivity() == null || getContext() == null) {
            return;
        }
        quotesdb = FirebaseDatabase.getInstance().getReference().child(path).orderByChild("author")
                .startAt(pesquisa)
                .endAt(pesquisa + "\uf8ff");
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
                GridLayoutManager llm = new GridLayoutManager(getActivity(), Tools.spancount, GridLayoutManager.VERTICAL, false);
                composesrecycler.setHasFixedSize(true);
                System.out.println(quotesArrayList.size());
                RecyclerAdapter myadapter = new RecyclerAdapter(getContext(), quotesArrayList, getActivity(), composesrecycler);
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


    @Override
    public void onResume() {
        Carregar();
        show();


        appbarlayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsetoolbar.setTitle("Motiv");
                    collapsetoolbar.setCollapsedTitleTextColor(ColorStateList.valueOf(Color.WHITE));
                    isShow = true;
                } else if (isShow) {
                    collapsetoolbar.setTitle(" ");//careful there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });
        super.onResume();
    }


    private void tutorial() {

        if (novo) {
            Pref preferences = new Pref(Objects.requireNonNull(getContext()));
            if (!preferences.hometutorialstate()) {
                preferences.setHomeTutorial(true);
                Info.tutorial(getString(R.string.home_intro), Objects.requireNonNull(getActivity()));
            }


        }
        novo = false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Objects.requireNonNull(getActivity()).getMenuInflater().inflate(R.menu.mainmenu, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.navigation_about) {
            MainActivity.home = false;
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_top, R.anim.fab_slide_out_to_left)
                    .replace(R.id.frame, new AboutFragment())
                    .commit();
        } else {
            MainActivity.home = false;
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.fui_slide_in_right, R.anim.fab_slide_out_to_left)
                    .replace(R.id.frame, new SearchFragment())
                    .commit();
        }
        return super.onOptionsItemSelected(item);
    }

    private void Carregar() {
        if (getActivity() == null || getContext() == null) {
            return;
        }


        quotesdb = FirebaseDatabase.getInstance().getReference();

        quotesdb.keepSynced(false);
        quotesdb = FirebaseDatabase.getInstance().getReference().child(path);
        if (this.isAdded()) {
            quotesdb.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    quotesArrayList = new ArrayList<>();
                    quotesArrayList.clear();
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
                    GridLayoutManager llm = new GridLayoutManager(getActivity(), Tools.spancount, GridLayoutManager.VERTICAL, false);
                    composesrecycler.setHasFixedSize(true);
                    System.out.println(quotesArrayList.size());
                    if (getContext() == null) {
                        return;
                    }
                    RecyclerAdapter myadapter = new RecyclerAdapter(getContext(), quotesArrayList, getActivity(), composesrecycler);
                    composesrecycler.setAdapter(myadapter);
                    composesrecycler.setLayoutManager(llm);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    System.out.println(databaseError.getMessage());
                }
            });
        }


    }

    private void show() {

        CountDownTimer timer = new CountDownTimer(1500, 100) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                if (getContext() == null) {
                    return;
                }
                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
                final Animation in = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);

                loading.startAnimation(animation);

                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        loading.setVisibility(View.GONE);
                        if (composesrecycler.getVisibility() != View.VISIBLE) {
                            composesrecycler.setVisibility(View.VISIBLE);
                            composesrecycler.startAnimation(in);
                        }


                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });


            }
        };
        timer.start();
    }


    private void Categories(String categorie) {
        if (getContext() == null || getActivity() == null) {
            return;
        }
        quotesdb = FirebaseDatabase.getInstance().getReference().child(path).orderByChild("categoria")
                .startAt(categorie)
                .endAt(categorie + "\uf8ff");
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
                GridLayoutManager llm = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
                composesrecycler.setHasFixedSize(true);
                System.out.println(quotesArrayList.size());
                RecyclerAdapter myadapter = new RecyclerAdapter(getContext(), quotesArrayList, getActivity(), composesrecycler);
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


    private void initView(View v) {

        home = v.findViewById(R.id.home);
        appbarlayout = v.findViewById(R.id.appbarlayout);
        collapsetoolbar = v.findViewById(R.id.collapsetoolbar);
        adView = v.findViewById(R.id.adView);
    }
}