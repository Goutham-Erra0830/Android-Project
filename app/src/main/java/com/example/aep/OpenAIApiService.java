package com.example.aep;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
public interface OpenAIApiService {
    @POST("/v1/chat/completions") // Adjust the endpoint as needed
    Call<ChatGPTResponse> getResponse(@Header("Authorization") String authHeader, @Body ChatGPTRequest request);
}
