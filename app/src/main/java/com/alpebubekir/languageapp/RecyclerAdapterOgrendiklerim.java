package com.alpebubekir.languageapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerAdapterOgrendiklerim extends RecyclerView.Adapter<RecyclerAdapterOgrendiklerim.MyViewHolder2>{

    Context context;
    ArrayList<Word> words;

    public static class MyViewHolder2 extends RecyclerView.ViewHolder{

        TextView turkce,ingilizce;
        ImageView image;

        public MyViewHolder2(@NonNull View itemView) {
            super(itemView);

            turkce = itemView.findViewById(R.id.turkce2);
            ingilizce = itemView.findViewById(R.id.ingilizce2);
            image = itemView.findViewById(R.id.image2);
        }
    }

    public RecyclerAdapterOgrendiklerim (Context context, ArrayList<Word> list)
    {
        this.context = context;
        this.words = list;
    }

    @NonNull
    @Override
    public RecyclerAdapterOgrendiklerim.MyViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.cards2, parent, false);
        return new MyViewHolder2(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterOgrendiklerim.MyViewHolder2 holder, int position) {
        Word word = words.get(position);
        holder.turkce.setText(word.getTr());
        holder.ingilizce.setText(word.getEn());
        Picasso.with(context).load(word.getLink()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return words.size();
    }
}
