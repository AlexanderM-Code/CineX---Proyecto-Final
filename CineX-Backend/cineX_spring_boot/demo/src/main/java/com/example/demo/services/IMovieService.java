package com.example.demo.services;

import com.example.demo.entities.Movie;
import java.util.List;

public interface IMovieService {
    List<Movie> getAllMovies();
    Movie getMovieById(String id);
    Movie createMovie(Movie movie); // NUEVA FIRMA DEL MÉTODO
}
