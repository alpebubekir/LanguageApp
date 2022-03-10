package com.alpebubekir.languageapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void kelimeOgren(View view)
    {
        Intent intent = new Intent(this,Ogren.class);
        startActivity(intent);
    }

    public void ogrendiklerim(View view)
    {
        Intent intent = new Intent(this,Ogrendiklerim.class);
        startActivity(intent);
    }

    public void test(View view)
    {

    }
}