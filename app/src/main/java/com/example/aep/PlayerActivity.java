package com.example.aep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PlayerActivity extends AppCompatActivity {

    String userid;

    private String x;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        // Get the Intent that started this activity
        Intent intent = getIntent();

        if (intent.hasExtra("userid")) {
            // Retrieve the string from the Intent
            userid = intent.getStringExtra("userid");
        }

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
    public void GptActivity(View view){
        Intent intent = new Intent(PlayerActivity.this, GptActivity.class);
        startActivity(intent);
    }

    public void PlayerInsights(View view)
    {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("users").document(userid).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        Log.i("userid", userid);
                        if (document.exists()) {

                             String Fullname = document.getString("full_name");
                            Intent intent = new Intent(PlayerActivity.this, PlayerInsightsActivity.class);
                            intent.putExtra("playerFullname", Fullname);
                            startActivity(intent);
                        } else {
                            // Handle the case where the document doesn't exist
                            Log.e("PlayerStatisticsActivity", "Document does not exist");
                        }
                    } else {
                        // Handle exceptions
                        Log.e("PlayerStatisticsActivity", "Error getting TotalHalfCenturies", task.getException());
                    }
                });


    }
}