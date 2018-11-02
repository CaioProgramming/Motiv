package com.creat.motiv.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.creat.motiv.Beans.Category;
import com.creat.motiv.R;

import java.util.List;

public class RecyclerCategoryAdapter extends RecyclerView.Adapter<RecyclerCategoryAdapter.MyViewHolder>  {
    private Context mContext;
    private List<Category> mData;
    private Activity mActivity;
    private EditText search;

    public RecyclerCategoryAdapter(Context mContext, List<Category> mData,
                                   Activity mActivity, EditText search) {
        this.mContext = mContext;
        this.mData = mData;
        this.mActivity = mActivity;
        this.search = search;
     }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.category_card,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.category.setText(mData.get(position).getName());
        Glide.with(mActivity).asGif().load(mData.get(position).getImageuri()).into(holder.categoryback);
        holder.category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search.setText(mData.get(position).getName());
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
        CardView cardView;
        ImageView categoryback;
        TextView category;

        public MyViewHolder(View view) {
            super(view);
            categoryback = itemView.findViewById(R.id.categoryback);
            cardView = itemView.findViewById(R.id.card);
            category = itemView.findViewById(R.id.category);






        }
    }
}
