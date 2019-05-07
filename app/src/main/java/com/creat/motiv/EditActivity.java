package com.creat.motiv;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.creat.motiv.Adapters.RecyclerColorAdapter;
import com.creat.motiv.Adapters.RecyclerFontAdapter;
import com.creat.motiv.Beans.Quotes;
import com.creat.motiv.Database.QuotesDB;
import com.creat.motiv.Utils.Pref;
import com.creat.motiv.Utils.Tools;
import com.github.mmin18.widget.RealtimeBlurView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;

import static com.creat.motiv.Database.QuotesDB.path;

public class EditActivity extends AppCompatActivity {
    QuotesDB quotesDB;
    private android.support.v7.widget.RecyclerView colorlibrary;
    private de.hdodenhof.circleimageview.CircleImageView userpic;
    private android.widget.TextView username;
    private android.widget.EditText quote;
    private android.widget.EditText author;
    private android.widget.TextView fontid;
    private android.widget.TextView texcolorid;
    private android.widget.TextView backcolorid;
    private android.widget.LinearLayout background;
    private com.github.clans.fab.FloatingActionButton textcolorfab;
    private com.github.clans.fab.FloatingActionButton backcolorfab;
    private com.github.clans.fab.FloatingActionButton fontpickerfab;
    private com.github.clans.fab.FloatingActionButton salvar;
    private FirebaseUser user;
    private String categoria;
    private Activity activity = this;
    private Context context = this;
    private RealtimeBlurView blurView;
    Quotes quotes;
    private android.widget.TextView quoteID;
    private android.widget.TextView backcoloridid;
    private com.github.clans.fab.FloatingActionButton categoryfab;
    private android.support.design.widget.TabLayout categories;
    private LinearLayout card;
    private android.widget.RelativeLayout edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        quotes = new Quotes();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        this.edit = findViewById(R.id.edit);
        this.card = findViewById(R.id.card);
        this.categories = findViewById(R.id.categories);
        android.widget.Toolbar options;
        this.blurView = findViewById(R.id.blur);
        this.salvar = findViewById(R.id.salvar);
        this.fontpickerfab = findViewById(R.id.fontpickerfab);
        this.backcolorfab = findViewById(R.id.backcolorfab);
        this.textcolorfab = findViewById(R.id.textcolorfab);
        this.categoryfab = findViewById(R.id.categoryfab);
        this.backcolorid = findViewById(R.id.backcolorid);
        this.texcolorid = findViewById(R.id.texcolorid);
        this.fontid = findViewById(R.id.fontid);
        this.background = findViewById(R.id.background);
        this.author = findViewById(R.id.author);
        this.quote = findViewById(R.id.quote);
        this.username = findViewById(R.id.username);
        this.userpic = findViewById(R.id.userpic);
        options = findViewById(R.id.options);
        this.colorlibrary = findViewById(R.id.colorlibrary);

