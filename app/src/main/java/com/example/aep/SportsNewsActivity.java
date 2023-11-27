package com.example.aep;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SportsNewsActivity extends AppCompatActivity {

    private String sourceurl;
    private RecyclerView recyclerView;
    private SportsNewsAdapter newsAdapter;


    private List<NewsItem> newsitems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports_news);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Create and set the adapter
        newsAdapter = new SportsNewsAdapter(new ArrayList<>());
        recyclerView.setAdapter(newsAdapter);


        Toolbar toolbar;
        toolbar = findViewById(R.id.toolbarsportsnews);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setTitle("Plan Events");
        // Get ActionBar reference
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            // Set the navigation (up) button color to white
            final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back); // Replace with your arrow drawable
            upArrow.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
            actionBar.setHomeAsUpIndicator(upArrow);

            // Set the title text color to white
            int textColor = getResources().getColor(android.R.color.white);
            Spannable text = new SpannableString("Plan Events");
            text.setSpan(new ForegroundColorSpan(textColor), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

            actionBar.setTitle(text);

            actionBar.setDisplayHomeAsUpEnabled(true);
        }

       fetchData();

        // Initialize Glide
        Glide.init(this, new GlideBuilder());


    }

    private void fetchData() {

        // Create Retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://newsapi.org/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        // Make a network request
        Call<NewsApiResponse> call = apiService.getNews();
        call.enqueue(new Callback<NewsApiResponse>() {
            @Override
            public void onResponse(Call<NewsApiResponse> call, Response<NewsApiResponse> response) {
                if (response.isSuccessful()&& response.body() != null) {
                    NewsApiResponse apiResponse = response.body();
                    Log.d("Urlresponse", String.valueOf(response.body()));
                    //Update the adapter with the fetched data
                    List<NewsItem> newsList = apiResponse.getArticles();
                    Log.d("Urlresponse", String.valueOf(apiResponse.getArticles()));
                    newsAdapter.setItems(newsList);
                } else {
                    Log.e("SportsNewsActivity", "Unsuccessful response: " + response.raw().toString());
                }
            }

            @Override
            public void onFailure(Call<NewsApiResponse>call, Throwable t) {
                // Handle network errors
                Log.e("SportsNewsActivity", "Network request failed", t);
                // Show a user-friendly error message if needed
                Toast.makeText(SportsNewsActivity.this, "Network request failed", Toast.LENGTH_SHORT).show();

            }
        });
    }


    }




