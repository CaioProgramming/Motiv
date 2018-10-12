package com.creat.motiv.Fragments;


import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.asksira.bsimagepicker.BSImagePicker;
import com.asksira.bsimagepicker.Utils;
import com.bumptech.glide.Glide;
import com.creat.motiv.Adapters.RecyclerAdapter;
import com.creat.motiv.Beans.Quotes;
import com.creat.motiv.Database.QuotesDB;
import com.creat.motiv.R;
import com.github.mmin18.widget.RealtimeBlurView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import de.mateware.snacky.Snacky;

import static com.creat.motiv.Database.QuotesDB.path;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements BSImagePicker.OnSingleImageSelectedListener{


    private android.widget.ImageView profilepic;
    private android.widget.TextView username;
    private android.widget.TextView postnumber;
    private android.support.v7.widget.RecyclerView myquotesrecycler;
    private RealtimeBlurView blur;
    private android.support.v4.widget.SwipeRefreshLayout swiperefresh;
    ArrayList<Quotes> myquotes;
    private Query quotesdb;
    FirebaseUser user;
    QuotesDB quotesDB;
    private android.support.v7.widget.Toolbar toolbar;
    private CollapsingToolbarLayout collapsetoolbar;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         blur = Objects.requireNonNull(getActivity()).findViewById(R.id.rootblur);
        user = FirebaseAuth.getInstance().getCurrentUser();
        quotesdb = FirebaseDatabase.getInstance().getReference();


        View view = inflater.inflate(R.layout.fragment_profile, container, false);
         collapsetoolbar = (CollapsingToolbarLayout) view.findViewById(R.id.collapsetoolbar);
         username = (TextView) view.findViewById(R.id.username);
         myquotesrecycler = view.findViewById(R.id.myquotes);
        this.toolbar = view.findViewById(R.id.toolbar);







        Carregar();

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setTitle(user.getDisplayName());
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null){
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        }

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)view.findViewById(R.id.collapsetoolbar);
        collapsingToolbarLayout.setTitle(user.getDisplayName());
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);


        // Set collapsing tool bar image.
        ImageView collapsingToolbarImageView = view.findViewById(R.id.profilepic);
        collapsingToolbarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        Glide.with(this).load(user.getPhotoUrl()).into(collapsingToolbarImageView);
        username.setText(user.getDisplayName());
        return view;

    }

    private void Picalert() {
        BSImagePicker singleSelectionPicker = new BSImagePicker.Builder("com.myself.fileprovider")
                //Default: Integer.MAX_VALUE. Don't worry about performance :)
                .hideGalleryTile()
                .setSpanCount(4) //Default: 3. This is the number of columns
                .setGridSpacing(Utils.dp2px(2)) //Default: 2dp. Remember to pass in a value in pixel.
                .setPeekHeight(Utils.dp2px(360))//Default: 360dp. This is the initial height of the dialog.
                .setOverSelectTextColor(R.color.black)
                .setMultiSelectDoneTextColor(R.color.black)
                .build();

        singleSelectionPicker.show(getActivity().getSupportFragmentManager(), "picker");
    }




    private void Carregar() {
        myquotes = new ArrayList<>();
        myquotes.clear();


        quotesdb.keepSynced(true);


        quotesdb = FirebaseDatabase.getInstance().getReference().child(path).orderByChild("data");
        quotesdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Quotes quotes = new Quotes();
                    i++;
                    Quotes q = d.getValue(Quotes.class);
                    if (q != null) {
                        quotes.setId(d.getKey());
                        quotes.setAuthor(q.getAuthor());
                        quotes.setQuote(q.getQuote());
                        quotes.setUserID(q.getUserID());
                        quotes.setCategoria(q.getCategoria());
                        quotes.setData(q.getData());
                        quotes.setLikes(q.getLikes());
                        if (q.getTextcolor() == 0|| q.getBackgroundcolor() == 0){
                            quotes.setTextcolor(Color.BLACK);
                            quotes.setBackgroundcolor(Color.WHITE);
                        }else{
                            quotes.setTextcolor(q.getTextcolor());
                            quotes.setBackgroundcolor(q.getBackgroundcolor());}
                            if (q.getUserID().equals(user.getUid()))
                        myquotes.add(quotes);
                        System.out.println(i);

                        System.out.println("Quotes " + myquotes.size());

                    }
                }

                Collections.reverse(myquotes);
                myquotesrecycler.setVisibility(View.VISIBLE);
                GridLayoutManager llm = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
                myquotesrecycler.setHasFixedSize(true);
                System.out.println(myquotes.size());
                final Animation myanim2 = AnimationUtils.loadAnimation(getContext(), R.anim.transition);
                RecyclerAdapter myadapter = new RecyclerAdapter(quotesDB,getContext(), myquotes, blur);
                myquotesrecycler.setAdapter(myadapter);
                myquotesrecycler.setLayoutManager(llm);
                myquotesrecycler.startAnimation(myanim2);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }


    @Override
    public void onSingleImageSelected(Uri uri) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setPhotoUri(uri).build();
        if (user != null) {
            user.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Snacky.builder().setActivity(Objects.requireNonNull(getActivity())).success().setText("Foto de perfil alterada").show();
                        //CircleImageView profilepic = getActivity().findViewById(R.id.profile_pic);
                        //Glide.with(getActivity()).load(user.getPhotoUrl()).into(profilepic);
                    } else {
                        Snacky.builder().setActivity(Objects.requireNonNull(getActivity())).success().setText("Erro " + task.getException()).show();
                    }


                }
            });
        }

    }
}
