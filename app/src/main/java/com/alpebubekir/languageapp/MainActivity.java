package com.alpebubekir.languageapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    static String id;
    SharedPreferences sharedPreferences;
    DatabaseReference databaseReferance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseReferance = FirebaseDatabase.getInstance("https://languageapp-f2602-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Kullanici");

        sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (sharedPreferences.contains("id"))
        {
            id = sharedPreferences.getString("id","00000");
        }
        else
        {


            databaseReferance.addListenerForSingleValueEvent(new ValueEventListener() {
                long i;
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    i = snapshot.getChildrenCount() + 1;

                    String newID = Integer.toString((int)i);
                    String zeros = "";

                    for (int j=0; j<5-(newID.length()); j++)
                    {
                        zeros += "0";
                    }

                    id = zeros + newID;
                    databaseReferance.child(id).setValue(i);
                    editor.putString("id",id);
                    editor.apply();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }


    }


    public void kelimeOgren(View view)
    {
        Intent intent = new Intent(this,Ogren.class);
        startActivity(intent);
    }

    public void ogrendiklerim(View view)
    {
        Intent intent = new Intent(this, Ogrendiklerim.class);
        startActivity(intent);
    }

    public void test(View view)
    {

    }
}