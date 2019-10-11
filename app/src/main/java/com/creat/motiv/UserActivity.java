package com.creat.motiv;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.creat.motiv.Beans.User;
import com.creat.motiv.Database.QuotesDB;
import com.creat.motiv.Database.UserDB;
import com.creat.motiv.Utils.Alert;
import com.github.mmin18.widget.RealtimeBlurView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserActivity extends AppCompatActivity {

    private RecyclerView myquotesrecycler;
    private AppBarLayout appbarlayout;
    private CollapsingToolbarLayout collapsetoolbar;
    private Toolbar toolbar;
    private CircleImageView profilepic;
    private TextView username;
    private TextView posts;
    private Button edit;
    private String uid,name,upic;
    private Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        uid = getIntent().getStringExtra("uid");
        name = getIntent().getStringExtra("uname");
        upic = getIntent().getStringExtra("upic");
        initView();

    }

    private void initView() {
        myquotesrecycler = findViewById(R.id.myquotesrecycler);
        appbarlayout = findViewById(R.id.appbarlayout);
        collapsetoolbar = findViewById(R.id.collapsetoolbar);
        toolbar = findViewById(R.id.toolbar);
        profilepic = findViewById(R.id.profilepic);
        username = findViewById(R.id.username);
        posts = findViewById(R.id.posts);
        edit = findViewById(R.id.edit);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });

        Carregar(myquotesrecycler);
    }

    private void Carregar(RecyclerView recyclerView) {
        User u = new User();
        u.setName(name);
        u.setPicurl(upic);
        u.setUid(uid);
        UserDB userDB = new UserDB(this);
        userDB.LoadUser(profilepic,username,u);

        QuotesDB quotesDB = new QuotesDB(this);
        if (uid != null || u.getUid() != null) {
            quotesDB.CarregarUserQuotes(recyclerView, posts, uid);
        } else {
            Alert a = new Alert(this);
            a.Message(a.erroricon, "Erro ao recuperar informações de usuário");
        }
    }
}
