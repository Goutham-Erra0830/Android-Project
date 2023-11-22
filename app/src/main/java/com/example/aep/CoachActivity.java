package com.example.aep;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CoachActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach);

        CardView cardViewDashboardItem2 = findViewById(R.id.dashboardItem2);

        // Set an OnClickListener for the CardView
        cardViewDashboardItem2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event, start PlayerEvaluationActivity
                playerEvaluation();
            }
        });
    }
    private void playerEvaluation() {
        Intent intent = new Intent(CoachActivity.this, PlayerEvaluationActivity.class);
        startActivity(intent);
    }
}