package com.alpebubekir.languageapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Ogrendiklerim extends AppCompatActivity {

    ArrayList<Word> ogrenildi = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerAdapterOgrendiklerim adapterOgrendiklerim;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference ogrenildiRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ogrendiklerim);

        ogrenildiRef = FirebaseDatabase.getInstance("https://languageapp-f2602-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Ogrenildi");
        ogrenildiRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    String ses = null;
                    if (dataSnapshot.child("ses").getValue().toString() != "0")
                    {
                        ses = dataSnapshot.child("ses").getValue().toString();
                    }

                    Word word = new Word(dataSnapshot.child("id").getValue().toString(), dataSnapshot.child("tr").getValue().toString(),dataSnapshot.child("en").getValue().toString(),dataSnapshot.child("link").getValue().toString(),ses);
                    ogrenildi.add(word);

                }
                adapterOgrendiklerim.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        recyclerView = findViewById(R.id.recyclerView2);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapterOgrendiklerim = new RecyclerAdapterOgrendiklerim(this,ogrenildi);
        recyclerView.setAdapter(adapterOgrendiklerim);

    }
}