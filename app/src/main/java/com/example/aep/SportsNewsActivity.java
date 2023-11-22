package com.example.aep;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import com.example.aep.NewsItem;

import io.grpc.internal.JsonParser;
import com.google.gson.JsonObject;





public class SportsNewsActivity extends AppCompatActivity {

    private String sourceurl;
    private RecyclerView recyclerView;
    private SportsNewsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports_news);

        //android:id="@+id/newsimage"
        //android:id="@+id/newsslide1"




        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //List<NewsItem> newsItemList = getNewsItemList();
        //adapter = new SportsNewsAdapter(newsItemList);
        //recyclerView.setAdapter(adapter);

        adapter = new SportsNewsAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        DisplayNews news = new DisplayNews();
        news.execute();

    }

    private class DisplayNews extends AsyncTask<String, Integer, List<NewsItem>> {

        JsonObject jsondata;
        private Bitmap newsimage;

        private String title;

        private String description;

        private String sourceName;

        List<NewsItem> newsItemList = new ArrayList<>();


        @Override
        protected List<NewsItem> doInBackground(String... strings) {

            String result = "";
            try {


                URL url = new URL("https://newsapi.org/v2/everything?q=cricket&apiKey=f4587d4087644352b260f998de3cebe1");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3");
                connection.connect();

                InputStream inputStream = connection.getInputStream();

                com.google.gson.JsonParser parser = new com.google.gson.JsonParser();
                jsondata = parser.parse(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).getAsJsonObject();

                Log.d("YourActivity", String.valueOf(jsondata));
                JsonParser jsonParser = new JsonParser();

                jsonParser.parseJson(String.valueOf(jsondata));



            }
            catch (Exception e) {
                e.printStackTrace();
                Log.e("connectionandroid", "Error occurred: " + e.getMessage(), e);
            }
            return newsItemList;
        }


        @Override
        protected void onPostExecute(List<NewsItem> newsItemList) {

            /*TextView Title = findViewById(R.id.textTitle);
            Title.setText(title);
            Log.d("newstitle", title);

            TextView textDescription = findViewById(R.id.textDescription);
            textDescription.setText(description);

            TextView textSourceName = findViewById(R.id.textSourceName);
            textSourceName.setText(sourceName);

            TextView textLink = findViewById(R.id.textLink);
            textLink.setText(sourceurl);

            ImageView newsImageview = findViewById(R.id.imageView);
            newsImageview.setImageBitmap(newsimage);*/

            adapter.updateData(newsItemList);


        }


        private class JsonParser {

            public List<NewsItem> parseJson(String jsondata) {
                try {
                    JSONObject jsonObject = new JSONObject(jsondata);

                    // Extracting values from the root object
                    String status = jsonObject.getString("status");
                    Log.d("json", status);
                    int totalResults = jsonObject.getInt("totalResults");
                    Log.d("json", String.valueOf(totalResults));

                    JSONArray articlesArray = jsonObject.getJSONArray("articles");

                    // Loop through the articles array articlesArray.length()
                    for (int i = 0; i < 4; i++) {
                        JSONObject articleObject = articlesArray.getJSONObject(i);

                        // Extracting values from each article object
                        JSONObject sourceObject = articleObject.getJSONObject("source");
                        String sourceId = sourceObject.getString("id");
                        sourceName = sourceObject.getString("name");

                        String author = articleObject.getString("author");
                        title = articleObject.getString("title");
                        description = articleObject.isNull("description") ? "" : articleObject.getString("description");
                        sourceurl = articleObject.getString("url");
                        String urlToImage = articleObject.isNull("urlToImage") ? "" : articleObject.getString("urlToImage");
                        String publishedAt = articleObject.getString("publishedAt");
                        String content = articleObject.isNull("content") ? "" : articleObject.getString("content");
                        URL icon = new URL(urlToImage);
                        newsimage = BitmapFactory.decodeStream(icon.openStream());

                        NewsItem newsItem = new NewsItem(title, description, sourceName, sourceurl, urlToImage);
                        newsItemList.add(newsItem);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return newsItemList;
            }
        }


    }


    public void openInBrowser(View view)
    {


        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sourceurl));

        // Check if there's an app to handle the Intent
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the activity (opens the browser)
            startActivity(intent);
        } else {
            // Handle the case where there's no app to handle the Intent
            Toast.makeText(getApplicationContext(), "No app to handle the link", Toast.LENGTH_SHORT).show();
        }

      /*  // Create an Intent to open the browser
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sourceurl));

        // Check if there's an app to handle the Intent
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the activity (opens the browser)
            startActivity(intent);
        } else {
            // Handle the case where there's no app to handle the Intent
            Toast.makeText(getApplicationContext(), "No app to handle the link", Toast.LENGTH_SHORT).show();
        }*/

        //WebView webView = findViewById(R.id.webView);

        // Show the WebView and load the URL
      //  webView.setVisibility(View.VISIBLE);
        //webView.loadUrl(sourceurl);
    }

}

