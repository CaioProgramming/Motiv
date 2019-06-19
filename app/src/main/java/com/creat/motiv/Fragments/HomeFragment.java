package com.creat.motiv.Fragments;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.creat.motiv.Adapters.RecyclerAdapter;
import com.creat.motiv.Beans.Quotes;
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
    private SearchView search;
    private RadioGroup searchoptions;
    private ProgressBar loading;
    private RadioButton author;
    private RadioButton love;
    private RadioButton citation;
    private TextView toolbarTitle;
    private RadioButton motivation;
    private RadioButton music;
    private RadioButton userfilter;
    private RadioGroup categories;
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
        search = v.findViewById(R.id.search);
        searchoptions = v.findViewById(R.id.searchoptions);
        composesrecycler = v.findViewById(R.id.composesrecycler);

        tutorial();


        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);


        toolbar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                View view = toolbar.getChildAt(0);
                if (view != null) {
                    TextView title = (TextView) view;
                    if (getContext() == null) {
                        return;
                    }
                    Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Cabin-Regular.ttf");
                    title.setTypeface(tf);

                }
            }
        });
        toolbar.setTitle("");
        search.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbarTitle.setVisibility(View.GONE);
                searchoptions.setVisibility(View.VISIBLE);


            }
        });


        search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchoptions.setVisibility(View.GONE);
                toolbarTitle.setVisibility(View.VISIBLE);

                return false;
            }
        });

        search.setOnQueryTextListener(this);


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

        author.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arg = "author";
                search.setQueryHint("Pesquisar author...");
            }
        });

        userfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arg = "username";
                search.setQueryHint("Pesquisar usuário...");
            }
        });


        love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Categories("Amor");
                search.setQueryHint("Pesquisar posts de amor");
            }
        });


        motivation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Categories("Motivação");
                search.setQueryHint("Pesquisar posts motivacionais");
            }
        });


        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Categories("Musica");
                search.setQueryHint("Pesquisar posts de músicas");
            }
        });

        citation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Categories("Citação");
                search.setQueryHint("Pesquisar posts de citações");

            }
        });
        toolbar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                View view = toolbar.getChildAt(0);
                if (view != null) {
                    TextView title = (TextView) view;
                    if (getActivity() == null) {
                        return;
                    }
                    Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Cabin-Regular.ttf");
                    title.setTypeface(tf);

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


    private void Carregar() {
        if (getActivity() == null || getContext() == null) {
            return;
        }


        quotesdb = FirebaseDatabase.getInstance().getReference();

        quotesdb.keepSynced(false);
        quotesdb = FirebaseDatabase.getInstance().getReference().child(path);
        if (this.isAdded()) {
            quotesdb.addListenerForSingleValueEvent(new ValueEventListener() {

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
        toolbarTitle = v.findViewById(R.id.toolbar_title);
        motivation = v.findViewById(R.id.motivation);
        music = v.findViewById(R.id.music);
        love = v.findViewById(R.id.love);
        author = v.findViewById(R.id.author);
        citation = v.findViewById(R.id.citation);
        userfilter = v.findViewById(R.id.userfilter);
    }
}
