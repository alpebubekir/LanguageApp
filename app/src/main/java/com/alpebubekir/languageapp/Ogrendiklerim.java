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

public class Ogrendiklerim extends AppCompatActivity {

    int enCount;
    TextView enTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ogrendiklerim);

        enCount = 0;
        enTV = findViewById(R.id.englishWordNumbers);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://languageapp-f2602-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Ogren");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    if (dataSnapshot.child("ogrenen").child(MainActivity.id).exists())
                    {
                        enCount++;
                    }
                }

                enTV.setText(enCount + " Kelime");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void English(View view)
    {
        Intent intent = new Intent(this,OgrendiklerimEn.class);
        startActivity(intent);
    }
}