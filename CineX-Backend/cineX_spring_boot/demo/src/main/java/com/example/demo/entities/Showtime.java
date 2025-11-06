package com.example.demo.entities;

import java.time.LocalDateTime;

// Hereda de BaseEntity
public class Showtime extends BaseEntity {
    private String movieId;
    private int roomId;
    private LocalDateTime showDateTime;

    public Showtime() {}

    public Showtime(int id, String movieId, int roomId, LocalDateTime showDateTime) {
        this.id = id; // 'id' viene de BaseEntity
        this.movieId = movieId;
        this.roomId = roomId;
        this.showDateTime = showDateTime;
    }

    // Getters y Setters (ya no se necesita getId/setId)
    public String getMovieId() { return movieId; }
    public void setMovieId(String movieId) { this.movieId = movieId; }

    public int getRoomId() { return roomId; }
    public void setRoomId(int roomId) { this.roomId = roomId; }

    public LocalDateTime getShowDateTime() { return showDateTime; }
    public void setShowDateTime(LocalDateTime showDateTime) { this.showDateTime = showDateTime; }
}
