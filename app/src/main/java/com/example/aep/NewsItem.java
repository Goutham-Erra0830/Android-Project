package com.example.aep;

public class NewsItem {
    private String title;
    private String description;
    private String sourceName;

    private String url;

    private String urlToImage;

    // Constructor
    public NewsItem(String title, String description, String sourceName, String url, String urlToImage) {
        this.title = title;
        this.description = description;
        this.sourceName = sourceName;
        this.url = url;
        this.urlToImage = urlToImage;
    }

    // Getters and setters (you can generate these automatically in most IDEs)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }
}

