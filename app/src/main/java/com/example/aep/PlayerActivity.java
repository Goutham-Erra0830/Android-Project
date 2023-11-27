package com.example.aep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.Button;

import com.anychart.AnyChart;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.palettes.RangeColors;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class PlayerActivity extends AppCompatActivity {

    private ViewFlipper viewFlipper;
    private WebView webView1;
    private WebView webView2;
    private WebView webView3;
    private ImageView events;
    private TextView rating;
    private TextView matchesplayed;
    private TextView strikerate;

    private String userid;
    private String fullname;
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
        ImageButton discoverButton = findViewById(R.id.discover);
        discoverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event, navigate to the DiscoverActivity
                Intent intent = new Intent(PlayerActivity.this, Discover.class);
                startActivity(intent);
            }
        });
       /* events.setOnClickListener(new View.OnClickListener() {

       /* events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PlayerActivity.this, EventActivity.class));
            }
        });*/

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

        rating=findViewById(R.id.Rating);
        matchesplayed=findViewById(R.id.Matchesplayed);
        strikerate=findViewById(R.id.Strikerate);

        readPlayerstats();

    }

    private void readPlayerstats() {
        // Add a ValueEventListener to read data from Firebase
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        database.collection("playerstats").document(fullname).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            rating.setText(String.valueOf(document.getLong("rating")));
                            matchesplayed.setText(String.valueOf(document.getLong("matchesPlayed")));
                            strikerate.setText(String.valueOf(document.getLong("strikeRate")));

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
        intent.putExtra("userid", userid);
        intent.putExtra("fullname", fullname);
        startActivity(intent);
    }

    public void PlayerInsights(View view)
    {
        Intent intent = new Intent(PlayerActivity.this, PlayerInsightsActivity.class);
        intent.putExtra("playerFullname", fullname);
        startActivity(intent);
    }

    public void BMI(View view)
    {
        Intent intent = new Intent(PlayerActivity.this, BMIActivity.class);
        //intent.putExtra("playerFullname", fullname);
        startActivity(intent);
    }

    public void Events(View view)
    {
        Intent intent = new Intent(PlayerActivity.this, EventActivity.class);
        startActivity(intent);
    }



}