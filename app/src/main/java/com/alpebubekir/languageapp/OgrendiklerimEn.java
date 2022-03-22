package com.alpebubekir.languageapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OgrendiklerimEn extends AppCompatActivity {

    ArrayList<Word> ogrenildi = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerAdapterOgrendiklerim adapterOgrendiklerim;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference ogrenildiRef;
    EditText searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ogrendiklerim_en);
        searchBar = findViewById(R.id.search_bar);

        ogrenildiRef = FirebaseDatabase.getInstance("https://languageapp-f2602-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Ogren");
        ogrenildiRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    if (dataSnapshot.child("ogrenen").child(MainActivity.id).exists())
                    {
                        String ses = null;
                        if (dataSnapshot.child("ses").getValue().toString() != "0")
                        {
                            ses = dataSnapshot.child("ses").getValue().toString();
                        }

                        Word word = new Word(dataSnapshot.child("id").getValue().toString(), dataSnapshot.child("tr").getValue().toString(),dataSnapshot.child("en").getValue().toString(),dataSnapshot.child("link").getValue().toString(),ses);
                        ogrenildi.add(word);
                    }
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

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                ArrayList<Word> filteredList = new ArrayList<>();
                System.out.println(editable.toString());
                for (Word i: ogrenildi)
                {
                    System.out.println(i.getTr().toLowerCase());
                    if (i.getTr().toLowerCase().contains(editable.toString()) || i.getEn().toLowerCase().contains(editable.toString()))
                    {
                        filteredList.add(i);
                    }
                }

                adapterOgrendiklerim.filterList(filteredList);
            }
        });

    }

    public void geri (View view)
    {
        Intent intent = new Intent(this,Ogrendiklerim.class);
        startActivity(intent);
    }

}