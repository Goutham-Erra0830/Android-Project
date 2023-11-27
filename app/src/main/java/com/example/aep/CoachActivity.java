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
import android.widget.TextView;
import android.widget.ViewFlipper;

public class CoachActivity extends AppCompatActivity {

    private ViewFlipper viewFlipper;
    private WebView webView1;
    private WebView webView2;
    private WebView webView3;

    private TextView coachname;
    private String coachcurrentname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach);

        CardView cardViewDashboardItem2 = findViewById(R.id.dashboardItem2);
        CardView cardViewDashboardItem1 = findViewById(R.id.teamBuilding);
        CardView cardViewDashboardItem3 = findViewById(R.id.dashboardItem3);
        CardView cardViewDashboardItem4 = findViewById(R.id.dashboardItem4);
        coachname=findViewById(R.id.textCopy);

        Intent intent = getIntent();

        if (intent.hasExtra("current_username")) {

            coachcurrentname= intent.getStringExtra("current_username");
        }

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
        cardViewDashboardItem4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event, start PlayerEvaluationActivity
                planEvent();
            }
        });

        coachname.setText(coachcurrentname);

    }

    private void loadYouTubeVideo(WebView webView, String videoId) {
        String video = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/" + videoId +
                "?autoplay=1\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
        webView.loadData(video, "text/html", "utf-8");
        Log.d("videourl",video );
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
    }

    private void playerEvaluation() {
        Intent intent = new Intent(CoachActivity.this, PlayerEvaluationActivity.class);
        startActivity(intent);
    }
    private void teamBuilding() {
        Intent intent = new Intent(CoachActivity.this, TeamBuildingActivity.class);
        startActivity(intent);
    }
    private void planEvent() {
        Intent intent = new Intent(CoachActivity.this, PlanEventActivity.class);
        startActivity(intent);
    }

    private void teamGrowth()
    {
        Intent intent = new Intent(CoachActivity.this, PlayerGrowthActivity.class);
        startActivity(intent);
    }

    public void Eventscoach(View view)
    {
        Intent intent = new Intent(CoachActivity.this, EventActivity.class);
        startActivity(intent);
    }

}