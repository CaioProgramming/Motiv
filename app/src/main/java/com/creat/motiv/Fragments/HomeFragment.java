package com.creat.motiv.Fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.creat.motiv.Adapters.RecyclerAdapter;
import com.creat.motiv.Beans.Quotes;
import com.creat.motiv.MainActivity;
import com.creat.motiv.R;
import com.creat.motiv.Utils.Info;
import com.creat.motiv.Utils.NewQuotepopup;
import com.creat.motiv.Utils.Pref;
import com.creat.motiv.Utils.Tools;
import com.github.mmin18.widget.RealtimeBlurView;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
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

    SearchView search;
    private RecyclerView composesrecycler;
    String arg = "quote";
    ArrayList<Quotes> quotesArrayList;
    FirebaseUser user;
    Boolean novo;
    Query quotesdb;

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
        v.findViewById(R.id.home);
        v.findViewById(R.id.appbarlayout);
        this.search = v.findViewById(R.id.search);
        search.setOnQueryTextListener(this);
        search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Carregar();
                return false;
            }
        });
        final RealtimeBlurView blur = getActivity().findViewById(R.id.rootblur);
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

        /*profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.home = false;
                ProfileFragment nextFrag = new ProfileFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame, nextFrag, "profilefragment")
                        .addToBackStack(null)
                        .commit();
            }
        });*/
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
                RecyclerAdapter myadapter = new RecyclerAdapter(getContext(), quotesArrayList, getActivity());
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
                RecyclerAdapter myadapter = new RecyclerAdapter(getContext(), quotesArrayList, getActivity());
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
            /*getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_top, R.anim.fab_slide_out_to_left)
                    .replace(R.id.frame, new AboutFragment())
                    .commit();*/
        } else {
            MainActivity.home = false;
            /*getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.fui_slide_in_right, R.anim.fab_slide_out_to_left)
                    .replace(R.id.frame, new SearchFragment())
                    .commit();*/
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
                    RecyclerAdapter myadapter = new RecyclerAdapter(getContext(), quotesArrayList, getActivity());
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


                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
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
                RecyclerAdapter myadapter = new RecyclerAdapter(getContext(), quotesArrayList, getActivity());
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


}