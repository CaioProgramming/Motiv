package com.creat.motiv.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.creat.motiv.Beans.Likes;
import com.creat.motiv.Beans.Quotes;
import com.creat.motiv.Database.QuotesDB;
import com.creat.motiv.R;
import com.creat.motiv.Utils.ColorUtils;
import com.creat.motiv.Utils.NewQuotepopup;
import com.creat.motiv.Utils.Tools;
import com.devs.readmoreoption.ReadMoreOption;
import com.github.mmin18.widget.RealtimeBlurView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.mateware.snacky.Snacky;

import static android.content.Context.CLIPBOARD_SERVICE;
import static com.creat.motiv.Database.QuotesDB.path;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>  {
    private Context mContext;
    private List<Quotes> mData;
    private Activity mActivity;
    private RecyclerView view;
    private boolean longpress = false;
    Dialog m_dialog;
    RealtimeBlurView blur;
    boolean expandable = false;
    boolean expand = false;


    public RecyclerAdapter( Context mContext, List<Quotes> mData,  Activity mActivity,RecyclerView view) {
        if (mActivity == null){
            return;
        }
        this.mContext = mContext;
        this.mData = mData;
        this.mActivity = mActivity;
        this.view = view;
        m_dialog= new Dialog(mActivity, R.style.Dialog_No_Border) ;
        m_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        blur = mActivity.findViewById(R.id.rootblur);


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.quotescard,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.like.setChecked(false);
        loadLikes(holder, position);



        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Like(position, holder);
            }
        });
        // holder.like.setVisibility(View.GONE);


        if (mData.get(position).isReport()){
            reported(holder);
        }
        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Vibrator vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
                if (vibrator.hasVibrator()) {
                    long[] mVibratePattern = new long[]{100, 150};

                    vibrator.vibrate(mVibratePattern,-1); // for 500 ms
                }
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (mData.get(position).getUserID().equals(user.getUid())){
                    Usermenu(holder, position);
                }else{
                    PopupMenu popup = new PopupMenu(mContext, holder.menu);
                    //inflating menu from xml resource
                    popup.inflate(R.menu.quotesoptions);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {


                            switch (item.getItemId()) {
                                case R.id.compartilhar:
                                    Intent share = new Intent(Intent.ACTION_SEND);
                                    share.setType("text/pain");
                                    share.putExtra(Intent.EXTRA_SUBJECT, "Motiv");
                                    share.putExtra(Intent.EXTRA_TEXT, mData.get(position).getQuote() + " -" + mData.get(position).getAuthor());
                                    mContext.startActivity(Intent.createChooser(share, "Escolha onde quer compartilhar"));
                                    return true;
                                case R.id.denunciar:
                                    final AlertDialog.Builder builder = new AlertDialog.Builder(mActivity).setTitle("Parece que alguém fez algo errado").setMessage("Opa,opa,opa, uma denúncia? Tem certeza que está frase tem algo inapropriado para a comunidade")
                                            .setNegativeButton("Não vou mais denunciar", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.dismiss();
                                                }
                                            })
                                            .setPositiveButton("Sim, quero denunciar esta frase", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    QuotesDB quotesDB = new QuotesDB(mActivity,mData.get(position));
                                                    quotesDB.Denunciar();
                                                }
                                            }) ;
                                    builder.show();
                                    return true;
                                case R.id.Copiar:
                                    ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(CLIPBOARD_SERVICE);
                                    ClipData clip = ClipData.newPlainText("frase", mData.get(position).getQuote());
                                    clipboard.setPrimaryClip(clip);
                                    Snacky.builder().setActivity(mActivity).setBackgroundColor(Color.BLACK).
                                            setText("Frase " + mData.get(position).getQuote() +
                                                    "copiado para área de transferência").build().show();

                                    return true;
                                default:
                                    return false;


                            }

                        }
                    });

                    popup.show();
                }
            }
        });
            Animation in = AnimationUtils.loadAnimation(mContext, R.anim.pop_in);
            holder.cardView.startAnimation(in);
            System.out.println("Quote " + mData.get(position).getQuote() + " selected font: " + mData.get(position).getFont());
            if (mData.get(position).getFont() != null) {
                holder.quote.setTypeface(Tools.fonts(mContext).get(mData.get(position).getFont()));
                holder.author.setTypeface(Tools.fonts(mContext).get(mData.get(position).getFont()));


            } else {
                holder.quote.setTypeface(Typeface.DEFAULT);
                holder.author.setTypeface(Typeface.DEFAULT);
            }
            Date postdia = Tools.convertDate(mData.get(position).getData());
            Date now = Calendar.getInstance().getTime();

            Integer dayCount = (int) ((now.getTime() - postdia.getTime()) / 1000 / 60 / 60 / 24);
            if (dayCount <= 1) {
                holder.dia.setText("Hoje");
                if (dayCount == 1) {
                    holder.dia.setText("Ontem");
                }
            } else {
                holder.dia.setText("Há " + dayCount + " dias");
            }
            System.out.println("Day counter " + dayCount);
            if (dayCount >= 7) {
                dayCount = dayCount / 7;
                if (dayCount == 1) {
                    holder.dia.setText("Há " + dayCount + " semana");
                } else {
                    holder.dia.setText("Há " + dayCount + " semanas");
                }
                if (dayCount > 4) {
                    dayCount = (int) ((now.getTime() - postdia.getTime()) / 1000 / 60 / 60 / 24);
                    dayCount = dayCount / 30;


                }
            }
            if (dayCount >= 30) {
                if (dayCount % 30 == 0) {
                    dayCount = dayCount / 30;
                    if (dayCount == 1) {
                        holder.dia.setText("Há " + dayCount + " mês");
                    } else {
                        holder.dia.setText("Há " + dayCount + " meses");
                    }
                } else {
                    dayCount = dayCount / 30;
                    holder.dia.setText("Há " + dayCount + " meses");
                }
            }


            if (mData.get(position).getUsername() != null || mData.get(position).getUserphoto() != null) {
                System.out.println(mData.get(position).getUserphoto());
                System.out.println(mData.get(position).getUsername());
                Glide.with(mContext).load(mData.get(position).getUserphoto()).into(holder.userpic);
                holder.username.setText(mData.get(position).getUsername());
            } else {
                holder.username.setVisibility(View.INVISIBLE);
                holder.userpic.setVisibility(View.INVISIBLE);

            }
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (mData.get(position).getUserID().equals(user.getUid())){
                holder.remove.setVisibility(View.VISIBLE);
            }else{
                holder.remove.setVisibility(View.INVISIBLE);
            }
            Animation faAnimation = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
            if (mData.get(position).getBackgroundcolor() != 0){
                holder.back.setBackgroundTintList(ColorStateList.valueOf(mData.get(position).getBackgroundcolor()));

            }
            if (mData.get(position).getTextcolor() != 0){
                holder.quote.setTextColor(mData.get(position).getTextcolor());
                holder.author.setTextColor(mData.get(position).getTextcolor());
            }


        Animation animationtext = AnimationUtils.loadAnimation(mContext,R.anim.fade_in);
            holder.quote.setText(mData.get(position).getQuote());
            holder.quote.startAnimation(animationtext);
            holder.author.startAnimation(animationtext);
            holder.author.setText(mData.get(position).getAuthor());
            holder.quote.startAnimation(faAnimation);
            holder.author.startAnimation(faAnimation);
            switch (mData.get(position).getCategoria()) {
                case "Musica":
                    holder.cardView.setBackgroundResource(R.drawable.bottom_line_music);

                    break;
                case "Citação":
                    holder.cardView.setBackgroundResource(R.drawable.bottom_line_citation);
                    break;
                case "Amor":
                    holder.cardView.setBackgroundResource(R.drawable.bottom_line_love);

                    break;
                case "Motivação":
                    holder.cardView.setBackgroundResource(R.drawable.bottom_line_motivation);

                    break;
                case "Nenhum":
                    holder.cardView.setBackgroundResource(R.drawable.bottom_line_none);

                    break;
            }






            holder.remove.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Remover(position, holder);

                }
            });


        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (m_dialog.isShowing()){
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    int action = event.getActionMasked();
                    if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL){
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        m_dialog.dismiss();
                        blur.setVisibility(View.GONE);
                        return true;
                    }
                }
                return false;

            }
        });


        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                dialog(position, holder);
                //return longpress;
                return false;



            }
        });


        // OR using options to customize
        int color = ColorUtils.getTransparentColor(mData.get(position).getTextcolor());

        ReadMoreOption readMoreOption = new ReadMoreOption.Builder(mContext)
                .textLength(205, ReadMoreOption.TYPE_CHARACTER)
                .moreLabel(" Ver mais...")
                .lessLabel(" Ver menos")
                .moreLabelColor(color)
                .lessLabelColor(color)
                .expandAnimation(true)
                .build();

        readMoreOption.addReadMoreTo(holder.quote, mData.get(position).getQuote());












    }

    private void showmore(@NonNull MyViewHolder holder) {
        Animation bottom = AnimationUtils.loadAnimation(mContext, R.anim.fab_scale_up);


        if (holder.quotedata.getVisibility() == View.GONE) {
            holder.quotedata.setVisibility(View.VISIBLE);
            holder.quotedata.startAnimation(bottom);
        } else {
            holder.quotedata.setVisibility(View.GONE);

        }
    }

    private void dialog(int position, @NonNull MyViewHolder holder) {
        Quotes q = mData.get(position);
        longpress = true;
        blur.setVisibility(View.VISIBLE);

        m_dialog = new Dialog(mActivity, R.style.Dialog_No_Border);
        m_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


        LayoutInflater m_inflater = LayoutInflater.from(mActivity);
        final View m_view = m_inflater.inflate(R.layout.quotepopup, null);


        TextView author = m_view.findViewById(R.id.author);
        TextView quote = m_view.findViewById(R.id.quote);
        LinearLayout lt = m_view.findViewById(R.id.popup);

        lt.setBackgroundTintList(ColorStateList.valueOf(q.getBackgroundcolor()));
        author.setTextColor(q.getTextcolor());
        author.setText(q.getAuthor());
        quote.setText(q.getQuote());
        quote.setTextColor(q.getTextcolor());

        author.setTypeface(holder.quote.getTypeface());
        quote.setTypeface(holder.quote.getTypeface());
        Animation in = AnimationUtils.loadAnimation(mContext,R.anim.fab_scale_up);
        final Animation out = AnimationUtils.loadAnimation(mContext,R.anim.fab_scale_down);

        m_dialog.setContentView(m_view);
        m_view.startAnimation(in);
        m_dialog.show();
        m_dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                m_view.startAnimation(out);
                m_view.setVisibility(View.GONE);
                m_dialog.dismiss();

            }
        });
        m_dialog.setCanceledOnTouchOutside(true);

    }

    private void reported(@NonNull MyViewHolder holder) {
        holder.report.setVisibility(View.VISIBLE);
        holder.report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blur.setVisibility(View.VISIBLE);
                m_dialog = new Dialog(mActivity, R.style.Dialog_No_Border);
                m_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                m_dialog.setCanceledOnTouchOutside(true);
                LayoutInflater m_inflater = LayoutInflater.from(mActivity);
                final View m_view = m_inflater.inflate(R.layout.quotepopup, null);
                m_dialog.setContentView(m_view);
                LinearLayout popup = m_view.findViewById(R.id.popup);
                TextView author = m_view.findViewById(R.id.author);
                TextView quote = m_view.findViewById(R.id.quote);
                quote.setText(R.string.reported);
                quote.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_flag_black_24dp, 0, 0, 0);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    quote.setCompoundDrawableTintList(ColorStateList.valueOf(Color.WHITE));
                }
                quote.setTextSize(16);
                quote.setTextColor(Color.WHITE);
                author.setVisibility(View.GONE);
                popup.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));
                m_dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        blur.setVisibility(View.GONE);
                    }
                });
                m_dialog.show();
            }
        });
    }

    private void Like(int position, @NonNull MyViewHolder holder) {
        QuotesDB quotesDB = new QuotesDB(mActivity,mData.get(position));
        if (!holder.like.isChecked()) {
            quotesDB.deslike();
        }else{
            quotesDB.like();
        }
    }

    private void loadLikes(@NonNull final MyViewHolder holder, int position) {
        final ArrayList<Likes> likesArrayList = new ArrayList<>();
        final FirebaseUser userdb = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference quotedb = FirebaseDatabase.getInstance().getReference();
        quotedb.child(path).child(mData.get(position).getId()).child("likes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                likesArrayList.clear();
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    Likes l = d.getValue(Likes.class);
                    Likes likes = new Likes(l.getUserid(),l.getUsername(),l.getUserpic());
                    likesArrayList.add(likes);
                    if (l.getUserid().equals(userdb.getUid())) {
                        holder.like.setChecked(true);
                    }else{
                        holder.like.setChecked(false);
                    }

                }
                holder.like.setText(String.valueOf(dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void Usermenu(@NonNull MyViewHolder holder, final int position) {
        PopupMenu popup = new PopupMenu(mContext,holder.menu);
        popup.inflate(R.menu.quotesoptions_user);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.compartilhar:
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("text/pain");
                        share.putExtra(Intent.EXTRA_SUBJECT,"My app");
                        share.putExtra(Intent.EXTRA_TEXT,mData.get(position).getQuote() + " -" +mData.get(position).getAuthor());
                        mContext.startActivity(Intent.createChooser(share,"Escolha onde quer compartilhar"));
                        return true;
                    case R.id.denunciar:
                        final AlertDialog.Builder builder = new AlertDialog.Builder(mActivity).setTitle("Parece que alguém fez algo errado").setMessage("Opa,opa,opa, uma denúncia? Tem certeza que está frase tem algo inapropriado para a comunidade")
                                .setNegativeButton("Não vou mais denunciar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                })
                                .setPositiveButton("Sim, quero denunciar esta frase", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        QuotesDB quotesDB = new QuotesDB(mActivity,mData.get(position));
                                        quotesDB.Denunciar();
                                    }
                                }) ;
                        builder.show();
                        Snacky.builder().setActivity(mActivity)
                                .setText("Em desenvolvimento")
                                .setBackgroundColor(Color.BLACK)
                                .setTextColor(Color.WHITE).build().show();
                        return true;

                    case R.id.Copiar:
                        ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("frase", mData.get(position).getQuote());
                        clipboard.setPrimaryClip(clip);
                        Snacky.builder().setActivity(mActivity).setBackgroundColor(Color.BLACK).
                                setText("Frase " + mData.get(position).getQuote() +
                                        "copiado para área de transferência").build().show();

                        return true;
                    default:
                        return false;

                    case R.id.editar:
                        NewQuotepopup newQuotepopup = new NewQuotepopup(mActivity, blur);
                        newQuotepopup.showedit(mData.get(position).getId());
                        return true;


                }

            }
        });
        popup.show();
    }


    private void OpenUser(String uid) {

    }


    private void Remover(final int position, @NonNull final MyViewHolder holder) {
        DatabaseReference raiz;
        raiz = FirebaseDatabase.getInstance().getReference(path);
        raiz.child(mData.get(position).getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Animation out = AnimationUtils.loadAnimation(mContext, R.anim.fab_scale_down);
                    holder.cardView.startAnimation(out);
                    holder.cardView.setVisibility(View.INVISIBLE);
                }else {
                    Snacky.builder().setActivity(mActivity).error().setText("Erro " + task.getException().getMessage()).show();

                }

            }
        });
    }


    @Override
    public int getItemCount() {
        if(mData.size() == 0){
            return 0;

        }else{
            return mData.size();}
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout cardView;
        CheckBox like;
        ImageButton remove, menu, report;
        ImageView userpic;
        TextView quote,author,username,dia;
        LinearLayout back;
        LinearLayout quotedata;

        public MyViewHolder(View view) {
            super(view);
            quotedata = view.findViewById(R.id.quotedata);
            menu = view.findViewById(R.id.menu);
            report = view.findViewById(R.id.reported);
            dia = view.findViewById(R.id.dia);
            like = view.findViewById(R.id.like);
            remove = view.findViewById(R.id.remove);
            quote = view.findViewById(R.id.quote);
            author = view.findViewById(R.id.author);
            cardView = view.findViewById(R.id.card);
            back = view.findViewById(R.id.background);
            username = view.findViewById(R.id.username);
            userpic = view.findViewById(R.id.userpic);


        }
    }
}