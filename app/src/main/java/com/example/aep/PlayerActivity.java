package com.example.aep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PlayerActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);


    }

    public void SportsRegistration(View view){

        Intent intent = new Intent(PlayerActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void SportsNews(View view)
    {
        Intent intent = new Intent(PlayerActivity.this, SportsNewsActivity.class);
        startActivity(intent);
    }

    public void Profile(View view)
    {
        Intent intent = new Intent(PlayerActivity.this, ProfileActivity.class);
        startActivity(intent);
    }
}