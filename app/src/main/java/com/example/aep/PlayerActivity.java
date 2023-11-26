package com.example.aep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PlayerActivity extends AppCompatActivity {

    private ViewFlipper viewFlipper;
    private WebView webView1;
    private WebView webView2;
    private WebView webView3;
    private ImageView events;

    String userid;
    String fullname;
    private TextView playername;

    private String x;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Intent intent = getIntent();

        if (intent.hasExtra("userid") && intent.hasExtra("current_username")) {
            // Retrieve the string from the Intent
            userid = intent.getStringExtra("userid");
            fullname= intent.getStringExtra("current_username");
        }

        playername=findViewById(R.id.textCopy);
        playername.setText(fullname);

        events=findViewById(R.id.myevents);
        events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PlayerActivity.this, EventActivity.class));
            }
        });

       /* // Initialize WebViews
        webView1 = findViewById(R.id.youtubeWebView1);
        webView2 = findViewById(R.id.youtubeWebView2);
        webView3 = findViewById(R.id.youtubeWebView3);

        // Set up ViewFlipper
        viewFlipper = findViewById(R.id.viewFlipper);
        viewFlipper.setFlipInterval(5000); // Set the interval between flips in milliseconds
        viewFlipper.startFlipping(); // Start flipping

        // Load YouTube videos
        loadYouTubeVideo(webView1, "-PKH3bITmTM?si=mwsg-UyRpplY7fSe");
        loadYouTubeVideo(webView2, "Xa-PIcqe1I0?si=p3wwg4ZLJuQ9v3-u");
        loadYouTubeVideo(webView3, "FNAsmZV6u0g?si=KtNwP3SWEUhKVVgk");  */

    }

    private void loadYouTubeVideo(WebView webView, String videoId) {
        String video = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/" + videoId +
                "?autoplay=1\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
        webView.loadData(video, "text/html", "utf-8");
        Log.d("videourl",video );
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        // Get the Intent that started this activity


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

    public void Profile(View view)
    {
        Intent intent = new Intent(PlayerActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    public void PlayerInsights(View view)
    {
        Intent intent = new Intent(PlayerActivity.this, PlayerInsightsActivity.class);
        intent.putExtra("playerFullname", fullname);
        startActivity(intent);
    }
}