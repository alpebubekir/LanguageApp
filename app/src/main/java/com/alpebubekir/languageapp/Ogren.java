package com.alpebubekir.languageapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Ogren extends AppCompatActivity {

    int enCount;
    TextView engTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ogren);
        enCount = 0;
        engTV = findViewById(R.id.englishWordNumbers);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://languageapp-f2602-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Ogren");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    if (!dataSnapshot.child("ogrenen").child(MainActivity.id).exists())
                    {
                        enCount++;
                    }
                }

                engTV.setText(enCount + " Kelime");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void englishTurkish(View view)
    {
        Intent intent = new Intent(this,EnglishTurkish.class);
        startActivity(intent);
    }
}