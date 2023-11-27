package com.example.aep;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ViewFlipper;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Discover extends AppCompatActivity {

    private WebView webView1;
    private WebView webView2;
    private WebView webView3;
    private WebView webView4;

    private ViewFlipper viewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);

        // Initialize WebViews
        webView1 = findViewById(R.id.youtubeWebView1);
        webView2 = findViewById(R.id.youtubeWebView2);
        webView3 = findViewById(R.id.youtubeWebView3);
        webView4 = findViewById(R.id.youtubeWebView4);

        // Load YouTube videos
        loadYouTubeVideo(webView1, "cicDyoOiTrk?si=onQl19jYRQlfBGwE");
        loadYouTubeVideo(webView2, "pP533zVKzPk?si=Qs3UPUb7HlVTgFDr");
        loadYouTubeVideo(webView3, "rnqRO3u5bCY?si=EsrOzvEOgh8D0mEn");
        loadYouTubeVideo(webView4, "rnqRO3u5bCY?si=EsrOzvEOgh8D0mEn");

    }

    private void loadYouTubeVideo(WebView webView, String videoId) {
        String video = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/" + videoId +
                "?autoplay=1\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
        webView.loadData(video, "text/html", "utf-8");
        Log.d("videourl", video);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
    }
}