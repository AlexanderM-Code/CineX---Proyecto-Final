package com.example.demo.services;

import com.example.demo.entities.Movie;
import com.example.demo.exceptions.BusinessRuleException;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.repositories.MovieRepository;
import com.example.demo.repositories.ShowtimeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MovieService implements IMovieService {

    private final MovieRepository movieRepository;
    private final ShowtimeRepository showtimeRepository;

    public MovieService(MovieRepository movieRepository, ShowtimeRepository showtimeRepository) {
        this.movieRepository = movieRepository;
        this.showtimeRepository = showtimeRepository;
    }

    @Override
    public List<Movie> findAll() {
        return movieRepository.findAll();
    }

    @Override
    public Movie findById(Long id) {
        return movieRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Película", id));
    }

    @Override
    @Transactional
    public Movie create(Movie movie) {
        movie.setId(null); // el id lo asigna la base de datos
        return movieRepository.save(movie);
    }

    @Override
    @Transactional
    public Movie update(Long id, Movie datos) {
        Movie movie = findById(id);
        movie.setTmdbId(datos.getTmdbId());
        movie.setTitle(datos.getTitle());
        movie.setGenre(datos.getGenre());
        movie.setDescription(datos.getDescription());
        movie.setImageUrl(datos.getImageUrl());
        movie.setDurationMinutes(datos.getDurationMinutes());
        return movieRepository.save(movie);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Movie movie = findById(id);
        if (showtimeRepository.existsByMovieId(id)) {
            throw new BusinessRuleException("No se puede eliminar \"" + movie.getTitle() + "\" porque tiene funciones programadas");
        }
        movieRepository.delete(movie);
    }
}
