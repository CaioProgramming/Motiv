package com.creat.motiv.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.creat.motiv.Beans.Likes;
import com.creat.motiv.Beans.Quotes;
import com.creat.motiv.Database.QuotesDB;
import com.creat.motiv.R;
import com.creat.motiv.Utils.Alert;
import com.creat.motiv.Utils.ColorUtils;
import com.creat.motiv.Utils.Tools;
import com.creat.motiv.Utils.Typewritter;
import com.devs.readmoreoption.ReadMoreOption;
import com.github.mmin18.widget.RealtimeBlurView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.creat.motiv.Database.QuotesDB.path;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>  {
    private Context mContext;
    private List<Quotes> mData;
    private Activity mActivity;
    private Dialog m_dialog;
    private RealtimeBlurView blur;


    public RecyclerAdapter(Context mContext, List<Quotes> mData, Activity mActivity) {
        this.mContext = mContext;
        this.mData = mData;
        this.mActivity = mActivity;
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
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final Quotes quote = mData.get(holder.getAdapterPosition());
            holder.like.setChecked(false);
            loadLikes(holder, position);


            holder.like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Like(holder.getAdapterPosition(), holder);
                }
            });
            // holder.like.setVisibility(View.GONE);


        if (quote.isReport()) {
                reported(holder);
            }
        Animation in = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
            holder.cardView.startAnimation(in);
            System.out.println("Quote " + mData.get(position).getQuote() + " selected font: " + mData.get(position).getFont());
        if (quote.getFont() != null) {
                holder.quote.setTypeface(Tools.fonts(mContext).get(mData.get(position).getFont()));
                holder.author.setTypeface(Tools.fonts(mContext).get(mData.get(position).getFont()));


            } else {
                holder.quote.setTypeface(Typeface.DEFAULT);
                holder.author.setTypeface(Typeface.DEFAULT);
            }
        Date postdia = Tools.convertDate(quote.getData());
            Date now = Calendar.getInstance().getTime();

        DateFormat fmt = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale.getDefault());


        int dayCount = (int) ((now.getTime() - postdia.getTime()) / 1000 / 60 / 60 / 24);
        if (dayCount < 1) {
                holder.dia.setText("Hoje");
        } else if (dayCount == 1) {
            holder.dia.setText("Ontem");
        } else if (dayCount < 7) {
                holder.dia.setText("Há " + dayCount + " dias");
            } else if (dayCount == 7) {
            holder.dia.setText("Há " + dayCount / 7 + " semana");
        } else if (dayCount == 30) {
            holder.dia.setText("Há " + dayCount / 30 + " mês");
        } else {
            holder.dia.setText(fmt.format(postdia));
        }


        if (quote.getUsername() != null || mData.get(position).getUserphoto() != null) {
            Glide.with(mContext).load(quote.getUserphoto()).addListener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    holder.userpic.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_astronaut));
                    holder.userpic.setBorderColor(Color.BLACK);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    holder.userpic.setImageDrawable(resource);
                    return false;
                }
            }).into(holder.userpic);
            holder.username.setText(quote.getUsername());
            } else {
                holder.username.setVisibility(View.INVISIBLE);
                holder.userpic.setVisibility(View.INVISIBLE);

            }
            if (mData.get(position).getBackgroundcolor() != 0){
                holder.back.setCardBackgroundColor(quote.getBackgroundcolor());

            }
            if (mData.get(position).getTextcolor() != 0){
                holder.quote.setTextColor(quote.getTextcolor());
                holder.author.setTextColor(quote.getTextcolor());
            }








            // OR using options to customize
        int color = ColorUtils.getTransparentColor(quote.getTextcolor());

            ReadMoreOption readMoreOption = new ReadMoreOption.Builder(mContext)
                    .textLength(205, ReadMoreOption.TYPE_CHARACTER)
                    .moreLabel(" Ver mais...")
                    .lessLabel(" Ver menos")
                    .moreLabelColor(color)
                    .lessLabelColor(color)
                    .expandAnimation(true)
                    .build();

        readMoreOption.addReadMoreTo(holder.quote, quote.getQuote());

            holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Vibrator vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
                    if (vibrator.hasVibrator()) {
                        long[] mVibratePattern = new long[]{100, 150};

                        vibrator.vibrate(mVibratePattern, -1); // for 500 ms
                    }
                    FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
                    boolean user = quote.getUserID().equals(u.getUid());
                    Alert alert = new Alert(mActivity);
                    alert.quoteoptions(user, mData.get(holder.getAdapterPosition()));
                    return false;
                }
            });

        holder.quote.animateText(quote.getQuote());
        holder.author.animateText(quote.getAuthor());

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
                    if (l != null) {
                        Log.println(Log.DEBUG, "Likes", "who liked " + l.getUsername());
                        likesArrayList.add(l);
                        if (l.getUserid().equals(userdb.getUid())) {
                            holder.like.setChecked(true);
                        } else {
                            holder.like.setChecked(false);
                        }
                    }

                }
                if (likesArrayList.size() > 1) {
                    final StringBuilder liketext = new StringBuilder();
                    String user = likesArrayList.get(0).getUserid();
                    String username = likesArrayList.get(0).getUsername();
                    if (userdb.getUid().equals(user)) {
                        username = "Você";
                    }
                    liketext.append("Curtido por ");
                    liketext.append("<b>").append(username).append("</b>").append(" e outras pessoas");
                    holder.likecount.setText(Html.fromHtml(String.valueOf(liketext)));

                    holder.likecount.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Alert alert = new Alert(mActivity);
                            alert.Likelist(likesArrayList);
                        }
                    });


                } else if (likesArrayList.size() == 1) {
                    holder.likecount.setText(Html.fromHtml(
                            String.format(" Curtido por %s", "<b>" + likesArrayList.get(0).getUsername() + "</b>")));

                    holder.likecount.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Alert alert = new Alert(mActivity);
                            alert.Likelist(likesArrayList);
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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


    static class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout cardView, likes;
        CheckBox like;
        ImageButton report;
        CircleImageView userpic;
        TextView username, dia, likecount;
        Typewritter quote,author;
        CardView back;
        LinearLayout quotedata;

        MyViewHolder(View view) {
            super(view);
            likes = view.findViewById(R.id.likes);
            likecount = view.findViewById(R.id.likecount);
            quotedata = view.findViewById(R.id.quotedata);
            report = view.findViewById(R.id.reported);
            dia = view.findViewById(R.id.dia);
            like = view.findViewById(R.id.like);
            quote = view.findViewById(R.id.quote);
            author = view.findViewById(R.id.author);
            cardView = view.findViewById(R.id.card);
            back = view.findViewById(R.id.background);
            username = view.findViewById(R.id.username);
            userpic = view.findViewById(R.id.userpic);


        }
    }
}