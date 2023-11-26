package com.example.aep;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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