        try {
            colorgallery();
        } catch (ClassNotFoundException | IllegalAccessException e) {
            e.printStackTrace();
        }
        theme();
        fabclicks();
        loadquote();
        setActionBar(options);

    }

    private void loadquote() {
         Intent i = getIntent();
        quotes.setId(Objects.requireNonNull(i.getExtras()).getString("id"));
        Query quotesdb = FirebaseDatabase.getInstance().getReference().child(path).orderByChild("id").startAt(quotes.getId()).endAt(quotes.getId() + "\uf8ff");

        quotesdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Quotes q = d.getValue(Quotes.class);
                    if (q!= null){
                        quotes.setCategoria(q.getCategoria());
                        quotes.setUserphoto(q.getUserphoto());
                        quotes.setUsername(q.getUsername());
                        quotes.setUserID(q.getUserID());
                        quotes.setBackgroundcolor(q.getBackgroundcolor());
                        quotes.setAuthor(q.getAuthor());
                        quotes.setQuote(q.getQuote());
                        quotes.setData(q.getData());
                        quotes.setBold(q.isBold());
                        quotes.setItalic(q.isItalic());
                        quotes.setTextcolor(q.getTextcolor());
                        quotes.setBackgroundcolor(q.getBackgroundcolor());
                        quotes.setFont(q.getFont());
                        quotes.setReport(q.isReport());
                        //quoteID.setText(quotes.getId());
                        quote.setText(quotes.getQuote());
                        author.setText(quotes.getAuthor());
                        username.setText(quotes.getUsername());
                        background.setBackgroundColor(quotes.getBackgroundcolor());

                        quote.setTextColor(quotes.getTextcolor());
                        if (quotes.getFont() != null){
                            quote.setTypeface(Tools.fonts(context).get(quotes.getFont()));
                            author.setTypeface(Tools.fonts(context).get(quotes.getFont()));
                        }else{
                            quote.setTypeface(Typeface.DEFAULT);
                            author.setTypeface(Typeface.DEFAULT);
                        }
                        author.setTextColor(quotes.getTextcolor());
                        texcolorid.setText(String.valueOf(quotes.getTextcolor()));
                        backcolorid.setText(String.valueOf(quotes.getBackgroundcolor()));
                        fontid.setText(String.valueOf(quotes.getFont()));
                        switch (quotes.getCategoria()) {
                            case "Musica":
                                card.setBackgroundResource(R.drawable.bottom_line_music);

                                break;
                            case "Citação":
                                card.setBackgroundResource(R.drawable.bottom_line_citation);
                                break;
                            case "Amor":
                                card.setBackgroundResource(R.drawable.bottom_line_love);
                                break;
                            case "Motivação":
                                card.setBackgroundResource(R.drawable.bottom_line_motivation);
                                break;
                            case "Nenhum":
                                card.setBackgroundResource(R.drawable.bottom_line_none);

                                break;
                        }


                        Glide.with(getApplicationContext()).load(quotes.getUserphoto()).into(userpic);


                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    private void fabclicks() {
        backcolorfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackColorpicker();
            }
        });
        textcolorfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Textocolorpicker();
            }
        });
        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvar();
            }
        });
        fontpickerfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fontpicker();
            }
        });
        categoryfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                card.setBackgroundResource(R.drawable.bottom_line_none);
                categoria = "Nenhum";
                quotes.setCategoria(categoria);
            }
        });
        /*boldfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boldtext();
            }
        });
        italicfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                italic();
            }
        });*/
    }


    public void salvar() {
        Integer fonti = null;
        if (fontid.getText() != "") {
            fonti = Integer.parseInt(fontid.getText().toString());
        }
        if (author.getText().toString().equals("")) {
            quotes.setAuthor(user.getDisplayName());
        }

        quotes.setQuote(quote.getText().toString());
        quotes.setAuthor(author.getText().toString());
        quotes.setFont(fonti);
        quotes.setBackgroundcolor(Integer.parseInt(backcolorid.getText().toString()));
        quotes.setTextcolor(Integer.parseInt(texcolorid.getText().toString()));
        quotesDB = new QuotesDB(quotes, this);
        quotesDB.Editar();
        CountDownTimer timer = new CountDownTimer(2000, 100) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                Intent back = new Intent(activity,Splash.class);
                back.putExtra("novo",false);
                startActivity(back);
                finish();

            }
        }.start();


    }

    public void colorgallery() throws ClassNotFoundException, IllegalAccessException {
        ArrayList<Integer> colors = new ArrayList<>();
        Field[] fields = Class.forName(Objects.requireNonNull(getPackageName() + ".R$color")).getDeclaredFields();
        for (Field field : fields) {
            String colorName = field.getName();
            int colorId = field.getInt(null);
            int color = getResources().getColor(colorId);
            System.out.println("color " + colorName + " " + color);
            colors.add(color);

        }

        System.out.println("Load " + colors.size() + " colors");
        colorlibrary.setHasFixedSize(true);
        GridLayoutManager llm = new GridLayoutManager(this, 3, GridLayoutManager.HORIZONTAL, false);
        RecyclerColorAdapter recyclerColorAdapter = new RecyclerColorAdapter(colors, this,
                background, quote, author, this, texcolorid, backcolorid);
        colorlibrary.setAdapter(recyclerColorAdapter);
        colorlibrary.setLayoutManager(llm);
    }


    private void theme() {
        Pref preferences = new Pref(this);
        if (preferences.nightmodestate()) {
            edit.setBackgroundResource(R.color.grey_800);
            categories.setTabTextColors(getResources().getColor(R.color.lwhite), getResources().getColor(R.color.colorPrimary));
            categories.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimary));
        }
    }



    private void BackColorpicker() {
        final ColorPicker cp = new ColorPicker(this);
        cp.show();
        cp.enableAutoClose();
        cp.setCallback(new ColorPickerCallback() {
            @Override
            public void onColorChosen(int color) {
                background.setVisibility(View.INVISIBLE);
                background.setBackgroundColor(color);
                int cx = background.getRight();
                int cy = background.getTop();
                int radius = Math.max(background.getWidth(), background.getHeight());
                Animator anim = ViewAnimationUtils.createCircularReveal(background, cx, cy,
                        0, radius);
                background.setVisibility(View.VISIBLE);
                anim.start();
                backcolorid.setText(String.valueOf(color));
            }
        });
    }

    private void Textocolorpicker() {
        final ColorPicker cp = new ColorPicker(this);
        cp.show();
        cp.enableAutoClose();
        cp.setCallback(new ColorPickerCallback() {
            @Override
            public void onColorChosen(final int color) {
                Log.d("Pure Hex", Integer.toHexString(color));
                int colorFrom = quote.getCurrentTextColor();
                ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, color);
                colorAnimation.setDuration(2000); // milliseconds
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        quote.setTextColor(color);
                        author.setTextColor(color);

                    }

                });
                colorAnimation.start();

                texcolorid.setText(String.valueOf(color));
                quotes.setTextcolor(color);


            }
        });
    }

    private void Fontpicker() {
        BottomSheetDialog myDialog = new BottomSheetDialog(this);
        blurView.setBlurRadius(20);
        myDialog.setContentView(R.layout.profilepicselect);
        RecyclerView recyclerView = myDialog.findViewById(R.id.picsrecycler);
        GridLayoutManager llm = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        assert recyclerView != null;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llm);
        RecyclerFontAdapter recyclerFontAdapter = new RecyclerFontAdapter(this, blurView, myDialog, quote, author, fontid);
        recyclerView.setAdapter(recyclerFontAdapter);
        myDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                blurView.setBlurRadius(0);
                blurView.setOverlayColor(android.graphics.Color.TRANSPARENT);

            }
        });
        myDialog.show();
    }


}
