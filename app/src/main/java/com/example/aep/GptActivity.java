package com.example.aep;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;

import java.io.IOException;

public class GptActivity extends AppCompatActivity {

    private EditText userInputEditText;
    private Button generateButton;
    private TextView generatedTextTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpt);

        userInputEditText = findViewById(R.id.userInputEditText);
        generateButton = findViewById(R.id.generateButton);
        generatedTextTextView = findViewById(R.id.generatedTextTextView);

        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("bro","inside on click");
                generateResponse();
            }
        });
    }

    private void generateResponse() {

        String userInput = userInputEditText.getText().toString();
        Log.i("bro",userInput);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.i("GptActivity", message);
            }
        });
        // Use OkHttp for HTTP requests
        //OkHttpClient client = new OkHttpClient();

        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();
        //String apiUrl = "https://api.openai.com/v1/engines/davinci-codex/completions";
        String apiUrl = "https://api.openai.com/v1/engines/text-davinci-003/completions";
        // Prepare the request body
        String requestBody = "{\"prompt\": \"" + userInput + "\"}";
        Log.i("bro","request body iss: "+requestBody);

        // Create the request
        Request request = new Request.Builder()
                .url(apiUrl)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer "+BuildConfig.OPENAI_API_KEY)
                .post(RequestBody.create(MediaType.parse("application/json"),requestBody))
                .build();



        // Make the request
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.i("bro","Inside response");
                    final String responseBody = response.body().string();
                    Log.i("bro",responseBody);

                    // Update UI on the main thread
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            generatedTextTextView.setText(responseBody);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }
}