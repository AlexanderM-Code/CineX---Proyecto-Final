package com.example.demo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/** Película de la cartelera. tmdbId enlaza con la ficha de la película en TMDB. */
@Entity
@Table(name = "movie")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tmdb_id", unique = true)
    private Long tmdbId;

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 200, message = "El título admite máximo 200 caracteres")
    @Column(nullable = false, length = 200)
    private String title;

    @Size(max = 80, message = "El género admite máximo 80 caracteres")
    @Column(length = 80)
    private String genre;

    @Size(max = 1000, message = "La descripción admite máximo 1000 caracteres")
    @Column(length = 1000)
    private String description;

    @Size(max = 500, message = "La URL de la imagen admite máximo 500 caracteres")
    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @NotNull(message = "La duración es obligatoria")
    @Positive(message = "La duración debe ser mayor que cero")
    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    public Movie() {}

    public Movie(Long tmdbId, String title, String genre, String description, String imageUrl, Integer durationMinutes) {
        this.tmdbId = tmdbId;
        this.title = title;
        this.genre = genre;
        this.description = description;
        this.imageUrl = imageUrl;
        this.durationMinutes = durationMinutes;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getTmdbId() { return tmdbId; }
    public void setTmdbId(Long tmdbId) { this.tmdbId = tmdbId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }
}
