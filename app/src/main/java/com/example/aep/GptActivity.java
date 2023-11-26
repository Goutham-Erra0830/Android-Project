package com.example.aep;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class GptActivity extends AppCompatActivity {

    private EditText editQuery;
    private TextView tvResponse;
    private Button btnSend;
    private OpenAIApiService apiService;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpt);

        editQuery = findViewById(R.id.editQuery);
        tvResponse = findViewById(R.id.tvResponse);
        btnSend = findViewById(R.id.btnSend);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..."); // Set your message here
        progressDialog.setCancelable(false);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(45, TimeUnit.SECONDS) // Increase connect timeout
                .readTimeout(45, TimeUnit.SECONDS)    // Increase read timeout
                .writeTimeout(45, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openai.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        apiService = retrofit.create(OpenAIApiService.class);

        btnSend.setOnClickListener(view -> sendQuery());
    }

    private void sendQuery() {
        progressDialog.show();
        String query = editQuery.getText().toString();
        ChatGPTRequest request = new ChatGPTRequest();

        List<ChatGPTRequest.Message> messages = new ArrayList<>();
        messages.add(new ChatGPTRequest.Message("user", query));  // Assuming "user" is the sender
        request.setMessages(messages);
        Log.i("popeye","inside sendquery");
        //request.setPrompt(query);
        request.setModel("gpt-3.5-turbo");

        String apiKey = "Bearer " +BuildConfig.OPENAI_API_KEY; // Replace with your actual API key
        apiService.getResponse(apiKey, request).enqueue(new Callback<ChatGPTResponse>() {
            @Override
            public void onResponse(Call<ChatGPTResponse> call, Response<ChatGPTResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    ChatGPTResponse chatGPTResponse = response.body();
                    List<ChatGPTResponse.Choice> choices = chatGPTResponse.getChoices();
                    if (choices != null && !choices.isEmpty()) {
                        // Assuming you want the first choice's message content
                        String reply = choices.get(0).getMessage().getContent();
                        tvResponse.setText(reply);
                    }
                }
            }

            @Override
            public void onFailure(Call<ChatGPTResponse> call, Throwable t) {
                progressDialog.dismiss();
                tvResponse.setText("Error: " + t.getMessage());
            }
        });
    }
}