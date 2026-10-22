package com.example.demo.services;

import com.example.demo.entities.Movie;

import java.util.List;

public interface IMovieService {
    List<Movie> findAll();
    Movie findById(Long id);
    Movie create(Movie movie);
    Movie update(Long id, Movie movie);
    void delete(Long id);
}
