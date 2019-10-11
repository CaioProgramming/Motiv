package com.creat.motiv.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.creat.motiv.Beans.Artists;
import com.creat.motiv.R;
import com.creat.motiv.Utils.Tools;

import java.util.ArrayList;


public class RecyclerReferencesAdapter extends RecyclerView.Adapter<RecyclerReferencesAdapter.MyViewHolder>  {
    private ArrayList<Artists> mData;
    private Activity mActivity;


    public RecyclerReferencesAdapter(Activity mActivity) {
        this.mActivity = mActivity;
        mData = Tools.references(mActivity);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mActivity);
        view = mInflater.inflate(R.layout.references_layout, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final Artists a = mData.get(holder.getAdapterPosition());
        holder.nome.setText(a.getNome());
        holder.nome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(a.getUri());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                mActivity.startActivity(intent);
            }
        });
        Log.println(Log.INFO,"REFERENCE color", String.valueOf(a.getColor()));
        holder.nome.setTextColor(a.getColor());



    }



    @Override
    public int getItemCount() {

        return mData.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nome;
        MyViewHolder(View view) {
            super(view);
            nome = view.findViewById(R.id.reference);



        }
    }
}
