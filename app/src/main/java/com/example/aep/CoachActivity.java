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
import android.widget.ViewFlipper;

public class CoachActivity extends AppCompatActivity {

    private ViewFlipper viewFlipper;
    private WebView webView1;
    private WebView webView2;
    private WebView webView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach);

        CardView cardViewDashboardItem2 = findViewById(R.id.dashboardItem2);
        CardView cardViewDashboardItem1 = findViewById(R.id.teamBuilding);

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

        webView1 = findViewById(R.id.youtubeWebView4);
        webView2 = findViewById(R.id.youtubeWebView5);
        webView3 = findViewById(R.id.youtubeWebView6);

        // Set up ViewFlipper
        viewFlipper = findViewById(R.id.viewFlipper);
        viewFlipper.setFlipInterval(5000); // Set the interval between flips in milliseconds
        viewFlipper.startFlipping(); // Start flipping

        // Load YouTube videos
        loadYouTubeVideo(webView1, "-PKH3bITmTM?si=mwsg-UyRpplY7fSe");
        loadYouTubeVideo(webView2, "Xa-PIcqe1I0?si=p3wwg4ZLJuQ9v3-u");
        loadYouTubeVideo(webView3, "FNAsmZV6u0g?si=KtNwP3SWEUhKVVgk");
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
}