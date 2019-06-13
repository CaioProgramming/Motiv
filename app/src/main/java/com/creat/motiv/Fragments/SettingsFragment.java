package com.creat.motiv.Fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.creat.motiv.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsFragment extends Fragment {


    FirebaseUser user;
    private Toolbar toolbar;
    private TextView toolbarTitle;
    private CircleImageView userpic;
    private Button changename;
    private LinearLayout editname;
    private EditText username;
    private Button save;
    private Button changepass;
    private TextInputLayout etPasswordLayout;
    private TextInputEditText etPassword;
    private Button savepass;
    private Button deleteposts;
    private Button deleteaccount;
    private LinearLayout passwordLayout;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.settings, container, false);

        initView(v);

        user = FirebaseAuth.getInstance().getCurrentUser();
        Glide.with(getActivity()).load(user.getPhotoUrl()).into(userpic);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomNavigationView navigationView = getActivity().findViewById(R.id.navigation);
                navigationView.setSelectedItemId(R.id.navigation_home);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.fab_slide_in_from_right, R.anim.fade_out)
                        .replace(R.id.frame, new HomeFragment())
                        .commit();


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
        toolbar.setTitle("");
        return v;
    }

    private void initView(View v) {
        toolbar = v.findViewById(R.id.toolbar);
        toolbarTitle = v.findViewById(R.id.toolbar_title);
        userpic = v.findViewById(R.id.userpic);
        changename = v.findViewById(R.id.changename);
        editname = v.findViewById(R.id.editname);
        username = v.findViewById(R.id.username);
        save = v.findViewById(R.id.save);
        changepass = v.findViewById(R.id.changepass);
        etPasswordLayout = v.findViewById(R.id.etPasswordLayout);
        etPassword = v.findViewById(R.id.etPassword);
        savepass = v.findViewById(R.id.savepass);
        deleteposts = v.findViewById(R.id.deleteposts);
        deleteaccount = v.findViewById(R.id.deleteaccount);
        passwordLayout = v.findViewById(R.id.password_layout);
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

        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changepass.setVisibility(View.GONE);
                passwordLayout.setVisibility(View.VISIBLE);
            }
        });


    }
}
