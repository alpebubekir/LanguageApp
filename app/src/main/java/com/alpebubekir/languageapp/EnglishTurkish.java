package com.alpebubekir.languageapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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

public class EnglishTurkish extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    RecyclerViewAdapter recyclerViewAdapter;
    StorageReference storageRef;
    ArrayList<Word> words;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_english_turkish);

        recyclerView = findViewById(R.id.recyclerView);
        databaseReference = FirebaseDatabase.getInstance("https://languageapp-f2602-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Ogren");
        storageRef = FirebaseStorage.getInstance("gs://languageapp-f2602.appspot.com").getReference();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        words = new ArrayList<>();
        recyclerViewAdapter = new RecyclerViewAdapter(this,words);
        recyclerView.setAdapter(recyclerViewAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    String ses = null;
                    if (dataSnapshot.child("ses").getValue().toString() != "0" )
                    {
                        ses = dataSnapshot.child("ses").getValue().toString();
                    }

                    Word word = new Word(dataSnapshot.getKey() ,dataSnapshot.child("tr").getValue().toString(),dataSnapshot.child("en").getValue().toString(),dataSnapshot.child("link").getValue().toString(),ses);

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
                }

                recyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }

}