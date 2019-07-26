package com.creat.motiv.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.creat.motiv.Beans.Quotes;
import com.creat.motiv.Database.QuotesDB;
import com.creat.motiv.MainActivity;
import com.creat.motiv.R;
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
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.creat.motiv.Database.QuotesDB.path;
import static com.creat.motiv.Database.QuotesDB.searcharg;

public class SettingsFragment extends Fragment {



    private Toolbar toolbar;
    FirebaseUser user;
    private Button changename;
    private LinearLayout editname;
    private EditText username;
    private Button save;
    private CircleImageView userpic;
    private Button deleteaccount;
    private Button deleteposts;
    private ImageButton backpass;
    private Button exit;
    private ImageButton backname;
    public SettingsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.settings, container, false);
        user = FirebaseAuth.getInstance().getCurrentUser();

        initView(v);

        user = FirebaseAuth.getInstance().getCurrentUser();
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.home = true;
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.fab_slide_in_from_right, R.anim.fade_out)
                        .replace(R.id.frame, new HomeFragment())
                        .commit();


            }
        });

        return v;
    }

    private void initView(View v) {
        toolbar = v.findViewById(R.id.toolbar);
        userpic = v.findViewById(R.id.userpic);
        changename = v.findViewById(R.id.changename);
        editname = v.findViewById(R.id.editname);
        username = v.findViewById(R.id.username);
        save = v.findViewById(R.id.save);
        deleteposts = v.findViewById(R.id.deleteposts);
        deleteaccount = v.findViewById(R.id.deleteaccount);
        backname = v.findViewById(R.id.backname);
        exit = v.findViewById(R.id.exit);
        if (user.getPhotoUrl() != null) {
            Glide.with(this).load(user.getPhotoUrl()).into(userpic);
        } else {
            userpic.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        username.setHint(user.getDisplayName());
        changename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changename.setVisibility(View.GONE);
                editname.setVisibility(View.VISIBLE);
            }
        });


        backname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changename.setVisibility(View.VISIBLE);
                editname.setVisibility(View.GONE);
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().equals("")) {
                    username.setError("Um nome vazio? Você tá brincando comigo. só pode.");
                } else {
                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(username.getText().toString()).build();
                    user.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            QuotesDB db = new QuotesDB();
                            final ArrayList<Quotes> myquotes = new ArrayList<>();

                            Query quotesdb = FirebaseDatabase.getInstance().getReference().child(path).orderByChild("userID")
                                    .startAt(user.getUid())
                                    .endAt(user.getUid() + searcharg);
                            quotesdb.addListenerForSingleValueEvent(new ValueEventListener() {

                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                                        Quotes quotes = new Quotes();
                                        Quotes q = d.getValue(Quotes.class);
                                        if (q != null) {
                                            quotes.setId(d.getKey());
                                            myquotes.add(quotes);
                                            System.out.println("Quotes " + myquotes.size());
                                        }
                                    }


                                }


                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            for (Quotes quotes : myquotes) {
                                db.AlterarNome(getActivity(), quotes.getId());
                            }


                        }


                    });
                }
            }
        });


    }
}
