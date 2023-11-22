package com.example.aep;
public class NewsItem {
    private String title;
    private String description;
    private String sourceName;
    private String sourceUrl;
    private String imageUrl;

    // Constructor
    public NewsItem(String title, String description, String sourceName, String sourceUrl, String imageUrl) {
        this.title = title;
        this.description = description;
        this.sourceName = sourceName;
        this.sourceUrl = sourceUrl;
        this.imageUrl = imageUrl;
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

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

