package com.creat.motiv.Fragments;


import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.creat.motiv.Adapters.RecyclerColorAdapter;
import com.creat.motiv.Adapters.RecyclerFontAdapter;
import com.creat.motiv.Beans.Quotes;
import com.creat.motiv.Database.QuotesDB;
import com.creat.motiv.R;
import com.creat.motiv.Utils.Info;
import com.creat.motiv.Utils.Pref;
import com.github.mmin18.widget.RealtimeBlurView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import de.mateware.snacky.Snacky;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewQuoteFragment extends Fragment {


     String categoria = "Nenhum";
    FirebaseUser user;
    QuotesDB quotesDB;

    Pref preferences;
    boolean tuto = true;
    private RecyclerView colorlibrary;
    private EditText frase;
    private EditText author;
    private LinearLayout background;
    private TextView fontid;
    private TextView texcolorid;
    private TextView backcolorid;
    private Dialog m_dialog;
    private android.support.design.widget.TabLayout categories;

    public NewQuoteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        preferences = new Pref(Objects.requireNonNull(getContext()));
        user = FirebaseAuth.getInstance().getCurrentUser();
        View view = inflater.inflate(R.layout.fragment_newquote, container, false);
        this.categories = view.findViewById(R.id.categories);
        android.widget.Toolbar options = view.findViewById(R.id.options);
        com.github.clans.fab.FloatingActionButton salvar = view.findViewById(R.id.salvar);
        com.github.clans.fab.FloatingActionButton fontpickerfab = view.findViewById(R.id.fontpickerfab);
        com.github.clans.fab.FloatingActionButton backcolorfab = view.findViewById(R.id.backcolorfab);
        com.github.clans.fab.FloatingActionButton textcolorfab = view.findViewById(R.id.textcolorfab);
        com.github.clans.fab.FloatingActionButton categoryfab = view.findViewById(R.id.categoryfab);
        this.backcolorid = view.findViewById(R.id.backcolorid);
        this.texcolorid = view.findViewById(R.id.texcolorid);
        this.fontid = view.findViewById(R.id.fontid);
        this.background = view.findViewById(R.id.background);
        this.author = view.findViewById(R.id.author);
        this.frase = view.findViewById(R.id.quote);
        TextView username = view.findViewById(R.id.username);
        CircleImageView userpic = view.findViewById(R.id.userpic);
        this.colorlibrary = view.findViewById(R.id.colorlibrary);

        username.setText(user.getDisplayName());
        Glide.with(this).load(user.getPhotoUrl()).into(userpic);
        //theme();

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
                categoria = "Nenhum";
                categories.setSelected(false);
            }
        });

        categories.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        categoria = "Amor";
                        break;
                    case 1:
                        categoria = "Música";
                        break;
                    case 2:
                        categoria = "Citação";

                        break;
                    case 3:
                        categoria = "Motivação";

                        break;
                    default:
                        categoria = "Nenhuma";
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



        frase.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_NEXT) {
                    if (!keyEvent.isShiftPressed()) {
                        Log.v("AndroidEnterKeyActivity", "Enter Key Pressed!");
                        switch (view.getId()) {
                            case 1:
                                author.requestFocus();
                                break;
                        }
                        return true;
                    }
                }
                return false;
            }
        });


        author.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH ||
                        i == EditorInfo.IME_ACTION_DONE ||
                        keyEvent.getAction() == KeyEvent.ACTION_DOWN &&
                                keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                    if (!keyEvent.isShiftPressed()) {
                        System.out.println("Enter key pressed");
                        switch (view.getId()) {
                            case 1:
                                salvar();
                                break;
                        }
                        return true;
                    }

                }
                return false; // pass on to other listeners.

            }
        });



        try {
            colorgallery();
        } catch (ClassNotFoundException | IllegalAccessException e) {
            e.printStackTrace();
        }
        if (getActivity() == null) {
            return null;
        }
        Objects.requireNonNull(getActivity()).setActionBar(options);


        return view;
    }


    private void agree() {
        if (getContext() == null) throw new AssertionError();
        preferences = new Pref(getContext());
        m_dialog = new Dialog(getContext(), R.style.Dialog_No_Border);
        Animation in = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_top);
        LayoutInflater m_inflater = LayoutInflater.from(getContext());
        View m_view = m_inflater.inflate(R.layout.politics, null);
        m_dialog.setContentView(m_view);
        Button agreebutton = m_view.findViewById(R.id.agreebutton);
        agreebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_dialog.dismiss();
                preferences.setAgree(true);

            }
        });
        m_view.startAnimation(in);
        if (!preferences.agreestate()) {
            Snacky.builder().setActivity(Objects.requireNonNull(getActivity())).warning()
                    .setText("Você precisa concordar com os termos de uso para usar o aplicativo!")
                    .setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            m_dialog.show();
                        }
                    }).show();
        }
        m_dialog.setCanceledOnTouchOutside(false);
        m_dialog.setCancelable(false);
    }



    private void BackColorpicker() {
        final ColorPicker cp = new ColorPicker(getActivity());
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
        final ColorPicker cp = new ColorPicker(getActivity());
        cp.show();
        cp.enableAutoClose();
        cp.setCallback(new ColorPickerCallback() {
            @Override
            public void onColorChosen(final int color) {
                Log.d("Pure Hex", Integer.toHexString(color));
                int colorFrom = frase.getCurrentTextColor();
                ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, color);
                colorAnimation.setDuration(2000); // milliseconds
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        frase.setTextColor(color);
                        author.setTextColor(color);

                    }

                });
                colorAnimation.start();
                texcolorid.setText(String.valueOf(color));


            }
        });
    }

    private void Fontpicker() {
         BottomSheetDialog myDialog = new BottomSheetDialog(Objects.requireNonNull(getContext()));
        final RealtimeBlurView blurView = Objects.requireNonNull(getActivity()).findViewById(R.id.rootblur);
        blurView.setVisibility(View.VISIBLE);
        blurView.setBlurRadius(50);
        myDialog.setContentView(R.layout.profilepicselect);
        RecyclerView recyclerView = myDialog.findViewById(R.id.picsrecycler);
        GridLayoutManager llm = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
        assert recyclerView != null;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llm);
        RecyclerFontAdapter recyclerFontAdapter = new RecyclerFontAdapter(getContext(), blurView, myDialog, frase, author, fontid);
        recyclerView.setAdapter(recyclerFontAdapter);
        myDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                blurView.setBlurRadius(0);
                blurView.setOverlayColor(Color.TRANSPARENT);

            }
        });
        myDialog.show();
    }

    private void Tutorial() {
        Boolean novo = Objects.requireNonNull(Objects.requireNonNull(getActivity()).getIntent().getExtras()).getBoolean("novo");
        if (novo){
            Pref preferences = new Pref(Objects.requireNonNull(getContext()));
            if (!preferences.writetutorialstate()) {
                preferences.setWriteTutorial(true);
                Info.tutorial(getString(R.string.new_quoteintro), getActivity());
            }

            //getData();
        }
        tuto = false;
    }


    public void salvar() {
        agree();
        Date datenow = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String dia = df.format(datenow);
        System.out.println(dia);
        Integer fonti = null;
        if (fontid.getText() != "") {
            fonti = Integer.parseInt(fontid.getText().toString());

        }
        if (texcolorid.getText() == ""){
            texcolorid.setText(String.valueOf(Color.BLACK));
        }
        if (backcolorid.getText() == ""){
            backcolorid.setText(String.valueOf(Color.WHITE)  );
        }

        Quotes quotes = new Quotes(null,
                frase.getText().toString(),
                author.getText().toString(),
                dia, categoria,
                user.getUid(),
                user.getDisplayName(),
                String.valueOf(user.getPhotoUrl()),
                Integer.parseInt(backcolorid.getText().toString()),
                Integer.parseInt( texcolorid.getText().toString()),
                false,
                false,
                fonti,
                false);
        System.out.println("font " + quotes.getFont() + "backcolor e textcolor " + quotes.getBackgroundcolor() + " "+ quotes.getTextcolor());

        if (author.getText().toString().equals("")){
            quotes.setAuthor(user.getDisplayName());
        }
        if (user.isEmailVerified()){
        quotesDB = new QuotesDB(quotes, Objects.requireNonNull(getActivity()));
        quotesDB.Inserir();
        }else{

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext()).setMessage("Email não verificado");
            builder.setMessage("Seu email não foi verificado, então não vai poder compartilhar frases.");
            builder.setPositiveButton("Então me envia esse email meu consagrado", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    user.sendEmailVerification();
                }
            });
            builder.setNegativeButton("Beleza então, não vou postar nada", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            builder.show();



        }



    }



    public void colorgallery() throws ClassNotFoundException, IllegalAccessException {
        ArrayList<Integer> colors = new ArrayList<>();
        Field[] fields = Class.forName(Objects.requireNonNull(getContext()).getPackageName() +".R$color").getDeclaredFields();
        for(Field field : fields) {
            String colorName = field.getName();
            int colorId = field.getInt(null);
            int color = getContext().getResources().getColor(colorId);
            System.out.println("color " + colorName + " " + color);
            colors.add(color);

        }

        System.out.println("Load " + colors.size() + " colors");
        Collections.reverse(colors);
        colorlibrary.setHasFixedSize(true);
        GridLayoutManager llm = new GridLayoutManager(getActivity(), 3, GridLayoutManager.HORIZONTAL, false);
        RecyclerColorAdapter recyclerColorAdapter = new RecyclerColorAdapter(colors,getContext(),
                background,frase,author,getActivity(),texcolorid,backcolorid);
        colorlibrary.setAdapter(recyclerColorAdapter);
        colorlibrary.setLayoutManager(llm);
    }

    @Override
    public void onStart() {
        super.onStart();
        Tutorial();
    }









}
