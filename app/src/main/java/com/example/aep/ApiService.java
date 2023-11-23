package com.example.aep;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("everything?q=cricket&apiKey=f4587d4087644352b260f998de3cebe1")
    Call<NewsApiResponse> getNews();
}
