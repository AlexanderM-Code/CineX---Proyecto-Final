package com.example.demo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Función: una película proyectada en una sala a una hora.
 * Una sala no puede tener dos funciones a la misma hora (restricción única).
 */
@Entity
@Table(name = "showtime", uniqueConstraints =
        @UniqueConstraint(name = "uk_showtime_room_time", columnNames = {"room_id", "show_date_time"}))
public class Showtime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La película es obligatoria")
    @ManyToOne(optional = false)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @NotNull(message = "La sala es obligatoria")
    @ManyToOne(optional = false)
    @JoinColumn(name = "room_id", nullable = false)
    private CinemaRoom room;

    @NotNull(message = "La fecha y hora son obligatorias")
    @Column(name = "show_date_time", nullable = false)
    private LocalDateTime showDateTime;

    @NotNull(message = "El precio es obligatorio")
    @PositiveOrZero(message = "El precio no puede ser negativo")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    public Showtime() {}

    public Showtime(Movie movie, CinemaRoom room, LocalDateTime showDateTime, BigDecimal price) {
        this.movie = movie;
        this.room = room;
        this.showDateTime = showDateTime;
        this.price = price;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Movie getMovie() { return movie; }
    public void setMovie(Movie movie) { this.movie = movie; }

    public CinemaRoom getRoom() { return room; }
    public void setRoom(CinemaRoom room) { this.room = room; }

    public LocalDateTime getShowDateTime() { return showDateTime; }
    public void setShowDateTime(LocalDateTime showDateTime) { this.showDateTime = showDateTime; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
}
