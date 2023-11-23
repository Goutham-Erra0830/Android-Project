package com.example.aep;
import java.util.List;

public class NewsApiResponse {
    private String status;
    private int totalResults;
    private List<NewsItem> articles;

    // Add getters and setters as needed

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public List<NewsItem> getArticles() {
        return articles;
    }

    public void setArticles(List<NewsItem> articles) {
        this.articles = articles;
    }
}

