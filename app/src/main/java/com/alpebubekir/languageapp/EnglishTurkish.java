package com.alpebubekir.languageapp;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Currency;

public class EnglishTurkish extends AppCompatActivity {
    DatabaseReference databaseReference;
    TextView tr,en;
    ImageView image;
    ArrayList<Word> words;
    int position = 0;
    ArrayList<Word> ogrendim;
    private int currentProgress;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_english_turkish);

        progressBar = findViewById(R.id.progress_horizontal);
        currentProgress = 0;
        tr = findViewById(R.id.textViewTR);
        en = findViewById(R.id.textViewEN);
        image = findViewById(R.id.imageViewWord);
        final int[] sizeWords = new int[1];
        System.out.println("def");
        databaseReference = FirebaseDatabase.getInstance("https://languageapp-f2602-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Ogren");

        words = new ArrayList<>();
        ogrendim = new ArrayList<>();

        System.out.println("abc");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println(MainActivity.id);
                for (DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    if (!dataSnapshot.child("ogrenen").child(MainActivity.id).exists())
                    {
                        String ses = null;
                        if (dataSnapshot.child("ses").getValue().toString() != "0" )
                        {
                            ses = dataSnapshot.child("ses").getValue().toString();
                        }

                        Word word = new Word(dataSnapshot.getKey(), dataSnapshot.child("tr").getValue().toString(),dataSnapshot.child("en").getValue().toString(),dataSnapshot.child("link").getValue().toString(),ses);

                        boolean x = true;

                        for (Word i: words)
                        {
                            if (i.getId() == word.getId())
                            {
                                x = false;
                            }
                        }

                        if (x)
                        {
                            words.add(word);
                        }
                        sizeWords[0] = words.size();
                        progressBar.setMax(sizeWords[0]);
                        tr.setText(words.get(position).getTr());
                        en.setText(words.get(position).getEn());
                        Picasso.with(EnglishTurkish.this).load(words.get(position).getLink()).into(image);
                        image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MediaPlayer mediaPlayer = new MediaPlayer();

                                try{
                                    mediaPlayer.setDataSource(words.get(position).getSes());
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
                        //System.out.println(words.size());
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {



            }

        });


        findViewById(R.id.ogrendim).setOnClickListener(new View.OnClickListener() {
            boolean isExist;
            double childCount;
            @Override
            public void onClick(View view) {

                DatabaseReference databaseReferance = FirebaseDatabase.getInstance("https://languageapp-f2602-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Ogren");
                databaseReferance = databaseReferance.child(words.get(position).getId());
                isExist = false;

                databaseReferance.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        childCount = snapshot.getChildrenCount();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                if (childCount == 0)
                {
                    databaseReferance.child("ogrenen").child(MainActivity.id).setValue(MainActivity.id);
                    Toast.makeText(EnglishTurkish.this,"Tebrikler! Bu kelimeyi öğrendiniz.",Toast.LENGTH_SHORT).show();
                    words.get(position).setOgrendi(true);
                    sizeWords[0]--;
                    progressBar.setMax(sizeWords[0]);
                }
                else
                {
                    databaseReferance.child("ogrenen").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot: snapshot.getChildren())
                            {
                                if (dataSnapshot.getValue().equals(MainActivity.id))
                                {
                                    isExist = true;
                                    break;
                                }
                            }

                            if (isExist)
                            {
                                Toast.makeText(EnglishTurkish.this,"Bu kelimeyi zaten öğrendiniz.",Toast.LENGTH_SHORT).show();
                            }

                            else
                            {
                                Toast.makeText(EnglishTurkish.this,"Tebrikler! Bu kelimeyi öğrendiniz.",Toast.LENGTH_SHORT).show();
                                snapshot.child(MainActivity.id).getRef().setValue(MainActivity.id);
                                words.get(position).setOgrendi(true);
                                sizeWords[0]--;
                                progressBar.setMax(sizeWords[0]);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                        /*System.out.println(isExist);

                        if (isExist)
                        {
                            System.out.println("def\n");
                            Toast.makeText(context,"Bu kelimeyi zaten öğrendiniz.",Toast.LENGTH_SHORT).show();
                        }

                        else
                        {
                            System.out.println("abc\n");
                            Toast.makeText(context,"Tebrikler! Bu kelimeyi öğrendiniz.",Toast.LENGTH_SHORT).show();
                            databaseReferance.child("ogrenen").child(MainActivity.id).setValue(MainActivity.id);
                        }*/
                }
                sonraki(view);
            }
        });



    }

    public void sonraki(View view)
    {

        if (!(position < words.size()-1))
        {
            Toast.makeText(this, "Kelimeler bitti.", Toast.LENGTH_SHORT).show();
        }

        else
        {
            if (words.get(position+1).isOgrendi())
            {
                position++;
                sonraki(view);
            }
            else {
                currentProgress = currentProgress + 1;
                progressBar.setProgress(currentProgress);

                position++;
                tr.setText(words.get(position).getTr());
                en.setText(words.get(position).getEn());
                Picasso.with(EnglishTurkish.this).load(words.get(position).getLink()).into(image);
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onceki(View view)
    {
        if (position==0)
        {
            Toast.makeText(this, "Kelimeler bitti.", Toast.LENGTH_SHORT).show();
        }

        else
        {
            if (words.get(position-1).isOgrendi())
            {
                position--;
                onceki(view);
            }
            else {
                currentProgress = currentProgress - 1;
                progressBar.setProgress(currentProgress);

                position--;
                tr.setText(words.get(position).getTr());
                en.setText(words.get(position).getEn());
                Picasso.with(EnglishTurkish.this).load(words.get(position).getLink()).into(image);
            }
        }
    }

    public void geri(View view)
    {
        Intent intent = new Intent(this,Ogren.class);
        startActivity(intent);
    }

    /*public void ogrendim(View view)
    {
        DatabaseReference databaseReferance1 = FirebaseDatabase.getInstance("https://languageapp-f2602-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Ogren");
        databaseReferance1 = databaseReferance1.child(words.get(position).getId());
        final boolean[] isExist = {false};
        final int[] childCount = new int[1];

        databaseReferance1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                childCount[0] = snapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (childCount[0] == 0)
        {
            databaseReferance1.child("ogrenen").child(MainActivity.id).setValue(MainActivity.id);
            Toast.makeText(this,"Tebrikler!",Toast.LENGTH_SHORT).show();
        }
        else
        {
            databaseReferance1.child("ogrenen").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot: snapshot.getChildren())
                    {
                        if (dataSnapshot.getValue().equals(MainActivity.id))
                        {
                            isExist[0] = true;
                            break;
                        }
                    }

                    if (isExist[0])
                    {
                        Toast.makeText(this,"Bu kelimeyi zaten öğrendiniz.",Toast.LENGTH_SHORT).show();
                    }

                    else
                    {
                        Toast.makeText(this,"Tebrikler! Bu kelimeyi öğrendiniz.",Toast.LENGTH_SHORT).show();
                        snapshot.child("ogrenen").child(MainActivity.id).getRef().setValue(MainActivity.id);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

                        System.out.println(isExist);

                        if (isExist)
                        {
                            System.out.println("def\n");
                            Toast.makeText(context,"Bu kelimeyi zaten öğrendiniz.",Toast.LENGTH_SHORT).show();
                        }

                        else
                        {
                            System.out.println("abc\n");
                            Toast.makeText(context,"Tebrikler! Bu kelimeyi öğrendiniz.",Toast.LENGTH_SHORT).show();
                            databaseReferance.child("ogrenen").child(MainActivity.id).setValue(MainActivity.id);
                        }
        }
    }*/

}