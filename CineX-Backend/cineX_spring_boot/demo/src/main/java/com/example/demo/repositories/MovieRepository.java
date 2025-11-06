package com.example.demo.repositories;

import com.example.demo.entities.Movie;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class MovieRepository {
    private final List<Movie> movies = new ArrayList<>();

    public MovieRepository() {
        // Datos de ejemplo en memoria
        movies.add(new Movie("tt0111161", "The Shawshank Redemption", "Drama", "Two imprisoned men bond over a number of years...", "url_to_image", "142 min"));
        movies.add(new Movie("tt0068646", "The Godfather", "Crime", "The aging patriarch of an organized crime dynasty...", "url_to_image", "175 min"));
        movies.add(new Movie("tt0468569", "The Dark Knight", "Action", "When the menace known as the Joker wreaks havoc...", "url_to_image", "152 min"));
    }

    public List<Movie> findAll() {
        return movies;
    }

    public Movie findById(String id) {
        return movies.stream()
                .filter(movie -> movie.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // NUEVO MÉTODO PARA GUARDAR
    public Movie save(Movie movie) {
        // Asigna un ID único y aleatorio a la nueva película
        movie.setId(UUID.randomUUID().toString());
        movies.add(movie);
        return movie;
    }
}
