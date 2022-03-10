package com.alpebubekir.languageapp;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>{

    Context context;
    ArrayList<Word> list;

    public RecyclerViewAdapter(Context context, ArrayList<Word> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.cards,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Word word = list.get(position);
        holder.tr.setText(word.getTr());
        holder.en.setText(word.getEn());
        Picasso.with(context).load(word.getLink()).into(holder.image);
        holder.word = word;
        holder.context = context;

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tr,en;
        ImageView image;
        Word word;
        Context context;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tr = itemView.findViewById(R.id.turkce);
            en = itemView.findViewById(R.id.ingilizce);
            image = itemView.findViewById(R.id.image);
            itemView.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DatabaseReference ogrenildiRef = FirebaseDatabase.getInstance("https://languageapp-f2602-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Ogrenildi");

                    ogrenildiRef.addValueEventListener(new ValueEventListener() {
                        boolean y = true;
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean x=true;
                            for (DataSnapshot dataSnapshot : snapshot.getChildren())
                            {
                                if (dataSnapshot.getKey().toString() == word.getId())
                                {
                                    x = false;
                                }
                            }

                            if (x)
                            {
                                ogrenildiRef.child(word.getId()).setValue(word);
                                DatabaseReference databaseReferenceOgren = FirebaseDatabase.getInstance("https://languageapp-f2602-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Ogren");
                                databaseReferenceOgren.child(word.getId()).removeValue();

                                Toast.makeText(context,"Tebrikler! Bu kelimeyi öğrendiniz!",Toast.LENGTH_SHORT).show();
                                y = false;
                            }

                            else if(y)
                            {
                                Toast.makeText(context,"Bu kelimeyi zaten öğrendiniz!",Toast.LENGTH_SHORT).show();
                                y = false;
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });

            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MediaPlayer mediaPlayer = new MediaPlayer();

                    try{
                        mediaPlayer.setDataSource(word.getSes());
                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mediaPlayer) {
                                mediaPlayer.start();
                            }
                        });

                        mediaPlayer.prepare();


                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }
            });
        }

    }

}

