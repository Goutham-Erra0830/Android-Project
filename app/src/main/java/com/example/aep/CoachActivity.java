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
        CardView cardViewDashboardItem1 = findViewById(R.id.teamBuilding);
        CardView cardViewDashboardItem3 = findViewById(R.id.dashboardItem3);

        // Set an OnClickListener for the CardView
        cardViewDashboardItem2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event, start PlayerEvaluationActivity
                playerEvaluation();
            }
        });
        cardViewDashboardItem1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event, start PlayerEvaluationActivity
                teamBuilding();
            }
        });

        cardViewDashboardItem3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event, start PlayerEvaluationActivity
                teamGrowth();
            }
        });
    }
    private void playerEvaluation() {
        Intent intent = new Intent(CoachActivity.this, PlayerEvaluationActivity.class);
        startActivity(intent);
    }
    private void teamBuilding() {
        Intent intent = new Intent(CoachActivity.this, TeamBuildingActivity.class);
        startActivity(intent);
    }

    private void teamGrowth()
    {
        Intent intent = new Intent(CoachActivity.this, PlayerGrowthActivity.class);
        startActivity(intent);
    }
}