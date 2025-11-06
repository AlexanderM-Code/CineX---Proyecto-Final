package com.example.demo.services;

import com.example.demo.entities.Movie;
import com.example.demo.repositories.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService implements IMovieService { // Implementa la interfaz

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    @Override
    public Movie getMovieById(String id) {
        return movieRepository.findById(id);
    }

    // NUEVA IMPLEMENTACIÓN DEL MÉTODO
    @Override
    public Movie createMovie(Movie movie) {
        return movieRepository.save(movie);
    }
}
