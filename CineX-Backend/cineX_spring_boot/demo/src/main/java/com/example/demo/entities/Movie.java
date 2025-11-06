package com.example.demo.entities;

public class Movie {
    private String id;
    private String title;
    private String genre;
    private String description;
    private String imageUrl;
    private String duration;

    public Movie() {}

    public Movie(String id, String title, String genre, String description, String imageUrl, String duration) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.description = description;
        this.imageUrl = imageUrl;
        this.duration = duration;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }
}
