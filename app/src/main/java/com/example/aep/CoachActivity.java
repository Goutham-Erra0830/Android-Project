package com.example.aep;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;

public class CoachActivity extends AppCompatActivity {

    private ViewFlipper viewFlipper;
    private WebView webView1;
    private WebView webView2;
    private WebView webView3;
    private String userid;
    private String fullname;

    private TextView coachname;
    private String coachcurrentname;

    private ImageButton profileCoach;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach);

        profileCoach = findViewById(R.id.profileCoach);
        coachname=findViewById(R.id.textCopycoach);
        Intent intent = getIntent();


        if (intent.hasExtra("userid") && intent.hasExtra("current_username")) {
            // Retrieve the string from the Intent
            userid = intent.getStringExtra("userid");
            fullname= intent.getStringExtra("current_username");
        }



        ImageView gifImageView = findViewById(R.id.gifImageView);
        Glide.with(this)
                .load(R.drawable.cricket)
                .override(2050, 450)  // Set desired dimensions
                .into(gifImageView);

        CardView cardViewDashboardItem2 = findViewById(R.id.dashboardItem2);
        CardView cardViewDashboardItem1 = findViewById(R.id.teamBuilding);
        CardView cardViewDashboardItem3 = findViewById(R.id.dashboardItem3);
        CardView cardViewDashboardItem4 = findViewById(R.id.dashboardItem4);

        coachname.setText(fullname);


        profileCoach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the EditProfileActivity
                Intent intent = new Intent(CoachActivity.this, ProfileActivity.class);
                intent.putExtra("userid", userid);
                intent.putExtra("fullname", fullname);
                startActivity(intent);
            }
        });

    }


    public void playerEvaluation(View view) {
        Intent intent = new Intent(CoachActivity.this, PlayerEvaluationActivity.class);
        startActivity(intent);
    }
    public void teamBuilding(View view) {
        Intent intent = new Intent(CoachActivity.this, TeamBuildingActivity.class);
        startActivity(intent);
    }
    public void planEvent(View view) {
        Intent intent = new Intent(CoachActivity.this, PlanEventActivity.class);
        startActivity(intent);
    }

    public void teamGrowth(View view)
    {
        Intent intent = new Intent(CoachActivity.this, PlayerGrowthActivity.class);
        startActivity(intent);
    }


    public void Eventscoach(View view)
    {
        Intent intent = new Intent(CoachActivity.this, EventActivity.class);
        startActivity(intent);
    }

    public void Discover(View view)
    {
        Intent intent = new Intent(CoachActivity.this, Discover.class);
        startActivity(intent);
    }

}